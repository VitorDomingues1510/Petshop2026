package com.curso.petshop.categoria;

import com.curso.petshop.produto.Produto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoriaProdutoTest {

    @Test
    void deveAdicionarProdutoEManejarOsDoisLadosDaAssociacao() {
        CategoriaProduto categoria = new CategoriaProduto("Ração");
        Produto produto = novoProduto("7890000000001");

        categoria.adicionarProduto(produto);

        assertEquals(1, categoria.getProdutos().size());
        assertSame(produto, categoria.getProdutos().getFirst());
        assertSame(categoria, produto.getCategoria());
    }

    @Test
    void naoDeveAdicionarProdutoNulo() {
        CategoriaProduto categoria = new CategoriaProduto("Ração");

        assertThrows(NullPointerException.class, () -> categoria.adicionarProduto(null));
    }

    @Test
    void naoDeveAdicionarDoisProdutosComOMesmoCodigo() {
        CategoriaProduto categoria = new CategoriaProduto("Ração");
        categoria.adicionarProduto(novoProduto("7890000000001"));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> categoria.adicionarProduto(novoProduto("7890000000001")));

        assertEquals("Código de barras já utilizado na categoria", excecao.getMessage());
    }

    @Test
    void naoDevePermitirQueProdutoPertençaADuasCategorias() {
        CategoriaProduto racao = new CategoriaProduto("Ração");
        CategoriaProduto higiene = new CategoriaProduto("Higiene");
        Produto produto = novoProduto("7890000000001");
        racao.adicionarProduto(produto);

        IllegalStateException excecao = assertThrows(
                IllegalStateException.class,
                () -> higiene.adicionarProduto(produto));

        assertEquals("Produto já pertence a outra categoria", excecao.getMessage());
    }

    @Test
    void naoDeveExporUmaListaInternaModificavel() {
        CategoriaProduto categoria = new CategoriaProduto("Ração");
        Produto produto = novoProduto("7890000000001");
        categoria.adicionarProduto(produto);

        assertThrows(
                UnsupportedOperationException.class,
                () -> categoria.getProdutos().add(novoProduto("7890000000002")));
    }

    private Produto novoProduto(String codigoBarras) {
        return new Produto(
                codigoBarras,
                "Ração Premium Cães 15kg",
                new BigDecimal("3.000"),
                new BigDecimal("12.90"),
                LocalDate.of(2026, 8, 20));
    }
}
