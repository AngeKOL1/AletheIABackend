package com.example.aletheia.pretest.exception;

import org.springframework.dao.DataIntegrityViolationException;
// import org.apache.hc.client5.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ResponseExceptionHandler {
    public static final String ERROR_TYPE_PROPERTY = "errorType";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            // Los errores a nivel de clase (ObjectError) no tienen campo.
            String campo = (error instanceof FieldError fe) ? fe.getField() : error.getObjectName();
            agregarError(errores, campo, error.getDefaultMessage());
        });

        return problemaDeValidacion(errores, request);
    }

    /** Validaciones sobre parámetros (@RequestParam, listas en @RequestBody...). */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleMethodValidation(HandlerMethodValidationException ex, HttpServletRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();

        ex.getParameterValidationResults().forEach(resultado -> {
            String base = resultado.getMethodParameter().getParameterName();
            if (resultado.getContainerIndex() != null) {
                base = base + "[" + resultado.getContainerIndex() + "]";
            }
            for (MessageSourceResolvable error : resultado.getResolvableErrors()) {
                String campo = (error instanceof FieldError fe) ? base + "." + fe.getField() : base;
                agregarError(errores, campo, error.getDefaultMessage());
            }
        });

        return problemaDeValidacion(errores, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(v ->
                agregarError(errores, v.getPropertyPath().toString(), v.getMessage()));
        return problemaDeValidacion(errores, request);
    }

    /** JSON mal formado o valor no permitido (p. ej. un enum inexistente como "fase": "MEDIO"). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Cuerpo de petición ilegible: {}", ex.getMostSpecificCause().getMessage());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición tiene un formato inválido o contiene valores no permitidos");
        pd.setTitle("Petición inválida");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty(ERROR_TYPE_PROPERTY, "ValidationError");
        return pd;
    }

    /** Parámetro con tipo incorrecto (p. ej. ?fase=XYZ o /sesiones-test/abc/finalizar). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Valor inválido para el parámetro '" + ex.getName() + "'");
        pd.setTitle("Parámetro inválido");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty(ERROR_TYPE_PROPERTY, "ValidationError");
        return pd;
    }

    private static void agregarError(Map<String, String> errores, String campo, String mensaje) {
        errores.merge(campo, mensaje, (previo, nuevo) -> previo + "; " + nuevo);
    }

    private static ProblemDetail problemaDeValidacion(Map<String, String> errores, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Uno o más campos tienen errores de validación");

        pd.setTitle("Error de validación");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty(ERROR_TYPE_PROPERTY, "ValidationError");
        pd.setProperty("campos", errores); // ← aquí van los errores por campo

        return pd;
    }

    @ExceptionHandler(ModelNotFoundException.class)
    public ProblemDetail handleModelNotFoundException(ModelNotFoundException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Model Not Found");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty(ERROR_TYPE_PROPERTY, "ModelNotFound");
        return pd;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Bad Request");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty(ERROR_TYPE_PROPERTY, "InvalidArgument");
        return pd;
    }

    @ExceptionHandler(ArithmeticException.class)
    public ProblemDetail handleArithmeticException(ArithmeticException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
        pd.setTitle("Arithmetic Error");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty(ERROR_TYPE_PROPERTY, "MathError");
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex, WebRequest request) {
        log.error("Error no controlado", ex); // log completo para ti, en el servidor
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado. Contacta al equipo si persiste.");
        pd.setTitle("Internal Server Error");
        pd.setProperty(ERROR_TYPE_PROPERTY, "UnexpectedError");
        return pd;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentialsException(InvalidCredentialsException ex, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        pd.setTitle("Invalid Credentials");
        pd.setProperty(ERROR_TYPE_PROPERTY, "AuthError");
        return pd;
    }

    @ExceptionHandler(UserDisabledException.class)
    public ProblemDetail handleUserDisabledException(UserDisabledException ex, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        pd.setTitle("User Disabled");
        pd.setProperty(ERROR_TYPE_PROPERTY, "AuthError");
        return pd;
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ProblemDetail handlePasswordMismatch(
            PasswordMismatchException ex, WebRequest request) {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());

        pd.setTitle("Password Mismatch");
        pd.setProperty(ERROR_TYPE_PROPERTY, "ValidationError");
        return pd;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        log.error("DataIntegrityViolationException: {}", ex.getMostSpecificCause().getMessage());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "El recurso ya existe o viola una restricción de la base de datos");
        pd.setTitle("Conflicto de datos");
        pd.setProperty(ERROR_TYPE_PROPERTY, "DataConflict");
        return pd;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setTitle("Estado inválido");
        pd.setProperty(ERROR_TYPE_PROPERTY, "InvalidState");
        return pd;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingParam(MissingServletRequestParameterException ex, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Falta el parámetro requerido: " + ex.getParameterName());
        pd.setTitle("Parámetro faltante");
        pd.setProperty(ERROR_TYPE_PROPERTY, "ValidationError");
        return pd;
    }

}
