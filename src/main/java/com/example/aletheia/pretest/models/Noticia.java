package com.example.aletheia.pretest.models;

import java.time.LocalDate;

import com.example.aletheia.pretest.models.enums.Forma;
import com.example.aletheia.pretest.models.enums.FuenteTexto;

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
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "noticia")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank
    @Column(name = "titular", nullable = false)
    private String titular;

    @Column(name = "epigrafe")
    private String epigrafe;

    @Column(name = "subtitulo")
    private String subtitulo;

    @Column(name = "entradilla")
    private String entradilla;

    // TEXT en lugar de @Lob: en PostgreSQL @Lob crea una columna oid (objeto grande).
    @Column(name = "cuerpo", columnDefinition = "TEXT")
    private String cuerpo;

    @Column(name = "cierre")
    private String cierre;

    @NotBlank
    @Column(name = "img", nullable = false, length = 2048)
    private String img;

    @Column(name = "fuente")
    private String fuente;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Column(name = "tema")
    private String tema;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma", nullable = false)
    private Forma forma;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuente_texto")
    private FuenteTexto fuenteTexto;

    @Column(name = "texto_ocr_bruto", columnDefinition = "TEXT")
    private String textoOcrBruto;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_veredicto_id", nullable = false)
    private CategoriaVeredicto categoriaVeredicto;
}