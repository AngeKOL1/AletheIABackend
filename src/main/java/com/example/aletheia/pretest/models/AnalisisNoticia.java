package com.example.aletheia.pretest.models;

import java.time.LocalDateTime;

import com.example.aletheia.pretest.models.enums.EstadoAnalisis;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Análisis de una noticia hecho por AletheIA. Se comparte entre todos los usuarios que lo
 * soliciten (ver {@link SolicitudAnalisis}) para no pagar dos veces la misma noticia.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "analisis_noticia",
        uniqueConstraints = @UniqueConstraint(name = "uk_analisis_aletheia_id", columnNames = "aletheia_id"),
        indexes = {
                @Index(name = "idx_analisis_estado", columnList = "estado"),
                @Index(name = "idx_analisis_noticia", columnList = "noticia_id")
        })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AnalisisNoticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noticia_id", nullable = false)
    private Noticia noticia;

    // Id del análisis en AletheIA (uuid4 hex). Nunca se envía al navegador.
    @Column(name = "aletheia_id", nullable = false, length = 32)
    private String aletheiaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoAnalisis estado;

    // Último estado informado por AletheIA (en_cola, en_proceso...), solo informativo.
    @Column(name = "estado_aletheia", length = 30)
    private String estadoAletheia;

    // Campos del informe que se consultan sin leer el JSON completo.
    @Column(name = "veredicto_global", length = 30)
    private String veredictoGlobal;

    @Column(name = "motivo_sin_veredicto", length = 50)
    private String motivoSinVeredicto;

    // Informe completo tal como lo describe el esquema (JSON en snake_case).
    @ToString.Exclude
    @Column(name = "informe_json", columnDefinition = "TEXT")
    private String informeJson;

    @Column(name = "error_tipo", length = 50)
    private String errorTipo;

    @Column(name = "error_etapa", length = 50)
    private String errorEtapa;

    @Column(name = "error_mensaje", columnDefinition = "TEXT")
    private String errorMensaje;

    @Column(name = "creado", nullable = false, updatable = false)
    private LocalDateTime creado;

    @Column(name = "actualizado", nullable = false)
    private LocalDateTime actualizado;

    @Column(name = "finalizado")
    private LocalDateTime finalizado;
}
