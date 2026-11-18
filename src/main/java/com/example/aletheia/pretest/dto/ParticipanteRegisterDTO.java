package com.example.aletheia.pretest.dto;

import com.example.aletheia.pretest.models.enums.FrecuenciaConsumoNoticias;
import com.example.aletheia.pretest.models.enums.Genero;
import com.example.aletheia.pretest.models.enums.NivelEducativo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
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
    @Email(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "El correo no tiene un formato válido")
    @Size(max = 255, message = "El correo no puede superar 255 caracteres")
    private String correo;

    @NotBlank
    @Size(max = 255, message = "Los nombres no pueden superar 255 caracteres")
    private String nombres;

    @NotBlank
    @Size(max = 255, message = "Los apellidos no pueden superar 255 caracteres")
    private String apellidos;

    // BCrypt solo usa los primeros 72 bytes de la contraseña.
    @NotBlank
    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
    private String contrasena;

    @Min(value = 18, message = "La edad mínima es 18 años")
    @Max(value = 99, message = "La edad máxima es 99 años")
    private Integer edad;

    // Se aceptan las etiquetas que envía el frontend ("Femenino", "Posgrado"...).
    private Genero genero;

    private NivelEducativo nivelEducativo;

    private FrecuenciaConsumoNoticias frecuenciaConsumoNoticias;

    @Size(max = 255, message = "El medio de comunicación no puede superar 255 caracteres")
    private String medioComunicacion;

    @Size(max = 255, message = "El cargo no puede superar 255 caracteres")
    private String cargo;
}
