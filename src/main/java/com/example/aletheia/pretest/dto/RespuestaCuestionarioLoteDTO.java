package com.example.aletheia.pretest.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaCuestionarioLoteDTO {
    @NotNull
    @Positive
    private Integer sesionTestId;

    @NotEmpty(message = "Debe enviar al menos una respuesta")
    @Size(max = 200, message = "Demasiadas respuestas en un solo lote")
    private List<@NotNull @Valid ItemRespuesta> respuestas;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemRespuesta {
        @NotNull
        @Positive
        private Integer itemCuestionarioId;

        // El formato concreto (p. ej. 1-5 para likert_5) se valida en el servicio según el ítem.
        @NotBlank
        @Size(max = 255, message = "La respuesta no puede superar 255 caracteres")
        private String respuesta;
    }
}
