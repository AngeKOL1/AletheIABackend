package com.example.aletheia.pretest.dto;

import java.time.LocalDateTime;
import com.example.aletheia.pretest.models.enums.Fase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
public class PrediccionManualResponseDTO {
    private Integer id;
    private Integer noticiaId;
    private String categoriaElegidaNombre;
    private Fase fase;
    private Integer ordenPresentacion;
    private Integer tiempoRespuestaSeg;
    private Integer confianzaAutopercibida;
    private LocalDateTime fechaHora;
}