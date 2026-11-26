package com.curso.petshop.venda;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    boolean existsByClienteId(Long clienteId);

    boolean existsByItensProdutoId(Long produtoId);

    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto"})
    @Query("select v from Venda v order by v.id")
    List<Venda> buscarTodasComItens();

    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto"})
    @Query("select v from Venda v where v.id = :id")
    Optional<Venda> buscarPorIdComItens(@Param("id") Long id);
}
