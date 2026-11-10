package com.example.aletheia.pretest.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.example.aletheia.pretest.dto.LoginRequestDTO;
import com.example.aletheia.pretest.dto.LoginResponseDTO;
import com.example.aletheia.pretest.exception.InvalidCredentialsException;
import com.example.aletheia.pretest.exception.UserDisabledException;
import com.example.aletheia.pretest.security.CustomUserDetails;
import com.example.aletheia.pretest.security.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public LoginResponseDTO autenticar(LoginRequestDTO dto) {
        String correo = dto.getCorreo().trim();

        Authentication autenticacion;
        try {
            autenticacion = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(correo, dto.getContrasena())
            );
        } catch (DisabledException e) {
            throw new UserDisabledException("Usuario deshabilitado");
        } catch (AuthenticationException e) {
            // Credenciales incorrectas, usuario inexistente, etc.: mismo mensaje para no revelar cuál fue.
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        // El usuario ya viene cargado en la autenticación; no hace falta volver a consultarlo.
        CustomUserDetails userDetails = (CustomUserDetails) autenticacion.getPrincipal();

        String token = jwtTokenUtil.generateToken(userDetails, userDetails.getId());

        return LoginResponseDTO.builder()
                .token(token)
                .id(userDetails.getId())
                .nombres(userDetails.getNombres())
                .correo(userDetails.getUsername())
                .rol(userDetails.getRol())
                .build();
    }
}
