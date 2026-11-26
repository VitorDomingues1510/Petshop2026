package com.curso.petshop.venda;

import com.curso.petshop.cliente.Cliente;
import com.curso.petshop.comum.ExclusaoNaoPermitidaException;
import com.curso.petshop.comum.RecursoNaoEncontradoException;
import com.curso.petshop.produto.Produto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "venda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "cliente_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_venda_cliente"))
    private Cliente cliente;

    @Column(name = "data_venda", nullable = false)
    private LocalDate dataVenda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VendaStatus status;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<ItemVenda> itens = new ArrayList<>();

    protected Venda() {
    }

    public Venda(Cliente cliente, LocalDate dataVenda) {
        this.cliente = Objects.requireNonNull(cliente, "Cliente é obrigatório");
        this.dataVenda = Objects.requireNonNull(dataVenda, "Data da venda é obrigatória");
        this.status = VendaStatus.ABERTA;
    }

    public void adicionarItem(Produto produto, BigDecimal quantidade) {
        validarAberta();
        itens.add(new ItemVenda(this, produto, quantidade));
    }

    public void removerItem(Long itemId) {
        validarAberta();
        boolean removido = itens.removeIf(item -> item.getId().equals(itemId));
        if (!removido) {
            throw new RecursoNaoEncontradoException("Item não encontrado nesta venda");
        }
    }

    public void finalizar() {
        validarAberta();
        if (itens.isEmpty()) {
            throw new IllegalStateException("A venda não possui itens");
        }
        itens.forEach(item -> item.getProduto().retirarEstoque(item.getQuantidade()));
        this.status = VendaStatus.FINALIZADA;
    }

    public void cancelar() {
        validarAberta();
        this.status = VendaStatus.CANCELADA;
    }

    public void validarExclusao() {
        if (status == VendaStatus.FINALIZADA) {
            throw new ExclusaoNaoPermitidaException("Não é possível excluir: a venda está finalizada");
        }
    }

    public BigDecimal calcularValorTotal() {
        return itens.stream()
                .map(ItemVenda::calcularValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public VendaStatus getStatus() {
        return status;
    }

    public List<ItemVenda> getItens() {
        return List.copyOf(itens);
    }

    private void validarAberta() {
        if (status != VendaStatus.ABERTA) {
            throw new IllegalStateException("Somente vendas abertas podem ser alteradas");
        }
    }
}
