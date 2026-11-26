package com.curso.petshop.categoria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    Optional<CategoriaProduto> findByNomeIgnoreCase(String nome);
}
