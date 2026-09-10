package com.curso.petshop.api;

import com.curso.petshop.domain.Fornecedor;
import com.curso.petshop.domain.GrupoProduto;
import com.curso.petshop.repository.FornecedorRepository;
import com.curso.petshop.repository.GrupoProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private GrupoProdutoRepository grupoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Test
    void deveCadastrarProdutoERetornar201() throws Exception {
        GrupoProduto grupo = grupoRepository.save(new GrupoProduto("Ração API"));
        Fornecedor fornecedor = fornecedorRepository.save(new Fornecedor(
                "Pet Distribuidora API",
                "22222222000192"));

        String json = """
                {
                  "codigoBarras": "API-001",
                  "descricao": "Ração Golden Adulto 15kg",
                  "saldoEstoque": 10.000,
                  "valorUnitario": 89.90,
                  "estoqueMinimo": 2.000,
                  "grupoId": %d,
                  "fornecedorId": %d
                }
                """.formatted(grupo.getId(), fornecedor.getId());

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.codigoBarras").value("API-001"))
                .andExpect(jsonPath("$.grupoNome").value("Ração API"))
                .andExpect(jsonPath("$.fornecedorRazaoSocial").value("Pet Distribuidora API"));
    }

    @Test
    void deveRetornar400ComErrosPorCampo() throws Exception {
        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Um ou mais campos são inválidos"))
                .andExpect(jsonPath("$.fields.codigoBarras").exists())
                .andExpect(jsonPath("$.fields.grupoId").exists());
    }

    @Test
    void deveRetornar404ParaProdutoInexistente() throws Exception {
        mockMvc.perform(get("/api/produtos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Produto não encontrado"))
                .andExpect(jsonPath("$.path").value("/api/produtos/" + Long.MAX_VALUE));
    }
}
