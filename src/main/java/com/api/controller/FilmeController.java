package com.api.controller;

import com.api.config.validacao.ResourceNotFoundException;
import com.api.controller.dto.NotaDTO;
import com.api.model.Filme;
import com.api.model.Nota;
import com.api.model.Usuario;
import com.api.proxy.InformacoesFilme;
import com.api.proxy.OMDbFeign;
import com.api.proxy.ResultadoBusca;
import com.api.repository.FilmeRepository;
import com.api.repository.NotaRepository;
import com.api.repository.UsuarioRepository;
import com.api.service.FilmeService;
import com.api.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/filmes")
@Tag(name = "Filmes", description = "Fornece recursos para manipulação dos filmes")
public class FilmeController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    FilmeRepository filmeRepository;

    @Autowired
    NotaRepository notaRepository;

    @Autowired
    OMDbFeign omDbFeign;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    FilmeService filmeService;

    @GetMapping("/buscaPorTitulo")
    public ResponseEntity<?> buscarFilme(@RequestParam String titulo){
        ResultadoBusca resposta = omDbFeign.buscaFilme(titulo);
        return resposta.getResponse() ? ResponseEntity.ok().body(resposta.getResultList()) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> escolherFilme(@PathVariable String id) throws  IllegalArgumentException {
        InformacoesFilme resposta = omDbFeign.selecionaFilme(id);
        if (resposta.getResponse()) return filmeService.verificaFilme(resposta);//filme encontrado na API externa
        else return ResponseEntity.notFound().build(); //filme não encontrado na API externa
    }

    // ID USER VIA SESSAO
    @PostMapping("/{idFilme}/avaliar/{idUsuario}")
    public ResponseEntity<?> adicionarNota (@PathVariable Long idUsuario, @PathVariable String idFilme,  @RequestParam int valorNota){
        Usuario usuarioTeste = usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com id = " + idUsuario));
        Filme filmeTeste = filmeRepository.findById(idFilme).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado filme com id = " + idFilme));
        if(notaRepository.findByUsuarioIdAndFilmeId(idUsuario, idFilme) == null) { //se a nota ainda não existe no BD
            Nota n = new Nota(Integer.valueOf(valorNota), usuarioTeste, filmeTeste);
            notaRepository.save(n);
            filmeService.recebeNota(filmeTeste);
            usuarioService.adicionaPonto(idUsuario);
            return ResponseEntity.ok().body(new NotaDTO(n));
        } else return ResponseEntity.badRequest().build(); //a nota já existe
    }
}
