package com.curso.petshop.produto;

import com.curso.petshop.categoria.CategoriaProduto;
import com.curso.petshop.comum.Status;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class PersistenciaJpaTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    void devePersistirERelerGrupoEProduto() {
        CategoriaProduto categoria = new CategoriaProduto("Ração");
        Produto produto = new Produto(
                "7891000000019",
                "Ração Premium Cães 15kg",
                new BigDecimal("10.000"),
                new BigDecimal("89.90"),
                LocalDate.of(2026, 3, 10));

        categoria.adicionarProduto(produto);

        entityManager.persist(categoria);
        entityManager.persist(produto);
        entityManager.flush();

        Long produtoId = produto.getId();
        entityManager.clear();

        Produto produtoRecuperado = entityManager.find(Produto.class, produtoId);

        assertNotNull(produtoRecuperado);
        assertEquals("Ração Premium Cães 15kg", produtoRecuperado.getDescricao());
        assertEquals("Ração", produtoRecuperado.getCategoria().getNome());
        assertEquals(Status.ATIVO, produtoRecuperado.getStatus());
    }

    @Test
    void deveRegistrarTodosOsChangeSetsDoCurso() {
        Integer quantidade = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM databasechangelog",
                Integer.class);

        assertEquals(17, quantidade);
    }

    @Test
    @Transactional
    void bancoDeveImpedirCodigoDeBarrasDuplicado() {
        Long categoriaId = inserirCategoriaDiretamente("Grupo para unicidade");

        inserirProdutoDiretamente(categoriaId, "CODIGO-REPETIDO", "Primeiro produto", "1.000", "10.00");

        assertThrows(
                DataIntegrityViolationException.class,
                () -> inserirProdutoDiretamente(
                        categoriaId,
                        "CODIGO-REPETIDO",
                        "Segundo produto",
                        "1.000",
                        "20.00"));
    }

    @Test
    @Transactional
    void bancoDeveImpedirSaldoNegativo() {
        Long categoriaId = inserirCategoriaDiretamente("Grupo para saldo");

        assertThrows(
                DataIntegrityViolationException.class,
                () -> inserirProdutoDiretamente(
                        categoriaId,
                        "CODIGO-SALDO-NEGATIVO",
                        "Produto inválido",
                        "-1.000",
                        "10.00"));
    }

    private Long inserirCategoriaDiretamente(String nome) {
        return jdbcTemplate.queryForObject(
                """
                INSERT INTO categoria_produto (nome, status)
                VALUES (?, 'ATIVO')
                RETURNING id
                """,
                Long.class,
                nome);
    }

    private void inserirProdutoDiretamente(
            Long categoriaId,
            String codigoBarras,
            String descricao,
            String saldo,
            String valor) {
        jdbcTemplate.update(
                """
                INSERT INTO produto (
                    codigo_barras,
                    descricao,
                    saldo_estoque,
                    valor_unitario,
                    estoque_minimo,
                    data_cadastro,
                    status,
                    categoria_produto_id
                )
                VALUES (?, ?, CAST(? AS NUMERIC), CAST(? AS NUMERIC), 0, DATE '2026-03-10', 'ATIVO', ?)
                """,
                codigoBarras,
                descricao,
                saldo,
                valor,
                categoriaId);
    }
}
