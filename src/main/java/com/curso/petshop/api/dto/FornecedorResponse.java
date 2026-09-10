package com.curso.petshop.api.dto;

import com.curso.petshop.domain.Status;

public record FornecedorResponse(
        Long id,
        String razaoSocial,
        String cnpj,
        Status status) {
}
