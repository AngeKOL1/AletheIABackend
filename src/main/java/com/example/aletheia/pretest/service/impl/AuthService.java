package com.example.aletheia.pretest.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.example.aletheia.pretest.dto.LoginRequestDTO;
import com.example.aletheia.pretest.dto.LoginResponseDTO;
import com.example.aletheia.pretest.security.CustomUserDetails;
import com.example.aletheia.pretest.security.JwtTokenUtil;
import com.example.aletheia.pretest.security.JwtUserDetailsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    public LoginResponseDTO autenticar(LoginRequestDTO dto) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getCorreo(), dto.getContrasena())
            );
        } catch (DisabledException e) {
            throw new RuntimeException("Usuario deshabilitado");
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Credenciales inválidas");
        }

        CustomUserDetails userDetails =
                (CustomUserDetails) jwtUserDetailsService.loadUserByUsername(dto.getCorreo());

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