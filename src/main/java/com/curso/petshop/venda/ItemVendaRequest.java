package com.curso.petshop.venda;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemVendaRequest(
        @NotNull(message = "Produto é obrigatório")
        @Positive(message = "Identificador do produto deve ser positivo")
        Long produtoId,

        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade deve ser maior que zero")
        BigDecimal quantidade) {
}
