package com.curso.petshop.produto;

import com.curso.petshop.categoria.CategoriaProduto;
import com.curso.petshop.categoria.CategoriaProdutoRepository;
import com.curso.petshop.fornecedor.Fornecedor;
import com.curso.petshop.fornecedor.FornecedorRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProdutoApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaProdutoRepository categoriaRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveCadastrarProdutoERetornar201() throws Exception {
        CategoriaProduto categoria = categoriaRepository.save(new CategoriaProduto("Ração"));
        Fornecedor fornecedor = fornecedorRepository.save(new Fornecedor(
                "Fornecedor API",
                "22222222000192"));

        String json = """
                {
                  "codigoBarras": "API-001",
                  "descricao": "Produto criado pela API",
                  "saldoEstoque": 10.000,
                  "valorUnitario": 49.90,
                  "estoqueMinimo": 2.000,
                  "categoriaId": %d,
                  "fornecedorId": %d
                }
                """.formatted(categoria.getId(), fornecedor.getId());

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.codigoBarras").value("API-001"))
                .andExpect(jsonPath("$.categoriaNome").value("Ração"))
                .andExpect(jsonPath("$.fornecedorRazaoSocial").value("Fornecedor API"));
    }

    @Test
    void deveRetornar400ComErrosPorCampo() throws Exception {
        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Um ou mais campos são inválidos"))
                .andExpect(jsonPath("$.fields.codigoBarras").exists())
                .andExpect(jsonPath("$.fields.categoriaId").exists());
    }

    @Test
    void deveRetornar404ParaProdutoInexistente() throws Exception {
        mockMvc.perform(get("/api/produtos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Produto não encontrado"))
                .andExpect(jsonPath("$.path").value("/api/produtos/" + Long.MAX_VALUE));
    }

    @Test
    void deveAlterarProduto() throws Exception {
        CategoriaProduto categoria = categoriaRepository.save(new CategoriaProduto("Higiene"));
        Produto produto = salvarProduto(categoria, "API-ALTERAR");

        String json = """
                {
                  "codigoBarras": "API-ALTERAR",
                  "descricao": "Shampoo Neutro 1L",
                  "saldoEstoque": 8.000,
                  "valorUnitario": 45.00,
                  "estoqueMinimo": 1.000,
                  "categoriaId": %d
                }
                """.formatted(categoria.getId());

        mockMvc.perform(put("/api/produtos/{id}", produto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Shampoo Neutro 1L"))
                .andExpect(jsonPath("$.valorUnitario").value(45.00));
    }

    @Test
    void deveExcluirProduto() throws Exception {
        CategoriaProduto categoria = categoriaRepository.save(new CategoriaProduto("Farmácia"));
        Produto produto = salvarProduto(categoria, "API-EXCLUIR");

        mockMvc.perform(delete("/api/produtos/{id}", produto.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/produtos/{id}", produto.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAlterarStatusDoProduto() throws Exception {
        CategoriaProduto categoria = categoriaRepository.save(new CategoriaProduto("Petiscos"));
        Produto produto = salvarProduto(categoria, "API-STATUS");

        mockMvc.perform(put("/api/produtos/{id}/status", produto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"INATIVO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INATIVO"));
    }

    @Test
    void deveRegistrarEntradaDeEstoque() throws Exception {
        CategoriaProduto categoria = categoriaRepository.save(new CategoriaProduto("Areia"));
        Produto produto = salvarProduto(categoria, "API-ENTRADA");

        mockMvc.perform(post("/api/produtos/{id}/estoque/entrada", produto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantidade\": 5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoEstoque").value(20.000));
    }

    @Test
    void deveRetornar400QuandoSaidaMaiorQueOSaldo() throws Exception {
        CategoriaProduto categoria = categoriaRepository.save(new CategoriaProduto("Coleiras"));
        Produto produto = salvarProduto(categoria, "API-SAIDA");

        mockMvc.perform(post("/api/produtos/{id}/estoque/saida", produto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantidade\": 100}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo de estoque insuficiente"));
    }

    @Test
    void deveRetornar400ParaIdInvalido() throws Exception {
        mockMvc.perform(delete("/api/produtos/{id}", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Valor inválido para o parâmetro id"));
    }

    private Produto salvarProduto(CategoriaProduto categoria, String codigoBarras) {
        Produto produto = new Produto(
                codigoBarras,
                "Shampoo Neutro 500ml",
                new BigDecimal("15.000"),
                new BigDecimal("32.50"),
                LocalDate.of(2026, 9, 1));
        categoria.adicionarProduto(produto);
        return produtoRepository.save(produto);
    }
}
