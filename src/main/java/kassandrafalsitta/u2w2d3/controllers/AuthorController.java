package kassandrafalsitta.u2w2d3.controllers;
import kassandrafalsitta.u2w2d3.entities.Author;
import kassandrafalsitta.u2w2d3.entities.Blog;
import kassandrafalsitta.u2w2d3.exceptions.BadRequestException;
import kassandrafalsitta.u2w2d3.payloads.AuthorDTO;
import kassandrafalsitta.u2w2d3.payloads.AuthorRespDTO;
import kassandrafalsitta.u2w2d3.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @GetMapping
    public Page<Author> getAllAuthors(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy) {
        return this.authorService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorRespDTO createAuthor(@RequestBody @Validated AuthorDTO body, BindingResult validationResult) {
        if(validationResult.hasErrors())  {
            String messages = validationResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            return new AuthorRespDTO(this.authorService.saveAuthor(body).getId());
        }
    }

    @GetMapping("/{authorId}")
    public Author getAuthorById(@PathVariable UUID authorId) {
        return authorService.findById(authorId);
    }

    @PutMapping("/{authorId}")
    public Author findAuthorByIdAndUpdate(@PathVariable UUID authorId, @RequestBody Author body) {
        return authorService.findByIdAndUpdate(authorId, body);
    }

    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findAuthorByIdAndDelete(@PathVariable UUID authorId) {
        authorService.findByIdAndDelete(authorId);
    }

    @PostMapping("/{authorId}/avatar")
    public Author uploadCover(@PathVariable UUID authorId, @RequestParam("avatar") MultipartFile image) throws IOException {
        return  this.authorService.uploadImage(authorId,image);
    }
}
