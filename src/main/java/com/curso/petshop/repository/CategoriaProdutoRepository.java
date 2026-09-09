package com.curso.petshop.repository;

import com.curso.petshop.domain.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    Optional<CategoriaProduto> findByNomeIgnoreCase(String nome);
}
