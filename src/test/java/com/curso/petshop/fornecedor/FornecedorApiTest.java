package com.curso.petshop.fornecedor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FornecedorApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Test
    void deveAlterarFornecedor() throws Exception {
        Fornecedor fornecedor = fornecedorRepository.save(new Fornecedor(
                "Fornecedor antigo",
                "33333333000193"));

        mockMvc.perform(put("/api/fornecedores/{id}", fornecedor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "razaoSocial": "Fornecedor novo",
                                  "cnpj": "33333333000193"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.razaoSocial").value("Fornecedor novo"));
    }

    @Test
    void deveExcluirFornecedor() throws Exception {
        Fornecedor fornecedor = fornecedorRepository.save(new Fornecedor(
                "Fornecedor temporário",
                "44444444000194"));

        mockMvc.perform(delete("/api/fornecedores/{id}", fornecedor.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/fornecedores/{id}", fornecedor.getId()))
                .andExpect(status().isNotFound());
    }
}
