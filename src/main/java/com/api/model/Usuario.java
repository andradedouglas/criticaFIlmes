package com.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;


@Entity
@Getter @Setter
@Table(name = "usuarios" )
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Nome é obrigatório")
    @Column(nullable = false, length = 50)
    private String nome;

    @Email(message = "Email inválido")
    @NotNull(message = "Email é obrigatório")
    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @NotNull(message = "Senha é obrigatória")
    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, length = 15)
    private Perfil perfil;

    @Column
    private int pontos;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comentario> comentarios;

    @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL)
    private List<Nota> notas;

    @ManyToMany(mappedBy = "curtidas", cascade = CascadeType.ALL)
    private Set<Comentario> comentariosCurtidos;

    @ManyToMany(mappedBy = "descurtidas", cascade = CascadeType.ALL)
    private Set<Comentario> comentariosNaoCurtidos;


    public Usuario(String nome, String email, String senha){
        this.nome =nome;
        this.email = email;
        this.senha = senha;
        this.perfil = Perfil.LEITOR;
        this.pontos = 0;
    }


}
