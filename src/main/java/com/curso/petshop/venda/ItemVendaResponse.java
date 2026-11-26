package com.curso.petshop.venda;

import java.math.BigDecimal;

public record ItemVendaResponse(
        Long id,
        Long produtoId,
        String produtoDescricao,
        BigDecimal quantidade,
        BigDecimal precoUnitario,
        BigDecimal valorTotal) {
}
