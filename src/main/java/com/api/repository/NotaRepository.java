package com.api.repository;

import com.api.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota,Long> {
    Nota findByUsuarioIdAndFilmeId(Long usuarioId, String filmeId);
}
