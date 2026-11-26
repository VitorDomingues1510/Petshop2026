package com.curso.petshop.cliente;

import com.curso.petshop.comum.Status;

public record ClienteResponse(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        Status status) {
}
