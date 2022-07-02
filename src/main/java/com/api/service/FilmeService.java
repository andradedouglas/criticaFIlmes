package com.api.service;

import com.api.controller.dto.FilmeDTO;
import com.api.model.Filme;
import com.api.proxy.InformacoesFilme;
import com.api.repository.FilmeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FilmeService {

    @Autowired
    FilmeRepository filmeRepository;

    public void recebeNota (Filme f){
        f.setQtdNotas(f.getQtdNotas() + 1);
        System.out.println("qtd de avaliações: " + f.getQtdNotas());
        f.setMediaNotas(f.getNotas().stream().map(a -> a.getValor()).mapToDouble(Double::valueOf).sum() / f.getQtdNotas());
        System.out.println("media de avaliação: " + f.getMediaNotas());
        filmeRepository.save(f);
    }

    public ResponseEntity<?> verificaFilme(InformacoesFilme resposta){
        Optional<Filme> filmeJaAdicionado = filmeRepository.findById(resposta.getImdbID());
        if (filmeJaAdicionado.isPresent())  //Filme já cadastrado no BD
            return ResponseEntity.ok().body(new FilmeDTO(filmeJaAdicionado.get()));
        else { //filme ainda não cadastrado no BD
            Filme novoFilme = new Filme(resposta);
            filmeRepository.save(novoFilme);
            return ResponseEntity.ok().body(new FilmeDTO(novoFilme));
        }
    }
}
