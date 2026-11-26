package com.curso.petshop.categoria;

import com.curso.petshop.comum.ExclusaoNaoPermitidaException;
import com.curso.petshop.comum.RecursoDuplicadoException;
import com.curso.petshop.comum.RecursoNaoEncontradoException;
import com.curso.petshop.comum.Status;
import com.curso.petshop.produto.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaProdutoService {

    private final CategoriaProdutoRepository repository;
    private final ProdutoRepository produtoRepository;

    public CategoriaProdutoService(
            CategoriaProdutoRepository repository,
            ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public CategoriaProduto cadastrar(String nome) {
        if (repository.existsByNomeIgnoreCase(nome)) {
            throw new RecursoDuplicadoException("Nome da categoria já cadastrado");
        }
        return repository.save(new CategoriaProduto(nome));
    }

    @Transactional(readOnly = true)
    public CategoriaProduto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria de produto não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<CategoriaProduto> listar() {
        return repository.findAll();
    }

    @Transactional
    public CategoriaProduto alterar(Long id, String nome) {
        CategoriaProduto categoria = buscarPorId(id);
        repository.findByNomeIgnoreCase(nome)
                .filter(outra -> !outra.getId().equals(id))
                .ifPresent(outra -> {
                    throw new RecursoDuplicadoException("Nome da categoria já cadastrado");
                });
        categoria.alterarNome(nome);
        return categoria;
    }

    @Transactional
    public CategoriaProduto alterarStatus(Long id, Status status) {
        CategoriaProduto categoria = buscarPorId(id);
        if (status == Status.ATIVO) {
            categoria.ativar();
        } else {
            categoria.inativar();
        }
        return categoria;
    }

    @Transactional
    public void excluir(Long id) {
        CategoriaProduto categoria = buscarPorId(id);
        if (produtoRepository.existsByCategoriaId(id)) {
            throw new ExclusaoNaoPermitidaException(
                    "Não é possível excluir: a categoria possui produtos vinculados");
        }
        repository.delete(categoria);
        repository.flush();
    }
}
