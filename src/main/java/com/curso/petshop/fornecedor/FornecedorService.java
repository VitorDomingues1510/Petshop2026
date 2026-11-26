package com.curso.petshop.fornecedor;

import com.curso.petshop.comum.ExclusaoNaoPermitidaException;
import com.curso.petshop.comum.RecursoDuplicadoException;
import com.curso.petshop.comum.RecursoNaoEncontradoException;
import com.curso.petshop.produto.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FornecedorService {

    private final FornecedorRepository repository;
    private final ProdutoRepository produtoRepository;

    public FornecedorService(
            FornecedorRepository repository,
            ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Fornecedor cadastrar(Fornecedor fornecedor) {
        if (repository.existsByCnpj(fornecedor.getCnpj())) {
            throw new RecursoDuplicadoException("CNPJ já cadastrado");
        }
        return repository.save(fornecedor);
    }

    @Transactional(readOnly = true)
    public Fornecedor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Fornecedor não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Fornecedor> listar() {
        return repository.findAll();
    }

    @Transactional
    public Fornecedor alterar(Long id, Fornecedor dados) {
        Fornecedor fornecedor = buscarPorId(id);
        if (repository.existsByCnpjAndIdNot(dados.getCnpj(), id)) {
            throw new RecursoDuplicadoException("CNPJ já cadastrado");
        }
        fornecedor.alterarDados(dados.getRazaoSocial(), dados.getCnpj());
        return fornecedor;
    }

    @Transactional
    public void excluir(Long id) {
        Fornecedor fornecedor = buscarPorId(id);
        if (produtoRepository.existsByFornecedorId(id)) {
            throw new ExclusaoNaoPermitidaException(
                    "Não é possível excluir: o fornecedor possui produtos vinculados");
        }
        repository.delete(fornecedor);
        repository.flush();
    }
}
