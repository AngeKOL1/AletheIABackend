package com.example.aletheia.pretest.exception;

/** AletheIA respondió 422: la noticia no es válida. El mensaje es el detalle que se muestra al usuario. */
public class AletheiaNoticiaInvalidaException extends AletheiaException {
    public AletheiaNoticiaInvalidaException(String detalle) {
        super(detalle);
    }
}
