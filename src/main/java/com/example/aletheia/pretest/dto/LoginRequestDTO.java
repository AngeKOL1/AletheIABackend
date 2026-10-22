package com.example.aletheia.pretest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank
    @Size(max = 255)
    private String correo;

    @NotBlank
    @Size(max = 72)
    private String contrasena;
}
