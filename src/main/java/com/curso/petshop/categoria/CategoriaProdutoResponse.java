package com.curso.petshop.categoria;

import com.curso.petshop.comum.Status;

public record CategoriaProdutoResponse(
        Long id,
        String nome,
        Status status) {
}
