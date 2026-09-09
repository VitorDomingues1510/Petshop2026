package com.curso.petshop.application;

import com.curso.petshop.domain.CategoriaProduto;
import com.curso.petshop.repository.CategoriaProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaProdutoService {

    private final CategoriaProdutoRepository repository;

    public CategoriaProdutoService(CategoriaProdutoRepository repository) {
        this.repository = repository;
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
}
