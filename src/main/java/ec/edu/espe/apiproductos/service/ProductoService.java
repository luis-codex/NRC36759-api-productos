package ec.edu.espe.apiproductos.service;

import ec.edu.espe.apiproductos.exception.ProductoInvalidoException;
import ec.edu.espe.apiproductos.exception.ProductoNoEncontradoException;
import ec.edu.espe.apiproductos.model.Producto;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {
    private final ConcurrentMap<Long, Producto> productos = new ConcurrentHashMap<>();
    private final AtomicLong siguienteId = new AtomicLong(1);

    public ProductoService() {
        guardarSemilla("Teclado mecánico", new BigDecimal("45.90"), 12);
        guardarSemilla("Mouse inalámbrico", new BigDecimal("24.50"), 20);
        guardarSemilla("Monitor 24 pulgadas", new BigDecimal("189.99"), 6);
    }

    public List<Producto> listar() {
        return productos.values().stream().sorted(Comparator.comparing(Producto::getId))
                .map(this::copiar).toList();
    }

    public Producto buscarPorId(Long id) {
        Producto producto = productos.get(id);
        if (producto == null) throw new ProductoNoEncontradoException(id);
        return copiar(producto);
    }

    public Producto crear(Producto producto) {
        validar(producto);
        long id = siguienteId.getAndIncrement();
        Producto nuevo = new Producto(id, producto.getNombre().trim(), producto.getPrecio(), producto.getStock());
        productos.put(id, nuevo);
        return copiar(nuevo);
    }

    public void eliminar(Long id) {
        if (productos.remove(id) == null) throw new ProductoNoEncontradoException(id);
    }

    private void validar(Producto producto) {
        if (producto == null) throw new ProductoInvalidoException("El producto es obligatorio");
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new ProductoInvalidoException("El nombre es obligatorio");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductoInvalidoException("El precio debe ser mayor que cero");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new ProductoInvalidoException("El stock no puede ser negativo");
        }
    }

    private void guardarSemilla(String nombre, BigDecimal precio, int stock) {
        long id = siguienteId.getAndIncrement();
        productos.put(id, new Producto(id, nombre, precio, stock));
    }

    private Producto copiar(Producto producto) {
        return new Producto(producto.getId(), producto.getNombre(), producto.getPrecio(), producto.getStock());
    }
}
