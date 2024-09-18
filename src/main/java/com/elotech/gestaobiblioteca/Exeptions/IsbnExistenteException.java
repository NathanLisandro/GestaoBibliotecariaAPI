package com.elotech.gestaobiblioteca.Exeptions;

public class IsbnExistenteException extends RuntimeException {

    public IsbnExistenteException(String message) {
      super(message);
    }
}
