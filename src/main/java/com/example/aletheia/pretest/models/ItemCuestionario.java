package com.example.aletheia.pretest.models;

import com.example.aletheia.pretest.models.converters.TipoRespuestaConverter;
import com.example.aletheia.pretest.models.enums.Dimension;
import com.example.aletheia.pretest.models.enums.FaseAplicacion;
import com.example.aletheia.pretest.models.enums.TipoRespuesta;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_cuestionario")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemCuestionario {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "contenido", nullable = false)
    private String contenido;
    // Se guarda como "likert_5" / "texto", el mismo valor que ya existe en la BD.
    @Convert(converter = TipoRespuestaConverter.class)
    @Column(name = "tipo_respuesta", nullable = false)
    private TipoRespuesta tipoRespuesta;
    @Column(name = "orden", nullable = false)
    private Integer orden;
    @Enumerated(EnumType.STRING)
    @Column(name = "dimension", nullable = false)
    private Dimension dimension;

    @Enumerated(EnumType.STRING)
    @Column(name = "fase_aplicacion", nullable = false)
    private FaseAplicacion faseAplicacion;
}
