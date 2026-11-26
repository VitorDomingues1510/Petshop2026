package com.curso.petshop.venda;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record VendaResponse(
        Long id,
        Long clienteId,
        String clienteNome,
        LocalDate dataVenda,
        VendaStatus status,
        List<ItemVendaResponse> itens,
        BigDecimal valorTotal) {
}
