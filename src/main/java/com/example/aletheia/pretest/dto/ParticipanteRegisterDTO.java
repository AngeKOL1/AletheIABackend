package com.example.aletheia.pretest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipanteRegisterDTO {
    @NotBlank
    @Email
    private String correo;

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @NotBlank
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;

    @Min(value = 1, message = "La edad debe ser un número positivo")
    private Integer edad;

    private String genero;

    private String nivelEducativo;

    private String frecuenciaConsumoNoticias;
}
