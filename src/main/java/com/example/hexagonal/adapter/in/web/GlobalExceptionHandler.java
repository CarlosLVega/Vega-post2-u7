package com.example.hexagonal.adapter.in.web;

import com.example.hexagonal.domain.exception.PrecioInvalidoException;
import com.example.hexagonal.domain.exception.ProductoNotFoundException;
import com.example.hexagonal.domain.exception.StockInsuficienteException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(ProductoNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler({PrecioInvalidoException.class, StockInsuficienteException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(RuntimeException ex) {
        return Map.of("error", ex.getMessage());
    }
}
