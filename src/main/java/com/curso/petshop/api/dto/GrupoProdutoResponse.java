package com.curso.petshop.api.dto;

import com.curso.petshop.domain.Status;

public record GrupoProdutoResponse(
        Long id,
        String nome,
        Status status) {
}
