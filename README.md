# API REST de productos

API REST desarrollada con Spring Boot para gestionar productos en memoria, como parte del Taller Individual 1.1 de Programación Avanzada.

## Requisitos

- Java 21
- Spring Boot 4.1.2
- Maven Wrapper incluido

## Ejecución

```bash
./mvnw spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

## Endpoints

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/api/productos` | Lista todos los productos |
| GET | `/api/productos/{id}` | Consulta un producto |
| POST | `/api/productos` | Registra un producto |
| DELETE | `/api/productos/{id}` | Elimina un producto |

## Ejemplos

```bash
curl -i http://localhost:8080/api/productos
curl -i http://localhost:8080/api/productos/1
curl -i -X POST http://localhost:8080/api/productos \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Audífonos","precio":39.99,"stock":8}'
curl -i -X DELETE http://localhost:8080/api/productos/1
```

Las reglas de negocio se validan en `ProductoService`. Los errores propios se centralizan mediante `@RestControllerAdvice`.
