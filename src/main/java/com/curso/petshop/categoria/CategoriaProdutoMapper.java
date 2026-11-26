package com.curso.petshop.categoria;

import org.springframework.stereotype.Component;

@Component
public class CategoriaProdutoMapper {

    public CategoriaProdutoResponse toResponse(CategoriaProduto categoria) {
        return new CategoriaProdutoResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getStatus());
    }
}
