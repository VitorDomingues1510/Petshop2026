package com.curso.petshop.api.mapper;

import com.curso.petshop.api.dto.CategoriaProdutoResponse;
import com.curso.petshop.domain.CategoriaProduto;
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
