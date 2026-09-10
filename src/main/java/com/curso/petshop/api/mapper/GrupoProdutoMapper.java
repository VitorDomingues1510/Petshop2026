package com.curso.petshop.api.mapper;

import com.curso.petshop.api.dto.GrupoProdutoResponse;
import com.curso.petshop.domain.GrupoProduto;
import org.springframework.stereotype.Component;

@Component
public class GrupoProdutoMapper {

    public GrupoProdutoResponse toResponse(GrupoProduto grupo) {
        return new GrupoProdutoResponse(
                grupo.getId(),
                grupo.getNome(),
                grupo.getStatus());
    }
}
