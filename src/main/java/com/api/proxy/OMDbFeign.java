package com.api.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name="OMDbAPI", url="${omdb.url}")
public interface OMDbFeign {

    @GetMapping
    ResultadoBusca buscaFilme (@RequestParam("s") String titulo);

    @GetMapping
    InformacoesFilme selecionaFilme (@RequestParam("i") String id);

}
