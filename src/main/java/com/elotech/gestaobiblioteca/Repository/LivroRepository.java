package com.elotech.gestaobiblioteca.Repository;

import com.elotech.gestaobiblioteca.model.Livros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface LivroRepository extends JpaRepository<Livros, Long> {
    List<Livros> findByCategoriaIn(Set<String> categorias);
    boolean existsByIsbn(String isbn);


}
