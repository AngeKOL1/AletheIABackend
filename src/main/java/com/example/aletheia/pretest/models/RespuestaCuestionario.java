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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "respuesta_cuestionario", uniqueConstraints = {
        @UniqueConstraint(name = "uk_respuesta_sesion_item",
                columnNames = { "sesion_test_id", "item_cuestionario_id" })
})
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
public class RespuestaCuestionario {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_test_id", nullable = false)
    private SesionTest sesionTest;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_cuestionario_id", nullable = false)
    private ItemCuestionario itemCuestionario;

    // Texto porque admite tipos "likert_5" (1-5) y "texto"; se valida según el tipo del ítem.
    @Column(name = "respuesta", nullable = false)
    private String respuesta;
}
