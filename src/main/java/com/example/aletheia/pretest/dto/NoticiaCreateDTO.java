package com.example.aletheia.pretest.dto;

import java.time.LocalDate;

import com.example.aletheia.pretest.models.enums.Forma;
import com.example.aletheia.pretest.models.enums.FuenteTexto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticiaCreateDTO {

    @NotBlank
    private String titular;

    private String epigrafe;

    private String subtitulo;

    private String entradilla;

    private String cuerpo;

    private String cierre;

    @NotBlank
    private String img;

    private String fuente;

    private LocalDate fechaPublicacion;

    private String tema;

    @NotNull
    private Forma forma;

    private FuenteTexto fuenteTexto;

    @NotNull
    private Integer categoriaVeredictoId;
}