package com.example.aletheia.pretest.models;

import java.time.LocalDateTime;

import com.example.aletheia.pretest.models.enums.Fase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "sesion_test", uniqueConstraints = {
        @UniqueConstraint(name = "uk_sesion_participante_fase", columnNames = { "participante_id", "fase" })
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SesionTest {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id", nullable = false)
    private Participante participante;

    @Enumerated(EnumType.STRING)
    @Column(name = "fase", nullable = false)
    private Fase fase;

    @Column (name = "hora_inicio", nullable = false)
    private LocalDateTime horaInicio;

    // null mientras la sesión está abierta
    @Column (name = "hora_fin")
    private LocalDateTime horaFin;

    @Column (name = "titulo", nullable = false)
    private String titulo;
}
