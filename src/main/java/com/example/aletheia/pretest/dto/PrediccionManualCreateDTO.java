package com.example.aletheia.pretest.dto;

import com.example.aletheia.pretest.models.enums.Fase;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrediccionManualCreateDTO {
    @NotNull
    @Positive
    private Integer noticiaId;

    @NotNull
    @Positive
    private Integer categoriaElegidaId;

    @NotNull
    private Fase fase;

    @NotNull
    @Positive(message = "El orden de presentación empieza en 1")
    private Integer ordenPresentacion;

    @NotNull
    @Positive(message = "El tiempo de respuesta debe ser mayor a 0")
    @Max(value = 86400, message = "El tiempo de respuesta no puede superar 24 horas")
    private Integer tiempoRespuestaSeg;

    @NotNull
    @Min(value = 1, message = "La confianza va de 1 a 5")
    @Max(value = 5, message = "La confianza va de 1 a 5")
    private Integer confianzaAutopercibida;
}
