package com.curso.petshop.comum;

public class ExclusaoNaoPermitidaException extends RuntimeException {

    public ExclusaoNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}
