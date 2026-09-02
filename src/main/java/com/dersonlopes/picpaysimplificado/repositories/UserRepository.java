package com.dersonlopes.picpaysimplificado.repositories;

import com.dersonlopes.picpaysimplificado.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Interface que herda do Spring Data JPA.
// Ela herda comandos automáticos de salvar, deletar e buscar.
// Adicionamos uma busca customizada por documento para usarmos nas próximas validações.
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByDocument(String document);

}
