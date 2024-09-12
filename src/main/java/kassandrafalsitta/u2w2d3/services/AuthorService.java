package kassandrafalsitta.u2w2d3.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kassandrafalsitta.u2w2d3.entities.Author;
import kassandrafalsitta.u2w2d3.entities.Blog;
import kassandrafalsitta.u2w2d3.exceptions.BadRequestException;
import kassandrafalsitta.u2w2d3.exceptions.NotFoundException;
import kassandrafalsitta.u2w2d3.payloads.AuthorDTO;
import kassandrafalsitta.u2w2d3.repositories.AuthorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class AuthorService {
    @Autowired
    private AuthorsRepository authorsRepository;

    @Autowired
    private Cloudinary cloudinaryUploader;


    public Page<Author> findAll(int page, int size, String sortBy) {
        if(page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.authorsRepository.findAll(pageable);
    }

    public Author saveAuthor(AuthorDTO body) {
        this.authorsRepository.findByEmail(body.email()).ifPresent(
                author -> {
                    throw new BadRequestException("L'email " + body.email() + " è già in uso!");
                }
        );
        Author newAuthor = new Author(body.name(),
                body.surname(),
                body.email(),
                body.dateOfBirth(),
                "https://ui-avatars.com/api/?name="+body.name()+"+"+body.surname());

        return this.authorsRepository.save(newAuthor);
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

    public Author uploadImage(UUID authorId, MultipartFile file) throws IOException {
        Author found = findById(authorId);
        String avatar = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setAvatar(avatar);
        return this.authorsRepository.save(found);
    }
}
