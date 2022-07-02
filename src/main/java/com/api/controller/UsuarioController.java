package com.api.controller;

import com.api.config.validacao.ResourceNotFoundException;
import com.api.controller.dto.UsuarioDTO;
import com.api.controller.form.LoginForm;
import com.api.controller.form.UsuarioForm;
import com.api.model.Perfil;
import com.api.model.Usuario;
import com.api.repository.UsuarioRepository;
import com.api.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Fornece recursos para manipulação dos usuários")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginForm loginForm) {
        Usuario u = usuarioRepository.findByEmailAndSenha(loginForm.getEmail(), loginForm.getSenha());
        if(u != null) return ResponseEntity.ok(new UsuarioDTO(u));
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @GetMapping
    public ResponseEntity<?> listaUsuarios () {
        List<Usuario> listaUsuarios = usuarioRepository.findAll();
        if(!listaUsuarios.isEmpty()) return ResponseEntity.ok(listaUsuarios.stream().map(UsuarioDTO::new).collect(Collectors.toList()));
        else return new ResponseEntity<>("Nenhum usuário encontrado",HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> exibeUsuarioPeloId (@PathVariable Long id) throws IllegalArgumentException{
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com id = " + id));
        return new ResponseEntity<>(new UsuarioDTO(u), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> exibeUsuarioPeloEmail (@PathVariable String email){
        Usuario u = usuarioRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com email = " + email));
        return new ResponseEntity<>(new UsuarioDTO(u), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> cadastraUsuario (@Valid @RequestBody UsuarioForm usuarioForm, UriComponentsBuilder uriBuilder) throws IllegalArgumentException{
        if(!usuarioRepository.existsByEmail(usuarioForm.getEmail())){
            Usuario novoUsuario = new Usuario(usuarioForm.getNome(), usuarioForm.getEmail(), usuarioForm.getSenha());
            usuarioRepository.save(novoUsuario);
            return ResponseEntity.created(uriBuilder.path("/usuarios/{id}").buildAndExpand(novoUsuario.getId()).toUri()).body(new UsuarioDTO(novoUsuario));
        }else return new ResponseEntity<>("Usuário já existe",HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizaCadastroUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioForm usuarioForm) throws IllegalArgumentException{
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com id = " + id));
        return ResponseEntity.ok(new UsuarioDTO(usuarioForm.atualizar(id, usuarioRepository)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletaUsuario(@PathVariable Long id) throws IllegalArgumentException{
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com id = " + id));
        usuarioRepository.delete(u);
        return new ResponseEntity<>("Deletado com sucesso", HttpStatus.OK);
    }

    // ID USER VIA SESSAO
    @PutMapping("/{idUsuarioModerador}/tornarModerador/{idUsuarioNaoModerador}")
    @Transactional
    public ResponseEntity<?> tornarModerador (@PathVariable Long idUsuarioModerador, @PathVariable Long idUsuarioNaoModerador){
        Usuario usuarioTesteModerador = usuarioRepository.findById(idUsuarioModerador).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com id = " + idUsuarioModerador));
        Usuario usuarioTesteAlvo = usuarioRepository.findById(idUsuarioNaoModerador).orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado usuário com id = " + idUsuarioNaoModerador));
        if(usuarioService.verificaPerfil(usuarioTesteModerador, List.of(Perfil.MODERADOR))){
            usuarioTesteAlvo.setPerfil(Perfil.MODERADOR);
            return ResponseEntity.ok().body(new UsuarioDTO(usuarioTesteAlvo));
        }else return ResponseEntity.badRequest().body("Usuário não autorizado");
    }

}
