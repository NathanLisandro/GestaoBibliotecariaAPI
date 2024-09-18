package com.elotech.gestaobiblioteca;

import com.elotech.gestaobiblioteca.Controller.EmprestimoController;
import com.elotech.gestaobiblioteca.DTO.DadosAtualizacaoEmprestimo;
import com.elotech.gestaobiblioteca.DTO.DadosCadastroEmprestimo;
import com.elotech.gestaobiblioteca.Service.EmprestimoService;
import com.elotech.gestaobiblioteca.model.Emprestimos;
import com.elotech.gestaobiblioteca.model.Livros;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EmprestimoControllerTest {

    @InjectMocks
    private EmprestimoController emprestimoController;

    @Mock
    private EmprestimoService emprestimoService;

    @Test
    void deveListarEmprestimos() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Emprestimos emprestimo = new Emprestimos();
        Page<Emprestimos> page = new PageImpl<>(List.of(emprestimo));
        when(emprestimoService.listarEmprestimos(pageRequest)).thenReturn(page);
        ResponseEntity<Page<Emprestimos>> response = emprestimoController.listar(0, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
        verify(emprestimoService, times(1)).listarEmprestimos(pageRequest);
    }

    @Test
    void deveCriarEmprestimo() {
        DadosCadastroEmprestimo dadosCadastro = new DadosCadastroEmprestimo(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(7), "PENDENTE");
        Emprestimos emprestimo = new Emprestimos();
        when(emprestimoService.criarEmprestimo(dadosCadastro)).thenReturn(emprestimo);
        ResponseEntity<Emprestimos> response = emprestimoController.criarEmprestimo(dadosCadastro);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(emprestimoService, times(1)).criarEmprestimo(dadosCadastro);
    }

    @Test
    void deveAtualizarEmprestimo() {
        Long id = 1L;
        DadosAtualizacaoEmprestimo dadosAtualizacao = new DadosAtualizacaoEmprestimo(1L, 1L, LocalDate.now(), "DEVOLVIDO");
        Emprestimos emprestimo = new Emprestimos();
        when(emprestimoService.atualizarEmprestimo(id, dadosAtualizacao)).thenReturn(emprestimo);
        ResponseEntity<Emprestimos> response = emprestimoController.atualizarEmprestimo(id, dadosAtualizacao);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(emprestimoService, times(1)).atualizarEmprestimo(id, dadosAtualizacao);
    }
}
