package com.api.controller.dto;

import com.api.model.Comentario;
import lombok.Data;

@Data
public class RespostaDTO {
    private Long id;
    private String texto;
    private int curtidas;
    private int descurtidas;
    private boolean repetido;
    private Long idUsuario;

    public RespostaDTO(Comentario c){
        this.id = c.getId();
        this.texto = c.getTexto();
        this.curtidas = c.getQtdCurtidas();
        this.descurtidas = c.getQtdDescurtidas();
        this.repetido = c.getRepetido();
        this.idUsuario = c.getUsuario().getId();
    }
}



