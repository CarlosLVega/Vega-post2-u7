package com.example.hexagonal.adapter.in.web;

import com.example.hexagonal.domain.model.Producto;
import com.example.hexagonal.domain.port.in.ActualizarStockUseCase;
import com.example.hexagonal.domain.port.in.CrearProductoUseCase;
import com.example.hexagonal.domain.port.in.ListarProductosUseCase;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final CrearProductoUseCase crearUseCase;
    private final ListarProductosUseCase listarUseCase;
    private final ActualizarStockUseCase stockUseCase;

    public ProductoController(
            CrearProductoUseCase crearUseCase,
            ListarProductosUseCase listarUseCase,
            ActualizarStockUseCase stockUseCase) {
        this.crearUseCase = crearUseCase;
        this.listarUseCase = listarUseCase;
        this.stockUseCase = stockUseCase;
    }

    @GetMapping
    public List<Producto> listar() {
        return listarUseCase.listarTodos();
    }

    @GetMapping("/{id}")
    public Producto buscar(@PathVariable Long id) {
        return listarUseCase.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Producto producto) {
        return crearUseCase.crear(producto);
    }

    @PatchMapping("/{id}/stock")
    public Producto reducirStock(@PathVariable Long id, @RequestParam int cantidad) {
        return stockUseCase.reducirStock(id, cantidad);
    }
}
