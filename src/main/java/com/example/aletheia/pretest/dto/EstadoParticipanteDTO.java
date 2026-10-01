package com.example.aletheia.pretest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoParticipanteDTO {
    private long prediccionesRespondidas;
    private long totalNoticias;
    private boolean cuestionarioCompletado;
    private Integer sesionAbiertaId;
}