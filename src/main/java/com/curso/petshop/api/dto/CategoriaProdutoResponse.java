package com.curso.petshop.api.dto;

import com.curso.petshop.domain.Status;

public record CategoriaProdutoResponse(
        Long id,
        String nome,
        Status status) {
}
