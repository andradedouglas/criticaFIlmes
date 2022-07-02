package com.api.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ComentarioForm {
    @NotBlank
    private Long usuarioId;
    @NotBlank
    private String filmeId;
    @NotBlank
    private String texto;
}
