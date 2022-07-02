package com.api.service;

import com.api.model.Perfil;
import com.api.model.Usuario;
import com.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    public void adicionaPonto(Long id) throws  IllegalArgumentException{
        Optional<Usuario> testaUsuario = usuarioRepository.findById(id);
        if(testaUsuario.isPresent()){
            Usuario u = testaUsuario.get();
            u.setPontos(u.getPontos() + 1);
            System.out.println("Ponto adquirido!");
            usuarioRepository.save(u);
            testaMudancaPerfil(u);

        }
    }

    public void testaMudancaPerfil(Usuario u) throws IllegalArgumentException{
        int pontos = u.getPontos();
        Perfil perfil =  u.getPerfil();
        if(perfil != Perfil.MODERADOR) {
            if (pontos >= 20 && pontos < 100) u.setPerfil(Perfil.BÁSICO); //pontos entre 20 e 99
            else if (pontos >= 100 && pontos < 1000) u.setPerfil(Perfil.AVANÇADO); //pontos entre 100 e 999
            else if (pontos >= 1000) u.setPerfil(Perfil.MODERADOR); //pontos a partir de 1000
            usuarioRepository.save(u);
        }
        System.out.println("Perfil atual = " +  u.getPerfil().toString() + " | Pontos = " + u.getPontos());
    }

    public boolean verificaPerfil(Usuario u , List<Perfil> perfis){
        if (perfis.contains(u.getPerfil())) return true;
        else return false;
    }

}
