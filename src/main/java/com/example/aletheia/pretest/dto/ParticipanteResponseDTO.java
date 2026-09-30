package com.example.aletheia.pretest.dto;

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
    private String genero;
    private String nivelEducativo;
    private String frecuenciaConsumoNoticias;
}
