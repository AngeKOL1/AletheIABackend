package com.example.aletheia.pretest.dto;

import java.time.LocalDateTime;

import com.example.aletheia.pretest.models.enums.Fase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SesionTestResponseDTO {
    private Integer id;
    private Fase fase;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private String titulo;
}
