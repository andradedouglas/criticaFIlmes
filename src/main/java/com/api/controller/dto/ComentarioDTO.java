package com.api.controller.dto;

import com.api.model.Comentario;
import com.api.model.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class ComentarioDTO {
    private Long id;
    private String texto;
    private int curtidas;
    private int descurtidas;
    private boolean repetido;
    private Long idUsuario;
    private Long respostaAoComentario;
    private List<RespostaDTO> respostas;

    public ComentarioDTO(Long id, String texto, int curtidas, int descurtidas, boolean repetido , Usuario usuario, Comentario respostaAoComentario){
        this.id = id;
        this.texto = texto;
        this.curtidas = curtidas;
        this.descurtidas = descurtidas;
        this.repetido = repetido;
        this.idUsuario = usuario.getId();
        if(respostaAoComentario != null)  this.respostaAoComentario = respostaAoComentario.getId();
        else respostaAoComentario = null;

        }
    public ComentarioDTO(Comentario c){
        this.id = c.getId();
        this.texto = c.getTexto();
        this.curtidas = c.getQtdCurtidas();
        this.descurtidas = c.getQtdDescurtidas();
        this.repetido = c.getRepetido();
        this.idUsuario = c.getUsuario().getId();
        if(c.getRespostaAoComentario() == null) this.respostaAoComentario = null;
        else this.respostaAoComentario = c.getRespostaAoComentario().getId();
        setRespostas(c.getRespostas());
        this.respostas = c.getRespostas().stream().map(RespostaDTO::new).collect(Collectors.toList());

    }

    public void setRespostas(List<Comentario> respostas){
        List<RespostaDTO> aux = new ArrayList<>();
        try{
            for (Comentario comentario : respostas)
                aux.add(new RespostaDTO(comentario));
            this.respostas = aux;
        }catch (NullPointerException e) {System.out.println("Comentario nulo");}

    }

}
