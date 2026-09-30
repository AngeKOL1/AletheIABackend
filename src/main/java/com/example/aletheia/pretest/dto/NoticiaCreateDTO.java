package com.example.aletheia.pretest.dto;

import java.time.LocalDate;

import com.example.aletheia.pretest.models.enums.Forma;
import com.example.aletheia.pretest.models.enums.FuenteTexto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class NoticiaCreateDTO {

    @NotBlank(message = "El titular es obligatorio")
    @Size(min = 5, max = 100, message = "El titular debe tener entre 5 y 100 caracteres")
    private String titular;

    @NotBlank (message = "La imagen es obligatoria")
    @Size(min = 5, max = 200, message = "La imagen debe tener entre 5 y 200 caracteres")
    private String epigrafe;

    @NotBlank (message = "El subtitulo es obligatorio")
    @Size(min = 5, max = 100, message = "El subtitulo debe tener entre 5 y 100 caracteres")
    private String subtitulo;

    @NotBlank (message = "La entradilla es obligatoria")
    @Size(min = 5, max = 200, message = "La entradilla debe tener entre 5 y 200 caracteres")
    private String entradilla;

    @NotBlank (message = "El cuerpo es obligatorio")
    @Size(min = 10, max = 1000, message = "El cuerpo debe tener entre 10 y 1000 caracteres")
    private String cuerpo;

    @NotBlank (message = "El cierre es obligatorio")
    @Size(min = 5, max = 200, message = "El cierre debe tener entre 5 y 200 caracteres")    
    private String cierre;

    @NotBlank (message = "La imagen es obligatoria")
    private String img;
    
    @NotNull (message = "La fuente es obligatoria")
    private FuenteTexto fuente;

    private LocalDate fechaPublicacion;

    @NotBlank (message = "El tema es obligatorio")
    @Size(min = 3, max = 50, message = "El tema debe tener entre 3 y 50 caracteres")
    private String tema;

    @NotNull (message = "La forma es obligatoria")
    private Forma forma;

    @NotBlank (message = "La fuente del texto es obligatoria")
    @Size(min = 5, max = 100, message = "La fuente del texto debe tener entre 5 y 100 caracteres")
    private FuenteTexto fuenteTexto;

    @NotNull 
    private Integer categoriaVeredictoId;
}
