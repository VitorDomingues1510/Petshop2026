package com.curso.petshop.api.mapper;

import com.curso.petshop.api.dto.FornecedorRequest;
import com.curso.petshop.api.dto.FornecedorResponse;
import com.curso.petshop.domain.Fornecedor;
import org.springframework.stereotype.Component;

@Component
public class FornecedorMapper {

    public Fornecedor toEntity(FornecedorRequest request) {
        return new Fornecedor(request.razaoSocial(), request.cnpj());
    }

    public FornecedorResponse toResponse(Fornecedor fornecedor) {
        return new FornecedorResponse(
                fornecedor.getId(),
                fornecedor.getRazaoSocial(),
                fornecedor.getCnpj(),
                fornecedor.getStatus());
    }
}
