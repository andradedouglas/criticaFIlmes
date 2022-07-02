package com.api.controller.dto;

import com.api.model.Filme;
import lombok.Data;

@Data
public class FilmeDTO {

    private String id;
    private String titulo;
    private String ano;
    private String genero;
    private String diretor;
    private String atores;
    private String sinopse;
    private String premiacoes;
    private String idioma;
    private String duracao;
    private String dataLancamento;
    private Double mediaNotas;
    private int qtdNotas;
    //private List<ComentarioInformacoesDTO> comentarios;
    //private List<NotaDTO> notas;

    public FilmeDTO(Filme filme) {
        this.id = filme.getId();
        this.titulo = filme.getTitulo();
        this.ano = filme.getAno();
        this.genero = filme.getGenero();
        this.diretor = filme.getDiretor();
        this.atores = filme.getAtores();
        this.sinopse = filme.getSinopse();
        this.premiacoes = filme.getPremiacoes();
        this.idioma = filme.getIdioma();
        this.duracao = filme.getDuracao();
        this.dataLancamento = filme.getDataLancamento();
        this.mediaNotas = filme.getMediaNotas();
        this.qtdNotas = filme.getQtdNotas();
        //this.comentarios = filme.getComentarios().stream().map(ComentarioInformacoesDTO::new).collect(Collectors.toList());
        //this.notas = filme.getNotas().stream().map(NotaDTO::new).collect(Collectors.toList());
    }

}