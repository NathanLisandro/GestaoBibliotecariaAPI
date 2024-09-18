package com.elotech.gestaobiblioteca.Repository;

import com.elotech.gestaobiblioteca.model.Emprestimos;
import com.elotech.gestaobiblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface EmprestimoRepository extends JpaRepository<Emprestimos, Long> {
    List<Emprestimos> findByUsuario(Usuario usuario);
    List<Emprestimos> findByLivroId(Long id);
}
