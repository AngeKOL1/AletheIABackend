package com.example.aletheia.pretest.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaCuestionarioLoteDTO {
    private Integer sesionTestId;
    private List<ItemRespuesta> respuestas;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemRespuesta {
        private Integer itemCuestionarioId;
        private String respuesta;
    }
}