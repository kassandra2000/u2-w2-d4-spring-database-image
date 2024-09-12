package kassandrafalsitta.u2w2d3.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogPayload {
    private String category;
    private String title;
    private String cover;
    private String content;
    private double readingTime;
    private UUID authorId;
}
