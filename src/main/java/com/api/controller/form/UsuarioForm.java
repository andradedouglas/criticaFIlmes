package com.api.controller.form;

import com.api.model.Usuario;
import com.api.repository.UsuarioRepository;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UsuarioForm {
    @NotBlank
    private String nome;
    @NotBlank @Email
    private String email;
    @NotBlank
    private String senha;

    public Usuario atualizar(Long id, UsuarioRepository usuarioRepository){
        Usuario u = usuarioRepository.getReferenceById(id);
        u.setNome(this.nome);
        u.setEmail(this.email);
        u.setSenha(this.senha);
        return u;
    }
}

