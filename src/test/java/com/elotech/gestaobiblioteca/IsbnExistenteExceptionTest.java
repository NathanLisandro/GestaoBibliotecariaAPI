package com.elotech.gestaobiblioteca;

import com.elotech.gestaobiblioteca.Exeptions.IsbnExistenteException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IsbnExistenteExceptionTest {

    @Test
    void testIsbnExistenteExceptionMessage() {
        String mensagem = "ISBN já existe";
        IsbnExistenteException exception = new IsbnExistenteException(mensagem);
        assertEquals(mensagem, exception.getMessage());
    }
}
