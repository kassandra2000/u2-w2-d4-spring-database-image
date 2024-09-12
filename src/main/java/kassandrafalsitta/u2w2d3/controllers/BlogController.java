package kassandrafalsitta.u2w2d3.controllers;

import kassandrafalsitta.u2w2d3.entities.Blog;
import kassandrafalsitta.u2w2d3.exceptions.BadRequestException;
import kassandrafalsitta.u2w2d3.payloads.BlogDTO;
import kassandrafalsitta.u2w2d3.payloads.BlogRespDTO;
import kassandrafalsitta.u2w2d3.services.BlogService;
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
@RequestMapping("/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping
    private Page<Blog> getAllBlogs(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "id") String sortBy) {
        return blogService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private BlogRespDTO createBlog(@RequestBody @Validated BlogDTO body, BindingResult validationResult) {

        if(validationResult.hasErrors())  {
            String messages = validationResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));

            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            // Se non ci sono stati salviamo l'utente

            return new BlogRespDTO(this.blogService.saveBlog(body).getId());
        }
    }

    @GetMapping("/{blogId}")
    private Blog getBlogById(@PathVariable UUID blogId){
        return blogService.findById(blogId);
    }

    @PutMapping("/{blogId}")
    private Blog findBlogByIdAndUpdate(@PathVariable UUID blogId, @RequestBody BlogDTO body)  {
        return blogService.findByIdAndUpdate(blogId, body);
    }

    @DeleteMapping("/{blogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void findBlogByIdAndDelete(@PathVariable UUID blogId){
        blogService.findByIdAndDelete(blogId);
    }

    @PostMapping("/{blogId}/cover")
    public Blog uploadCover(@PathVariable UUID blogId,@RequestParam("cover") MultipartFile image) throws IOException {
        return  this.blogService.uploadImage(blogId,image);
    }
}
