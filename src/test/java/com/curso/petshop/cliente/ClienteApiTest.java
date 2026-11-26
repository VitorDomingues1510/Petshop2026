package com.curso.petshop.cliente;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ClienteApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void deveCadastrarClienteERetornar201() throws Exception {
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Maria Silva",
                                  "cpf": "11122233344",
                                  "email": "maria@email.com",
                                  "telefone": "11987654321"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.status").value("ATIVO"));
    }

    @Test
    void deveRetornar409ParaCpfDuplicado() throws Exception {
        clienteRepository.save(new Cliente("Maria", "55566677788", "maria@email.com", null));

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Outra Maria",
                                  "cpf": "55566677788",
                                  "email": "outra@email.com"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("CPF já cadastrado"));
    }

    @Test
    void deveAlterarCliente() throws Exception {
        Cliente cliente = clienteRepository.save(new Cliente("João", "99988877766", "joao@email.com", null));

        mockMvc.perform(put("/api/clientes/{id}", cliente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "João Souza",
                                  "cpf": "99988877766",
                                  "email": "joao.souza@email.com",
                                  "telefone": "1133334444"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Souza"))
                .andExpect(jsonPath("$.telefone").value("1133334444"));
    }

    @Test
    void deveExcluirCliente() throws Exception {
        Cliente cliente = clienteRepository.save(new Cliente("Ana", "12312312312", "ana@email.com", null));

        mockMvc.perform(delete("/api/clientes/{id}", cliente.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/clientes/{id}", cliente.getId()))
                .andExpect(status().isNotFound());
    }
}
