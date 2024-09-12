package kassandrafalsitta.u2w2d3.repositories;

import kassandrafalsitta.u2w2d3.entities.Author;
import kassandrafalsitta.u2w2d3.entities.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlogsRepository extends JpaRepository<Blog, UUID> {
    Optional<Blog> findByTitle(String title);
    Optional<Blog> findByContent(String content);

}
