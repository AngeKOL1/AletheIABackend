package com.example.aletheia.pretest.dto;

import java.time.LocalDate;

import com.example.aletheia.pretest.models.enums.Forma;
import com.example.aletheia.pretest.models.enums.FuenteTexto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticiaCreateDTO {

    @NotBlank
    @Size(max = 255, message = "El titular no puede superar 255 caracteres")
    private String titular;

    @Size(max = 255, message = "El epígrafe no puede superar 255 caracteres")
    private String epigrafe;

    @Size(max = 255, message = "El subtítulo no puede superar 255 caracteres")
    private String subtitulo;

    @Size(max = 255, message = "La entradilla no puede superar 255 caracteres")
    private String entradilla;

    @Size(max = 50000, message = "El cuerpo no puede superar 50000 caracteres")
    private String cuerpo;

    @Size(max = 255, message = "El cierre no puede superar 255 caracteres")
    private String cierre;

    @NotBlank
    @Size(max = 2048, message = "La ruta de la imagen no puede superar 2048 caracteres")
    private String img;

    @Size(max = 255, message = "La fuente no puede superar 255 caracteres")
    private String fuente;

    @PastOrPresent(message = "La fecha de publicación no puede ser futura")
    private LocalDate fechaPublicacion;

    @Size(max = 255, message = "El tema no puede superar 255 caracteres")
    private String tema;

    @NotNull
    private Forma forma;

    private FuenteTexto fuenteTexto;

    @NotNull
    @Positive
    private Integer categoriaVeredictoId;
}
