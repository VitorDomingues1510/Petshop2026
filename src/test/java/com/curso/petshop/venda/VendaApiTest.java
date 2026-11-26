package com.curso.petshop.venda;

import com.curso.petshop.categoria.CategoriaProduto;
import com.curso.petshop.categoria.CategoriaProdutoRepository;
import com.curso.petshop.cliente.Cliente;
import com.curso.petshop.cliente.ClienteRepository;
import com.curso.petshop.produto.Produto;
import com.curso.petshop.produto.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class VendaApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CategoriaProdutoRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    private Cliente cliente;
    private Produto produto;

    @BeforeEach
    void prepararDados() {
        cliente = clienteRepository.save(new Cliente("Cliente Venda", "10120230344", "venda@email.com", null));
        CategoriaProduto categoria = categoriaRepository.save(new CategoriaProduto("Categoria venda"));
        produto = new Produto(
                "TESTE-VENDA-001",
                "Bolinha de borracha",
                new BigDecimal("40.000"),
                new BigDecimal("12.90"),
                LocalDate.of(2026, 9, 1));
        categoria.adicionarProduto(produto);
        produto = produtoRepository.save(produto);
    }

    @Test
    void deveAbrirVenda() throws Exception {
        mockMvc.perform(post("/api/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\": %d}".formatted(cliente.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ABERTA"))
                .andExpect(jsonPath("$.clienteNome").value("Cliente Venda"));
    }

    @Test
    void deveAdicionarItemECalcularValorTotal() throws Exception {
        Venda venda = vendaRepository.save(new Venda(cliente, LocalDate.of(2026, 9, 2)));

        mockMvc.perform(post("/api/vendas/{id}/itens", venda.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"produtoId\": %d, \"quantidade\": 3}".formatted(produto.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itens[0].id").isNumber())
                .andExpect(jsonPath("$.itens[0].precoUnitario").value(12.90))
                .andExpect(jsonPath("$.valorTotal").value(38.70));
    }

    @Test
    void deveFinalizarVendaEBaixarEstoque() throws Exception {
        Venda venda = new Venda(cliente, LocalDate.of(2026, 9, 2));
        venda.adicionarItem(produto, new BigDecimal("4.000"));
        venda = vendaRepository.save(venda);

        mockMvc.perform(post("/api/vendas/{id}/finalizar", venda.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FINALIZADA"));

        assertEquals(0, new BigDecimal("36.000").compareTo(produto.getSaldoEstoque()));
    }

    @Test
    void naoDeveAlterarVendaCancelada() throws Exception {
        Venda venda = new Venda(cliente, LocalDate.of(2026, 9, 2));
        venda.cancelar();
        venda = vendaRepository.save(venda);

        mockMvc.perform(post("/api/vendas/{id}/itens", venda.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"produtoId\": %d, \"quantidade\": 1}".formatted(produto.getId())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Somente vendas abertas podem ser alteradas"));
    }

    @Test
    void deveExcluirVendaAberta() throws Exception {
        Venda venda = vendaRepository.save(new Venda(cliente, LocalDate.of(2026, 9, 2)));

        mockMvc.perform(delete("/api/vendas/{id}", venda.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void naoDeveExcluirVendaFinalizada() throws Exception {
        Venda venda = new Venda(cliente, LocalDate.of(2026, 9, 2));
        venda.adicionarItem(produto, new BigDecimal("1.000"));
        venda.finalizar();
        venda = vendaRepository.save(venda);

        mockMvc.perform(delete("/api/vendas/{id}", venda.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Não é possível excluir: a venda está finalizada"));
    }

    @Test
    void naoDeveExcluirClienteComVendas() throws Exception {
        vendaRepository.save(new Venda(cliente, LocalDate.of(2026, 9, 2)));

        mockMvc.perform(delete("/api/clientes/{id}", cliente.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Não é possível excluir: o cliente possui vendas vinculadas"));
    }

    @Test
    void deveRemoverItemDaVenda() throws Exception {
        Venda venda = new Venda(cliente, LocalDate.of(2026, 9, 2));
        venda.adicionarItem(produto, new BigDecimal("2.000"));
        venda.adicionarItem(produto, new BigDecimal("1.000"));
        venda = vendaRepository.saveAndFlush(venda);
        Long itemId = venda.getItens().get(1).getId();

        mockMvc.perform(delete("/api/vendas/{id}/itens/{itemId}", venda.getId(), itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens.length()").value(1))
                .andExpect(jsonPath("$.valorTotal").value(25.80));
    }

    @Test
    void deveRetornar404ParaItemInexistente() throws Exception {
        Venda venda = vendaRepository.save(new Venda(cliente, LocalDate.of(2026, 9, 2)));

        mockMvc.perform(delete("/api/vendas/{id}/itens/{itemId}", venda.getId(), Long.MAX_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Item não encontrado nesta venda"));
    }
}
