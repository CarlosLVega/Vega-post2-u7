package com.example.hexagonal.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.hexagonal.domain.exception.StockInsuficienteException;
import com.example.hexagonal.domain.model.Producto;
import com.example.hexagonal.domain.port.out.ProductoRepositoryPort;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ProductoDomainServiceTest {
    @Test
    void reduceStockWithoutSpringContext() {
        InMemoryProductoRepository repository = new InMemoryProductoRepository();
        ProductoDomainService service = new ProductoDomainService(repository);
        Producto creado = service.crear(new Producto(null, "Teclado", "Mecanico", BigDecimal.valueOf(120000), 5));

        Producto actualizado = service.reducirStock(creado.getId(), 2);

        assertEquals(3, actualizado.getStock());
    }

    @Test
    void failsWhenStockIsInsufficient() {
        InMemoryProductoRepository repository = new InMemoryProductoRepository();
        ProductoDomainService service = new ProductoDomainService(repository);
        Producto creado = service.crear(new Producto(null, "Mouse", "Optico", BigDecimal.valueOf(50000), 1));

        assertThrows(StockInsuficienteException.class, () -> service.reducirStock(creado.getId(), 2));
    }

    private static class InMemoryProductoRepository implements ProductoRepositoryPort {
        private final List<Producto> productos = new ArrayList<>();
        private long sequence = 1;

        @Override
        public Producto guardar(Producto producto) {
            if (producto.getId() == null) {
                producto.setId(sequence++);
                productos.add(producto);
                return producto;
            }
            productos.removeIf(p -> p.getId().equals(producto.getId()));
            productos.add(producto);
            return producto;
        }

        @Override
        public Optional<Producto> buscarPorId(Long id) {
            return productos.stream().filter(p -> p.getId().equals(id)).findFirst();
        }

        @Override
        public List<Producto> buscarTodos() {
            return productos;
        }

        @Override
        public void eliminar(Long id) {
            productos.removeIf(p -> p.getId().equals(id));
        }
    }
}
