package kassandrafalsitta.u2w2d3.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;


public record BlogDTO (
        @NotEmpty(message = "La categoria è obbligatoria")
        @Size(min = 3, max = 30, message = "La categoria deve essere compresa tra 3 e 30 caratteri")
    String category,
        @NotEmpty(message = "Il titolo è obbligatorio")
        @Size(min = 3, max = 40, message = "Il titolo deve essere compreso tra 3 e 40 caratteri")
        String title,
        @NotEmpty(message = "Il contenuto è obbligatorio")
        @Size(min = 3, max = 100, message = "Il contenuto deve essere compreso tra 3 e 100 caratteri")
        String content,
        @NotNull(message = "Il tempo di lettura è obbligatorio")
        @Positive(message = "Il tempo di lettura deve essere un valore positivo")
        double readingTime,
        @NotNull(message = "L'UUID è obbligatorio")
        UUID authorId){}
