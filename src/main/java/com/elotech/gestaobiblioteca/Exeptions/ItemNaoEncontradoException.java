package com.elotech.gestaobiblioteca.Exeptions;

public class ItemNaoEncontradoException extends RuntimeException {

    public ItemNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
