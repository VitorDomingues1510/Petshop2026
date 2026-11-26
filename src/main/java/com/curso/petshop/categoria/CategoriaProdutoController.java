package com.curso.petshop.categoria;

import com.curso.petshop.comum.StatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/{id}")
    public CategoriaProdutoResponse alterar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaProdutoRequest request) {
        return mapper.toResponse(service.alterar(id, request.nome()));
    }

    @PutMapping("/{id}/status")
    public CategoriaProdutoResponse alterarStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusRequest request) {
        return mapper.toResponse(service.alterarStatus(id, request.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
