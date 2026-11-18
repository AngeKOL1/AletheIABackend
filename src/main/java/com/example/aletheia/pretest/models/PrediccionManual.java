package com.example.aletheia.pretest.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "prediccion_manual", uniqueConstraints = {
        @UniqueConstraint(name = "uk_prediccion_participante_noticia_fase",
                columnNames = { "participante_id", "noticia_id", "fase" })
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PrediccionManual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "fase", nullable = false)
    private Fase fase;

    @Column(name = "orden_presentacion")
    private Integer ordenPresentacion;

    @Column(name = "tiempo_respuesta_seg")
    private Integer tiempoRespuestaSeg;

    @Column(name = "confianza_autopercibida")
    private Integer confianzaAutopercibida;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id", nullable = false)
    private Participante participante;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noticia_id", nullable = false)
    private Noticia noticia;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_elegida_id", nullable = false)
    private CategoriaVeredicto categoriaElegida;

    @CreationTimestamp
    @Column(name = "fecha_hora", nullable = false, updatable = false)
    private LocalDateTime fechaHora;

}
