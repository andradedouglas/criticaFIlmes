package com.api.config.validacao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data @AllArgsConstructor
public class MensagemErro {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;
}
