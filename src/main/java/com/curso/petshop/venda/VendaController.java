package com.curso.petshop.venda;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaService service;
    private final VendaMapper mapper;

    public VendaController(VendaService service, VendaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<VendaResponse> abrir(
            @Valid @RequestBody VendaRequest request) {
        Venda venda = service.abrir(request.clienteId());
        URI location = URI.create("/api/vendas/" + venda.getId());
        return ResponseEntity.created(location).body(mapper.toResponse(venda));
    }

    @GetMapping("/{id}")
    public VendaResponse buscarPorId(@PathVariable Long id) {
        return mapper.toResponse(service.buscarPorId(id));
    }

    @GetMapping
    public List<VendaResponse> listar() {
        return service.listar().stream().map(mapper::toResponse).toList();
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<VendaResponse> adicionarItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemVendaRequest request) {
        Venda venda = service.adicionarItem(id, request.produtoId(), request.quantidade());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(venda));
    }

    @DeleteMapping("/{id}/itens/{itemId}")
    public VendaResponse removerItem(
            @PathVariable Long id,
            @PathVariable Long itemId) {
        return mapper.toResponse(service.removerItem(id, itemId));
    }

    @PostMapping("/{id}/finalizar")
    public VendaResponse finalizar(@PathVariable Long id) {
        return mapper.toResponse(service.finalizar(id));
    }

    @PostMapping("/{id}/cancelar")
    public VendaResponse cancelar(@PathVariable Long id) {
        return mapper.toResponse(service.cancelar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
