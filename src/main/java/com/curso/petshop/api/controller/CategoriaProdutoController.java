package com.curso.petshop.api.controller;

import com.curso.petshop.api.dto.CategoriaProdutoRequest;
import com.curso.petshop.api.dto.CategoriaProdutoResponse;
import com.curso.petshop.api.mapper.CategoriaProdutoMapper;
import com.curso.petshop.application.CategoriaProdutoService;
import com.curso.petshop.domain.CategoriaProduto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias-produtos")
public class CategoriaProdutoController {

    private final CategoriaProdutoService service;
    private final CategoriaProdutoMapper mapper;

    public CategoriaProdutoController(CategoriaProdutoService service, CategoriaProdutoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CategoriaProdutoResponse> cadastrar(
            @Valid @RequestBody CategoriaProdutoRequest request) {
        CategoriaProduto categoria = service.cadastrar(request.nome());
        URI location = URI.create("/api/categorias-produtos/" + categoria.getId());
        return ResponseEntity.created(location).body(mapper.toResponse(categoria));
    }

    @GetMapping("/{id}")
    public CategoriaProdutoResponse buscarPorId(@PathVariable Long id) {
        return mapper.toResponse(service.buscarPorId(id));
    }

    @GetMapping
    public List<CategoriaProdutoResponse> listar() {
        return service.listar().stream().map(mapper::toResponse).toList();
    }
}
