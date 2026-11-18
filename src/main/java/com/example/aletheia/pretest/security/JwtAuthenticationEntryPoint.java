package com.example.aletheia.pretest.security;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

/** 401: sin token, o token inválido o caducado. */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String MENSAJE = "Token no encontrado o inválido";

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                          AuthenticationException authException) throws IOException, ServletException {

        // El detalle técnico del token (firma, formato...) solo va al log: daría pistas a un atacante.
        Object detalle = request.getAttribute("jwtError");
        if (detalle != null) {
            log.debug("Token rechazado en {}: {}", request.getRequestURI(), detalle);
        }

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, MENSAJE);
        pd.setTitle("No autenticado");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("errorType", "AuthError");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Sin esto el contenedor usa ISO-8859-1 y las tildes llegan corruptas al frontend.
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(pd));
    }
}
