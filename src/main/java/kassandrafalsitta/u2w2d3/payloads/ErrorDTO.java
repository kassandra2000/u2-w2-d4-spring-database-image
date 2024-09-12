package kassandrafalsitta.u2w2d3.payloads;

import java.time.LocalDateTime;

public record ErrorDTO(String message,LocalDateTime timestamp) {
}
