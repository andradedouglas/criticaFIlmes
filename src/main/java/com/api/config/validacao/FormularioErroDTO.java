package com.api.config.validacao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class FormularioErroDTO {
    private String campo;
    private String erro;


}
