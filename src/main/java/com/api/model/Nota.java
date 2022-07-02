package com.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter @Setter
@Entity
@Table(name = "notas")
@NoArgsConstructor
public class Nota {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int valor;

    @Column
    @CreationTimestamp
    private Timestamp dataCriacao;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "filme_id", nullable = false)
    private Filme filme;

    public Nota(int valor, Usuario usuario, Filme filme){
        this.valor = valor;
        this.usuario = usuario;
        this.filme = filme;
    }
}
