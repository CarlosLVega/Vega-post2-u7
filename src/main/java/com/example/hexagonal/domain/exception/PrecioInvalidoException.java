package com.example.hexagonal.domain.exception;

public class PrecioInvalidoException extends RuntimeException {
    public PrecioInvalidoException(String mensaje) {
        super(mensaje);
    }
}
