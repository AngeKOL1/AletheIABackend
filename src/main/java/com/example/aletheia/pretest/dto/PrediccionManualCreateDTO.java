package com.example.aletheia.pretest.dto;

import com.example.aletheia.pretest.models.enums.Fase;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrediccionManualCreateDTO {
    @NotNull
    private Integer noticiaId;
    @NotNull
    private Integer categoriaElegidaId;
    @NotNull
    private Fase fase;
    private Integer ordenPresentacion;
    private Integer tiempoRespuestaSeg;
    private Integer confianzaAutopercibida;
}