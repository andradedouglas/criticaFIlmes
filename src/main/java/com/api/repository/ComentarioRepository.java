package com.api.repository;

import com.api.controller.dto.ComentarioDTO;
import com.api.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario,Long> {
    public List<ComentarioDTO> findAllByFilmeId(String filmeId);

    @Query(value = "SELECT * FROM comentarios WHERE resposta_ao_comentario = ?1", nativeQuery = true)
    public List<Comentario> retornaRespostasAoComentario(Long comentarioId);

    @Query(value = "SELECT COUNT(*) FROM curtidas WHERE usuario_id = ?1 AND comentario_id = ?2", nativeQuery = true)
    public int existeCurtida(Long usuarioId, Long comentarioId);

    @Query(value = "SELECT COUNT(*) FROM descurtidas WHERE usuario_id = ?1 AND comentario_id = ?2", nativeQuery = true)
    public int existeDescurtida(Long usuarioId, Long comentarioId);

}
