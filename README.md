# Módulo de Productos con Arquitectura Hexagonal

## Información del proyecto

**Asignatura:** Patrones de Diseño de Software  
**Unidad:** Unidad 7 - Patrones Arquitectónicos I  
**Actividad:** Post-Contenido 2  
**Estudiante:** Carlos Vega  
**Repositorio:** Vega-post2-u7  

## Descripción

Este proyecto implementa un módulo de gestión de productos aplicando **arquitectura hexagonal** (Ports & Adapters).

El objetivo es separar completamente el dominio de la infraestructura, garantizando que la lógica de negocio no dependa de frameworks como Spring Boot o tecnologías como JPA o REST.

La aplicación expone una API REST para crear, listar, consultar productos y reducir su stock.

## Tecnologías utilizadas

- Java 17  
- Spring Boot 3.3.5  
- Spring Web  
- Spring Data JPA  
- H2 Database  
- Maven  
- JUnit 5  

## Base de datos

Se utiliza H2 en memoria.

Consola:

```text
http://localhost:8080/h2-console
````

Datos:

* JDBC URL: `jdbc:h2:mem:productosdb`
* User Name: `sa`
* Password: *(vacío)*

## Arquitectura hexagonal

El sistema se divide en tres componentes:

* Dominio
* Puertos
* Adaptadores

El dominio contiene la lógica del negocio, los puertos definen contratos y los adaptadores conectan con tecnologías externas.

## Estructura del proyecto

```text
com.example.hexagonal
├── domain
│   ├── model
│   │   └── Producto.java
│   ├── exception
│   │   ├── PrecioInvalidoException.java
│   │   ├── ProductoNotFoundException.java
│   │   └── StockInsuficienteException.java
│   ├── port
│   │   ├── in
│   │   │   ├── CrearProductoUseCase.java
│   │   │   ├── ListarProductosUseCase.java
│   │   │   └── ActualizarStockUseCase.java
│   │   └── out
│   │       └── ProductoRepositoryPort.java
│   └── service
│       └── ProductoDomainService.java
├── adapter
│   ├── in
│   │   └── web
│   │       ├── ProductoController.java
│   │       └── GlobalExceptionHandler.java
│   └── out
│       └── persistence
│           ├── ProductoJpaEntity.java
│           ├── ProductoJpaRepository.java
│           └── ProductoRepositoryAdapter.java
├── config
│   └── BeanConfiguration.java
└── HexagonalApplication.java
```

## Dominio

La clase `Producto` pertenece al dominio y no depende de Spring ni JPA.

Ejemplo de lógica:

```java
public void reducirStock(int cantidad) {
    if (cantidad > this.stock) {
        throw new StockInsuficienteException(
            "Stock insuficiente. Disponible: " + this.stock);
    }
    this.stock -= cantidad;
}
```

## Puertos de entrada

Definen los casos de uso:

```java
Producto crear(Producto producto);
List<Producto> listarTodos();
Producto buscarPorId(Long id);
Producto reducirStock(Long id, int cantidad);
```

## Puerto de salida

```java
Producto guardar(Producto producto);
Optional<Producto> buscarPorId(Long id);
List<Producto> buscarTodos();
void eliminar(Long id);
```

## Servicio de dominio

`ProductoDomainService` implementa los casos de uso sin depender de Spring. Se registra manualmente en configuración.

## Adaptador REST

Expone la API:

```text
GET   /api/productos
GET   /api/productos/{id}
POST  /api/productos
PATCH /api/productos/{id}/stock?cantidad=3
```

## Adaptador de persistencia

Encapsula JPA:

* ProductoJpaEntity
* ProductoJpaRepository
* ProductoRepositoryAdapter

## Configuración de beans

```java
@Configuration
public class BeanConfiguration {

    @Bean
    public ProductoDomainService productoDomainService(
            ProductoRepositoryPort repositoryPort) {
        return new ProductoDomainService(repositoryPort);
    }
}
```

## Cómo ejecutar el proyecto

```bash
mvn compile
mvn package
mvn spring-boot:run
```

URL base:

```text
http://localhost:8080
```

## Pruebas con PowerShell

```powershell
curl http://localhost:8080/api/productos

curl -Method POST http://localhost:8080/api/productos `
  -ContentType "application/json" `
  -Body '{"nombre":"Teclado","descripcion":"Teclado mecanico","precio":120000,"stock":10}'

curl http://localhost:8080/api/productos/1

curl -Method PATCH "http://localhost:8080/api/productos/1/stock?cantidad=3"

curl -Method PATCH "http://localhost:8080/api/productos/1/stock?cantidad=100"
```

## Verificación

* Compila correctamente
* API funcional
* CRUD operativo
* Validación de stock correcta
* Dominio sin dependencias de Spring
* Adaptadores desacoplados

## Commits sugeridos

```bash
git add .
git commit -m "Configura proyecto con arquitectura hexagonal"

git add .
git commit -m "Implementa dominio y puertos"

git add .
git commit -m "Agrega adaptadores REST y JPA"

git add .
git commit -m "Documenta arquitectura y pruebas"
```

## Conclusión

La arquitectura hexagonal permite aislar el dominio de detalles técnicos, logrando un sistema desacoplado, mantenible y fácilmente testeable.

