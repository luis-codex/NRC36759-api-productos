package ec.edu.espe.apiproductos.exception;

public class ProductoInvalidoException extends RuntimeException {
    public ProductoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
