package ec.edu.espe.apiproductos.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarProductoNoEncontrado(
            ProductoNoEncontradoException exception, HttpServletRequest request) {
        return construirRespuesta(exception.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ProductoInvalidoException.class)
    public ResponseEntity<ErrorResponse> manejarProductoInvalido(
            ProductoInvalidoException exception, HttpServletRequest request) {
        return construirRespuesta(exception.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<ErrorResponse> construirRespuesta(
            String mensaje, HttpStatus estado, HttpServletRequest request) {
        return ResponseEntity.status(estado).body(new ErrorResponse(
                mensaje, estado.value(), Instant.now(), request.getRequestURI()));
    }
}
