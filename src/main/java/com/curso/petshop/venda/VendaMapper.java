package com.curso.petshop.venda;

import org.springframework.stereotype.Component;

@Component
public class VendaMapper {

    public VendaResponse toResponse(Venda venda) {
        return new VendaResponse(
                venda.getId(),
                venda.getCliente().getId(),
                venda.getCliente().getNome(),
                venda.getDataVenda(),
                venda.getStatus(),
                venda.getItens().stream().map(this::toResponse).toList(),
                venda.calcularValorTotal());
    }

    private ItemVendaResponse toResponse(ItemVenda item) {
        return new ItemVendaResponse(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getDescricao(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.calcularValorTotal());
    }
}
