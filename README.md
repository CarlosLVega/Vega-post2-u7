# Modulo de Productos con Arquitectura Hexagonal

## Informacion del proyecto

**Asignatura:** Patrones de Diseno de Software  
**Unidad:** Unidad 7 - Patrones Arquitectonicos I  
**Actividad:** Post-Contenido 2  
**Repositorio:** Vega-post2-u7  

## Descripcion

Este proyecto implementa una API REST para la gestion de productos usando Spring Boot y arquitectura hexagonal, tambien conocida como Ports & Adapters. El objetivo es mantener el dominio independiente de frameworks externos y ubicar los detalles tecnicos en adaptadores.

## Tecnologias

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- H2 Database
- Maven
- JUnit 5

## Arquitectura hexagonal

La aplicacion separa el nucleo de negocio de la infraestructura mediante puertos e implementaciones externas.

```text
adapter/in/web              -> Adaptador REST de entrada
adapter/out/persistence     -> Adaptador JPA de salida
config                      -> Wiring explicito de Spring
domain/model                -> Modelo de dominio puro
domain/port/in              -> Puertos de entrada o casos de uso
domain/port/out             -> Puerto de salida para persistencia
domain/service              -> Servicio de dominio puro
```

## Estructura

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
│   │   │   ├── ActualizarStockUseCase.java
│   │   │   ├── CrearProductoUseCase.java
│   │   │   └── ListarProductosUseCase.java
│   │   └── out
│   │       └── ProductoRepositoryPort.java
│   └── service
│       └── ProductoDomainService.java
├── adapter
│   ├── in
│   │   └── web
│   │       ├── GlobalExceptionHandler.java
│   │       └── ProductoController.java
│   └── out
│       └── persistence
│           ├── ProductoJpaEntity.java
│           ├── ProductoJpaRepository.java
│           └── ProductoRepositoryAdapter.java
├── config
│   └── BeanConfiguration.java
└── HexagonalApplication.java
```

## Puertos

### Puertos de entrada

- `CrearProductoUseCase`: define el caso de uso para crear productos.
- `ListarProductosUseCase`: define el listado y busqueda por ID.
- `ActualizarStockUseCase`: define la reduccion de stock.

### Puerto de salida

- `ProductoRepositoryPort`: define lo que el dominio necesita de la persistencia sin depender de JPA.

## Dominio puro

La clase `Producto` no tiene anotaciones de Spring ni JPA. Su logica de negocio se mantiene dentro del dominio:

- Validar disponibilidad.
- Reducir stock.
- Lanzar error si el stock es insuficiente.

El servicio `ProductoDomainService` tampoco tiene `@Service`; se registra explicitamente desde `BeanConfiguration`.

## Adaptadores

### Adaptador REST

`ProductoController` traduce peticiones HTTP en llamadas a los puertos de entrada.

### Adaptador JPA

`ProductoRepositoryAdapter` implementa `ProductoRepositoryPort` y traduce entre:

```text
Producto          -> Modelo de dominio
ProductoJpaEntity -> Entidad JPA de infraestructura
```

## Endpoints

```text
GET   /api/productos
GET   /api/productos/{id}
POST  /api/productos
PATCH /api/productos/{id}/stock?cantidad=2
```

## Ejecucion

Compilar y probar:

```bash
mvn clean package
```

Ejecutar:

```bash
mvn spring-boot:run
```

La API queda disponible en:

```text
http://localhost:8080
```

La consola H2 queda disponible en:

```text
http://localhost:8080/h2-console
```

Datos de H2:

```text
JDBC URL: jdbc:h2:mem:productosdb
User Name: sa
Password: 
```

## Pruebas con PowerShell

Listar productos:

```powershell
curl http://localhost:8080/api/productos
```

Crear producto:

```powershell
curl -Method POST http://localhost:8080/api/productos -ContentType "application/json" -Body '{"nombre":"Teclado","descripcion":"Teclado mecanico","precio":120000,"stock":10}'
```

Buscar producto:

```powershell
curl http://localhost:8080/api/productos/1
```

Reducir stock:

```powershell
curl -Method PATCH "http://localhost:8080/api/productos/1/stock?cantidad=3"
```

Probar stock insuficiente:

```powershell
curl -Method PATCH "http://localhost:8080/api/productos/1/stock?cantidad=100"
```

## Capturas sugeridas

Agregar capturas de:

```text
capturas/get-productos.png
capturas/post-producto.png
capturas/patch-stock.png
capturas/error-stock-insuficiente.png
capturas/h2-console.png
```

## Checkpoints

- El proyecto compila con `mvn clean package`.
- `GET /api/productos` retorna lista vacia al inicio.
- `POST /api/productos` crea un producto y retorna `201 Created`.
- `PATCH /api/productos/{id}/stock` reduce el stock correctamente.
- Si la cantidad supera el stock disponible, retorna `400 Bad Request`.
- Las clases en `domain/` no importan Spring ni JPA.
- `ProductoDomainService` se prueba en JUnit sin `@SpringBootTest`.
- El adaptador JPA traduce entre dominio y entidad de persistencia.

## Commits sugeridos

```text
feat: crear proyecto Spring Boot con estructura hexagonal
feat: implementar dominio puro y puertos de productos
feat: agregar adaptadores REST y JPA para productos
feat: agregar pruebas y documentacion de arquitectura hexagonal
```

## Conclusion

La arquitectura hexagonal permite proteger el dominio de detalles tecnicos. En este proyecto, las reglas de negocio viven en clases Java puras, los casos de uso se expresan como puertos y la infraestructura se conecta mediante adaptadores REST y JPA.
