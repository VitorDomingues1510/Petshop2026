package com.curso.petshop.produto;

import com.curso.petshop.comum.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByCodigoBarras(String codigoBarras);

    boolean existsByCodigoBarras(String codigoBarras);

    boolean existsByCodigoBarrasAndIdNot(String codigoBarras, Long id);

    List<Produto> findByCategoriaId(Long categoriaId);

    boolean existsByCategoriaId(Long categoriaId);

    boolean existsByFornecedorId(Long fornecedorId);

    List<Produto> findByStatus(Status status);

    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    @Query("select p from Produto p order by p.id")
    List<Produto> buscarTodosComRelacionamentos();

    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    @Query("select p from Produto p where p.id = :id")
    Optional<Produto> buscarPorIdComRelacionamentos(@Param("id") Long id);
}
