package com.api.repository;

import com.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByEmail(String email);
    Usuario findByEmailAndSenha(String email, String senha);
    Boolean existsByEmail(String email);


}
