package com.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter @Setter
@Entity
@Table(name = "comentarios")
@NoArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String texto;

    @Column(nullable = false)
    private int qtdCurtidas;

    @Column(nullable = false)
    private int qtdDescurtidas;

    @Column(nullable = false)
    private Boolean repetido;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp dataCriacao;

    @ManyToOne
    @JoinColumn(name = "resposta_ao_comentario")
    private Comentario respostaAoComentario;

    @JsonIgnore
    @OneToMany(mappedBy = "respostaAoComentario",cascade = CascadeType.ALL)
    private List<Comentario> respostas;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "filme_id", nullable = false)
    private Filme filme;

    @ManyToMany
    @JoinTable(
            name = "curtidas",
            joinColumns = @JoinColumn(name = "comentario_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> curtidas;

    @ManyToMany
    @JoinTable(
            name = "descurtidas",
            joinColumns = @JoinColumn(name = "comentario_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> descurtidas;



    public Comentario(String texto, Usuario usuario, Filme filme){
        this.texto = texto;
        this.usuario = usuario;
        this.filme = filme;
        this.qtdCurtidas = 0;
        this.qtdDescurtidas = 0;
        this.repetido = false;
        this.respostaAoComentario = null;
        this.respostas= new ArrayList<Comentario>();

    }

    public void adicionaResposta(Comentario c){
        respostas.add(c);
    }
    public void setRespostas(List<Comentario> respostas){this.respostas=respostas;}
    public List<Comentario> getRespostas(){return respostas;}


}
