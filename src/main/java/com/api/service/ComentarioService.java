package com.api.service;

import com.api.controller.form.ComentarioForm;
import com.api.model.Comentario;
import com.api.model.Filme;
import com.api.model.Usuario;
import com.api.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService {

    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    UsuarioService usuarioService;

    public Comentario criaResposta(Comentario c, ComentarioForm cf, Usuario u, Filme f){
        Comentario novaReposta = new Comentario(cf.getTexto(), u, f);
        novaReposta.setRespostaAoComentario(c);
        comentarioRepository.save(novaReposta);
        c.adicionaResposta(novaReposta);
        comentarioRepository.save(c);
        usuarioService.adicionaPonto(u.getId());
        return novaReposta;
    }
}
