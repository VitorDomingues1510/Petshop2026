package com.curso.petshop.cliente;

import com.curso.petshop.comum.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "cliente",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_cliente_cpf",
                columnNames = "cpf"))
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    protected Cliente() {
    }

    public Cliente(String nome, String cpf, String email, String telefone) {
        alterarDados(nome, cpf, email, telefone);
        this.status = Status.ATIVO;
    }

    public void alterarDados(String nome, String cpf, String email, String telefone) {
        this.nome = validarTextoObrigatorio(nome, "Nome é obrigatório");
        this.cpf = validarCpf(cpf);
        this.email = validarTextoObrigatorio(email, "E-mail é obrigatório");
        this.telefone = telefone == null || telefone.isBlank() ? null : telefone.trim();
    }

    public void ativar() {
        this.status = Status.ATIVO;
    }

    public void inativar() {
        this.status = Status.INATIVO;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public Status getStatus() {
        return status;
    }

    private static String validarTextoObrigatorio(String texto, String mensagem) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return texto.trim();
    }

    private static String validarCpf(String cpf) {
        String valor = validarTextoObrigatorio(cpf, "CPF é obrigatório");
        if (!valor.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve possuir 11 dígitos");
        }
        return valor;
    }
}
