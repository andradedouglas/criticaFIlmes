package com.api.controller.dto;

import com.api.model.Perfil;
import com.api.model.Usuario;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private Perfil perfil;
    private int pontos;
    private List<ComentarioDTO> comentarios;
    private List<NotaDTO> notas;
    private List<Long> comentariosCurtidos;
    private List<Long> comentariosNaoCurtidos;

    public UsuarioDTO(Usuario u){
        this.id = u.getId();
        this.nome = u.getNome();
        this.email = u.getEmail();
        this.senha = u.getSenha();
        this.perfil = u.getPerfil();
        this.pontos = u.getPontos();
        try{
            this.comentarios = u.getComentarios().stream().map(ComentarioDTO::new).collect(Collectors.toList());;
            this.notas = u.getNotas().stream().map(NotaDTO::new).collect(Collectors.toList());
            this.comentariosCurtidos =  u.getComentariosCurtidos().stream().map(c -> c.getId()).collect(Collectors.toList());
            this.comentariosNaoCurtidos =  u.getComentariosNaoCurtidos().stream().map(c -> c.getId()).collect(Collectors.toList());
        }catch (NullPointerException e) {System.out.println("NullPointerException thrown");}

    }

}
