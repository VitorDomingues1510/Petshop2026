package com.curso.petshop.categoria;

import com.curso.petshop.produto.Produto;
import com.curso.petshop.produto.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CategoriaProdutoApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaProdutoRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveAlterarCategoria() throws Exception {
        CategoriaProduto categoria = categoriaRepository.save(new CategoriaProduto("Brinquedo"));

        mockMvc.perform(put("/api/categorias-produtos/{id}", categoria.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Brinquedos\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Brinquedos"));
    }

    @Test
    void deveExcluirCategoria() throws Exception {
        CategoriaProduto categoria = categoriaRepository.save(new CategoriaProduto("Categoria temporária"));

        mockMvc.perform(delete("/api/categorias-produtos/{id}", categoria.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/categorias-produtos/{id}", categoria.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar409AoExcluirCategoriaComProdutos() throws Exception {
        CategoriaProduto categoria = categoriaRepository.save(new CategoriaProduto("Categoria com produto"));
        Produto produto = new Produto(
                "TESTE-CATEGORIA-409",
                "Produto da categoria",
                new BigDecimal("1.000"),
                new BigDecimal("10.00"),
                LocalDate.of(2026, 9, 1));
        categoria.adicionarProduto(produto);
        produtoRepository.save(produto);

        mockMvc.perform(delete("/api/categorias-produtos/{id}", categoria.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(
                        "Não é possível excluir: a categoria possui produtos vinculados"));
    }

    @Test
    void deveRetornar404AoAlterarCategoriaInexistente() throws Exception {
        mockMvc.perform(put("/api/categorias-produtos/{id}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Inexistente\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAlterarStatusDaCategoria() throws Exception {
        CategoriaProduto categoria = categoriaRepository.save(new CategoriaProduto("Acessórios"));

        mockMvc.perform(put("/api/categorias-produtos/{id}/status", categoria.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"INATIVO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INATIVO"));
    }
}
