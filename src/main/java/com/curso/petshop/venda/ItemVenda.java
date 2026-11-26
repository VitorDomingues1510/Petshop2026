package com.curso.petshop.venda;

import com.curso.petshop.produto.Produto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Entity
@Table(name = "item_venda")
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "venda_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_venda_venda"))
    private Venda venda;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "produto_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_venda_produto"))
    private Produto produto;

    @Column(nullable = false, precision = 18, scale = 3)
    private BigDecimal quantidade;

    @Column(name = "preco_unitario", nullable = false, precision = 18, scale = 2)
    private BigDecimal precoUnitario;

    protected ItemVenda() {
    }

    ItemVenda(Venda venda, Produto produto, BigDecimal quantidade) {
        this.venda = Objects.requireNonNull(venda, "Venda é obrigatória");
        this.produto = Objects.requireNonNull(produto, "Produto é obrigatório");
        Objects.requireNonNull(quantidade, "Quantidade é obrigatória");
        if (quantidade.signum() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        this.quantidade = quantidade;
        this.precoUnitario = produto.getValorUnitario();
    }

    public BigDecimal calcularValorTotal() {
        return quantidade
                .multiply(precoUnitario)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
}
