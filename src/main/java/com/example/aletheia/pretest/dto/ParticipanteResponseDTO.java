package com.example.aletheia.pretest.dto;

import com.example.aletheia.pretest.models.enums.FrecuenciaConsumoNoticias;
import com.example.aletheia.pretest.models.enums.Genero;
import com.example.aletheia.pretest.models.enums.NivelEducativo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipanteResponseDTO {
    private Integer id;
    private String correo;
    private String nombres;
    private String apellidos;
    private Integer edad;
    private Genero genero;
    private NivelEducativo nivelEducativo;
    private FrecuenciaConsumoNoticias frecuenciaConsumoNoticias;
}
