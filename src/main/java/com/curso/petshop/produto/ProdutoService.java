package com.curso.petshop.produto;

import com.curso.petshop.categoria.CategoriaProduto;
import com.curso.petshop.categoria.CategoriaProdutoRepository;
import com.curso.petshop.comum.ExclusaoNaoPermitidaException;
import com.curso.petshop.comum.RecursoDuplicadoException;
import com.curso.petshop.comum.RecursoNaoEncontradoException;
import com.curso.petshop.comum.Status;
import com.curso.petshop.fornecedor.Fornecedor;
import com.curso.petshop.fornecedor.FornecedorRepository;
import com.curso.petshop.venda.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaProdutoRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;
    private final VendaRepository vendaRepository;

    public ProdutoService(
            ProdutoRepository produtoRepository,
            CategoriaProdutoRepository categoriaRepository,
            FornecedorRepository fornecedorRepository,
            VendaRepository vendaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.vendaRepository = vendaRepository;
    }

    @Transactional
    public Produto cadastrar(Produto produto, Long categoriaId, Long fornecedorId) {
        if (produtoRepository.existsByCodigoBarras(produto.getCodigoBarras())) {
            throw new RecursoDuplicadoException("Código de barras já cadastrado");
        }

        CategoriaProduto categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria de produto não encontrada"));
        categoria.adicionarProduto(produto);

        if (fornecedorId != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Fornecedor não encontrado"));
            produto.associarFornecedor(fornecedor);
        }

        return produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return produtoRepository.buscarPorIdComRelacionamentos(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Produto> listar() {
        return produtoRepository.buscarTodosComRelacionamentos();
    }

    @Transactional
    public Produto alterar(Long id, Produto dados, Long categoriaId, Long fornecedorId) {
        Produto produto = buscarPorId(id);

        if (produtoRepository.existsByCodigoBarrasAndIdNot(dados.getCodigoBarras(), id)) {
            throw new RecursoDuplicadoException("Código de barras já cadastrado");
        }

        CategoriaProduto categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria de produto não encontrada"));
        produto.alterarCategoria(categoria);

        if (fornecedorId != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Fornecedor não encontrado"));
            produto.associarFornecedor(fornecedor);
        } else {
            produto.removerFornecedor();
        }

        produto.alterarDados(
                dados.getCodigoBarras(),
                dados.getDescricao(),
                dados.getSaldoEstoque(),
                dados.getValorUnitario(),
                dados.getEstoqueMinimo());
        return produto;
    }

    @Transactional
    public Produto alterarStatus(Long id, Status status) {
        Produto produto = buscarPorId(id);
        if (status == Status.ATIVO) {
            produto.ativar();
        } else {
            produto.inativar();
        }
        return produto;
    }

    @Transactional
    public void excluir(Long id) {
        Produto produto = buscarPorId(id);
        if (vendaRepository.existsByItensProdutoId(id)) {
            throw new ExclusaoNaoPermitidaException(
                    "Não é possível excluir: o produto possui vendas vinculadas");
        }
        produtoRepository.delete(produto);
        produtoRepository.flush();
    }

    @Transactional
    public Produto receberEstoque(Long id, BigDecimal quantidade) {
        Produto produto = buscarPorId(id);
        produto.receberEstoque(quantidade);
        return produto;
    }

    @Transactional
    public Produto retirarEstoque(Long id, BigDecimal quantidade) {
        Produto produto = buscarPorId(id);
        produto.retirarEstoque(quantidade);
        return produto;
    }
}
