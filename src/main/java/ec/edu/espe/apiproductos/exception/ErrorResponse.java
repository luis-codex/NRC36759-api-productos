package ec.edu.espe.apiproductos.exception;

import java.time.Instant;

public record ErrorResponse(String mensaje, int estado, Instant timestamp, String path) {
}
