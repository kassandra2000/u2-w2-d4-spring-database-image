package kassandrafalsitta.u2w2d3.services;


import kassandrafalsitta.u2w2d3.entities.Author;
import kassandrafalsitta.u2w2d3.exceptions.BadRequestException;
import kassandrafalsitta.u2w2d3.exceptions.NotFoundException;
import kassandrafalsitta.u2w2d3.repositories.AuthorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AuthorService {
    @Autowired
    private AuthorsRepository authorsRepository;


    public Page<Author> findAll(int page, int size, String sortBy) {
        if(page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.authorsRepository.findAll(pageable);
    }

    public Author saveAuthor(Author body) {
        this.authorsRepository.findByEmail(body.getEmail()).ifPresent(
                author -> {
                    throw new BadRequestException("L'email " + body.getEmail() + " è già in uso!");
                }
        );
        body.setAvatar("https://ui-avatars.com/api/?name=" + body.getName() + "+" + body.getSurname());
        return this.authorsRepository.save(body);
    }

    public Author findById(UUID authorId) {
        return this.authorsRepository.findById(authorId).orElseThrow(() -> new NotFoundException(authorId));
    }

    public Author findByIdAndUpdate(UUID authorId, Author updatedAuthor) {
        this.authorsRepository.findByEmail(updatedAuthor.getEmail()).ifPresent(
                author -> {
                    throw new BadRequestException("L'email " + updatedAuthor.getEmail() + " è già in uso!");
                }
        );
        Author found = findById(authorId);
        found.setName(updatedAuthor.getName());
        found.setSurname(updatedAuthor.getSurname());
        found.setEmail(updatedAuthor.getEmail());
        found.setDateOfBirth(updatedAuthor.getDateOfBirth());
        found.setAvatar(updatedAuthor.getAvatar());

        return this.authorsRepository.save(found);
    }

    public void findByIdAndDelete(UUID authorId) {
        this.authorsRepository.delete(this.findById(authorId));
    }
}
