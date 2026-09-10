package com.curso.petshop.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GrupoProdutoTest {

    @Test
    void deveAdicionarProdutoEManejarOsDoisLadosDaAssociacao() {
        GrupoProduto grupo = new GrupoProduto("Ração");
        Produto produto = novoProduto("7891234560001");

        grupo.adicionarProduto(produto);

        assertEquals(1, grupo.getProdutos().size());
        assertSame(produto, grupo.getProdutos().getFirst());
        assertSame(grupo, produto.getGrupo());
    }

    @Test
    void naoDeveAdicionarProdutoNulo() {
        GrupoProduto grupo = new GrupoProduto("Ração");

        assertThrows(NullPointerException.class, () -> grupo.adicionarProduto(null));
    }

    @Test
    void naoDeveAdicionarDoisProdutosComOMesmoCodigo() {
        GrupoProduto grupo = new GrupoProduto("Ração");
        grupo.adicionarProduto(novoProduto("7891234560001"));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> grupo.adicionarProduto(novoProduto("7891234560001")));

        assertEquals("Código de barras já utilizado no grupo", excecao.getMessage());
    }

    @Test
    void naoDevePermitirQueProdutoPertençaADoisGrupos() {
        GrupoProduto racao = new GrupoProduto("Ração");
        GrupoProduto higiene = new GrupoProduto("Higiene");
        Produto produto = novoProduto("7891234560001");
        racao.adicionarProduto(produto);

        IllegalStateException excecao = assertThrows(
                IllegalStateException.class,
                () -> higiene.adicionarProduto(produto));

        assertEquals("Produto já pertence a outro grupo", excecao.getMessage());
    }

    @Test
    void naoDeveExporUmaListaInternaModificavel() {
        GrupoProduto grupo = new GrupoProduto("Ração");
        Produto produto = novoProduto("7891234560001");
        grupo.adicionarProduto(produto);

        assertThrows(
                UnsupportedOperationException.class,
                () -> grupo.getProdutos().add(novoProduto("7891234560002")));
    }

    private Produto novoProduto(String codigoBarras) {
        return new Produto(
                codigoBarras,
                "Ração Golden Adulto 15kg",
                new BigDecimal("3.000"),
                new BigDecimal("89.90"),
                LocalDate.of(2026, 8, 20));
    }
}
