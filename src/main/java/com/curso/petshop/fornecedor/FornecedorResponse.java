package com.curso.petshop.fornecedor;

import com.curso.petshop.comum.Status;

public record FornecedorResponse(
        Long id,
        String razaoSocial,
        String cnpj,
        Status status) {
}
