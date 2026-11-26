package com.curso.petshop.venda;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VendaRequest(
        @NotNull(message = "Cliente é obrigatório")
        @Positive(message = "Identificador do cliente deve ser positivo")
        Long clienteId) {
}
