package com.api.controller;

import com.api.config.validacao.ResourceNotFoundException;
import com.api.controller.dto.ComentarioDTO;
import com.api.controller.form.ComentarioForm;
import com.api.model.Comentario;
import com.api.model.Filme;
import com.api.model.Perfil;
import com.api.model.Usuario;
import com.api.repository.ComentarioRepository;
import com.api.repository.FilmeRepository;
import com.api.repository.UsuarioRepository;
import com.api.service.ComentarioService;
import com.api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comentarios")
@Tag(name = "Comentários", description = "Fornece recursos para manipulação dos comentários")
public class ComentarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    FilmeRepository filmeRepository;

    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ComentarioService comentarioService;

    @Operation(summary = "Retorna as informações de um comentário a partir do seu ID")
    @GetMapping("/{idComentario}")
    public ResponseEntity<?> exibeComentario (@PathVariable Long idComentario) throws IllegalArgumentException{
        Comentario comentario = comentarioRepository.findById(idComentario).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado comentário com id = " + idComentario));
         return new ResponseEntity<>(new ComentarioDTO(comentario), HttpStatus.OK);
    }

    @Operation(summary = "Adiciona um novo comentário")
    @PostMapping("/novo")
    @CacheEvict(value = "listaDeComentarios", allEntries = true)
    public ResponseEntity<?> adicionarComentario (@RequestBody ComentarioForm comentarioForm, UriComponentsBuilder uriBuilder){
        Usuario usuarioTeste = usuarioRepository.findById(comentarioForm.getUsuarioId()).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com id = " + comentarioForm.getUsuarioId()));
        Filme filmeTeste = filmeRepository.findById(comentarioForm.getFilmeId()).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado filme com id = " + comentarioForm.getFilmeId()));
        if (!usuarioService.verificaPerfil(usuarioTeste,List.of(Perfil.LEITOR))){
            Comentario c = new Comentario(comentarioForm.getTexto(), usuarioTeste, filmeTeste);
            comentarioRepository.save(c);
            return ResponseEntity.created(uriBuilder.path("/comentarios/{id}").buildAndExpand(c.getId()).toUri()).body(new ComentarioDTO(c));
        } else return ResponseEntity.badRequest().body("Usuário não autorizado");
    }

    @Operation(summary = "Adiciona uma resposta a um comentário")
    @PostMapping("/{idComentario}/resposta")
    @CacheEvict(value = "listaDeComentarios", allEntries = true)
    public ResponseEntity<?> adicionarResposta (@PathVariable Long idComentario, @RequestBody ComentarioForm comentarioForm, UriComponentsBuilder uriBuilder){
        Usuario usuarioTeste = usuarioRepository.findById(comentarioForm.getUsuarioId()).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com id = " + comentarioForm.getUsuarioId()));
        Filme filmeTeste = filmeRepository.findById(comentarioForm.getFilmeId()).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado filme com id = " + comentarioForm.getFilmeId()));
        Comentario comentarioTeste = comentarioRepository.findById(idComentario).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado comentário com id = " + idComentario));
        if (!usuarioService.verificaPerfil(usuarioTeste,List.of(Perfil.LEITOR))){
           Comentario novaReposta = comentarioService.criaResposta(comentarioTeste, comentarioForm, usuarioTeste, filmeTeste);
            return ResponseEntity.created(uriBuilder.path("/comentarios/{id}").buildAndExpand(novaReposta.getId()).toUri()).body(new ComentarioDTO(novaReposta));
        } else return ResponseEntity.badRequest().body("Usuário não autorizado");
    }

    @Operation(summary = "Lista os comentários de um filme")
    @GetMapping("/{idFilme}/listar")
    @Cacheable(value = "listaDeComentarios")
    public ResponseEntity<?> listaComentarios (@PathVariable String idFilme){
        Filme filmeTeste = filmeRepository.findById(idFilme).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado filme com id = " + idFilme));
        List<ComentarioDTO> comentarios = comentarioRepository.findAllByFilmeId(idFilme);
        List<ComentarioDTO> comentariosFiltrados = comentarios.stream().filter(c -> c.getRespostaAoComentario() == null).collect(Collectors.toList());
        comentariosFiltrados.forEach(comentario -> comentario.setRespostas(comentarioRepository.retornaRespostasAoComentario(comentario.getId())));
        return ResponseEntity.ok().body(comentariosFiltrados);
    }

    @Operation(summary = "Cita um comentário - retorna os seus dados")
    @GetMapping("/{idComentario}/citar/{idUsuario}")
    public ResponseEntity<?> citaComentario (@PathVariable Long idComentario, @PathVariable Long idUsuario){
        Usuario usuarioTeste = usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com id = " + idUsuario));
        Comentario comentarioTeste = comentarioRepository.findById(idComentario).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado comentário com id = " + idComentario));
        if(usuarioService.verificaPerfil(usuarioTeste,List.of(Perfil.AVANÇADO, Perfil.MODERADOR))) return ResponseEntity.ok().body(new ComentarioDTO(comentarioTeste));
        else return ResponseEntity.badRequest().body("Usuário não autorizado");
    }

    @Operation(summary = "Curte um comentário")
    @PutMapping("/{idComentario}/curtir/{idUsuario}")
    @Transactional
    public ResponseEntity<?> curtirComentario (@PathVariable Long idComentario, @PathVariable Long idUsuario){
        Usuario usuarioTeste = usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com id = " + idUsuario));
        Comentario comentarioTeste = comentarioRepository.findById(idComentario).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado comentário com id = " + idComentario));
        if(usuarioService.verificaPerfil(usuarioTeste,List.of(Perfil.AVANÇADO, Perfil.MODERADOR))){
           if(comentarioRepository.existeCurtida(usuarioTeste.getId(), comentarioTeste.getId()) > 0) return ResponseEntity.badRequest().body("Curtida já existe");
           else {
                comentarioTeste.setQtdCurtidas(comentarioTeste.getQtdCurtidas() + 1);
                comentarioTeste.getCurtidas().add(usuarioTeste);
                return ResponseEntity.ok().body(new ComentarioDTO(comentarioTeste));
           }
        } else return ResponseEntity.badRequest().body("Usuário não autorizado");
    }

    @Operation(summary = "Descurte um comentário")
    @PutMapping("/{idComentario}/descurtir/{idUsuario}")
    @Transactional
    public ResponseEntity<?> descurtirComentario (@PathVariable Long idComentario, @PathVariable Long idUsuario){
        Usuario usuarioTeste = usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com id = " + idUsuario));
        Comentario comentarioTeste = comentarioRepository.findById(idComentario).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado comentário com id = " + idComentario));
        if(usuarioService.verificaPerfil(usuarioTeste,List.of(Perfil.AVANÇADO, Perfil.MODERADOR))){
            if(comentarioRepository.existeDescurtida(usuarioTeste.getId(), comentarioTeste.getId()) > 0) return ResponseEntity.badRequest().body("Descurtida já existe");
            else {
                comentarioTeste.setQtdDescurtidas(comentarioTeste.getQtdDescurtidas() + 1);
                comentarioTeste.getDescurtidas().add(usuarioTeste);
                return ResponseEntity.ok().body(new ComentarioDTO(comentarioTeste));
            }
        } else return ResponseEntity.badRequest().body("Usuário não autorizado");
    }

    @Operation(summary = "Exclui um comentário")
    @DeleteMapping("/{idComentario}/excluir/{idUsuario}")
    public ResponseEntity<?> excluirComentario(@PathVariable Long idComentario, @PathVariable Long idUsuario){
        Usuario usuarioTeste = usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com id = " + idUsuario));
        Comentario comentarioTeste = comentarioRepository.findById(idComentario).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado comentário com id = " + idComentario));
        if(usuarioService.verificaPerfil(usuarioTeste,List.of(Perfil.MODERADOR))){
            comentarioRepository.deleteById(idComentario);
            return ResponseEntity.ok().body("Removido com sucesso");
        }else return ResponseEntity.badRequest().body("Usuário não autorizado");
    }

    @Operation(summary = "Marca um comentário como repetido")
    @PutMapping("/{idComentario}/repetido/{idUsuario}")
    @Transactional
    public ResponseEntity<?> comentarioRepetido (@PathVariable Long idComentario, @PathVariable Long idUsuario){
        Usuario usuarioTeste = usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com id = " + idUsuario));
        Comentario comentarioTeste = comentarioRepository.findById(idComentario).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado comentário com id = " + idComentario));
        if(usuarioService.verificaPerfil(usuarioTeste,List.of(Perfil.MODERADOR))){
            comentarioTeste.setRepetido(true);
            return ResponseEntity.ok().body("Comentário marcado como repetido");
        } else return ResponseEntity.badRequest().body("Usuário não autorizado");
    }
}
