package com.elotech.gestaobiblioteca;

import com.elotech.gestaobiblioteca.Exeptions.ItemNaoEncontradoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemNaoEncontradoExceptionTest {

    @Test
    void testItemNaoEncontradoExceptionMessage() {
        String mensagem = "Item não encontrado";
        ItemNaoEncontradoException exception = new ItemNaoEncontradoException(mensagem);
        assertEquals(mensagem, exception.getMessage());
    }
}
