package com.api.model;

import com.api.proxy.InformacoesFilme;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "filmes" )
@NoArgsConstructor
public class Filme {

    @Id
    @Column(nullable = false, length = 20)
    private String id;

    @Column(nullable = false)
    private String titulo;

    @Column
    private String ano;

    @Column
    private String genero;

    @Column
    private String diretor;

    @Column
    private String atores;

    @Column
    private String sinopse;

    @Column
    private String premiacoes;

    @Column
    private String idioma;

    @Column
    private String duracao;

    @Column
    private String dataLancamento;

    @Column(nullable = false)
    private Double mediaNotas;

    @Column(nullable = false)
    private int qtdNotas;

    @JsonIgnore
    @OneToMany(mappedBy = "filme", cascade = CascadeType.ALL)
    private List<Comentario> comentarios;

    @JsonIgnore
    @OneToMany(mappedBy = "filme", cascade = CascadeType.ALL)
    private List<Nota> notas;

    @JsonIgnore
    @Transient
    private InformacoesFilme dados;

    public Filme(InformacoesFilme dados){
        this.mediaNotas = 0.0;
        this.qtdNotas = 0;
        this.id = dados.getImdbID();
        this.titulo = dados.getTitle();
        this.ano = dados.getYear();
        this.atores = dados.getActors();
        this.diretor = dados.getDirector();
        this.genero = dados.getGenre();
        this.sinopse = dados.getPlot();
        this.premiacoes = dados.getAwards();
        this.idioma = dados.getLanguage();
        this.duracao = dados.getRuntime();
        this.dataLancamento = dados.getReleased();
        this.dados = dados;
    }
}
