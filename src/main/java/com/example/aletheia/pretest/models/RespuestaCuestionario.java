package com.example.aletheia.pretest.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table(name = "respuesta_cuestionario")
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
public class RespuestaCuestionario {
    @Id 
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_test_id", nullable = false)
    private SesionTest sesionTest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_cuestionario_id", nullable = false)
    private ItemCuestionario itemCuestionario;

    @Column(name = "respuesta", nullable = false)
    private String respuesta;
}
