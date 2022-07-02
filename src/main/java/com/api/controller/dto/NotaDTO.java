package com.api.controller.dto;

import com.api.model.Nota;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class NotaDTO {
    private Long id;
    private int valor;
    private Timestamp dataCriacao;
    private Long usuarioId;
    private String filmeId;

    public NotaDTO(Nota n){
        this.id = n.getId();
        this.valor = n.getValor();
        this.dataCriacao = n.getDataCriacao();
        this.usuarioId = n.getUsuario().getId();
        this.filmeId = n.getFilme().getId();
    }

}
