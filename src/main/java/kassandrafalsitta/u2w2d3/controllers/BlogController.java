package kassandrafalsitta.u2w2d3.controllers;

import kassandrafalsitta.u2w2d3.entities.Blog;
import kassandrafalsitta.u2w2d3.entities.BlogPayload;
import kassandrafalsitta.u2w2d3.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    private Blog createBlog(@RequestBody BlogPayload body) {
        return blogService.saveBlog(body);
    }

    @GetMapping("/{blogId}")
    private Blog getBlogById(@PathVariable UUID blogId){
        return blogService.findById(blogId);
    }

    @PutMapping("/{blogId}")
    private Blog findBlogByIdAndUpdate(@PathVariable UUID blogId, @RequestBody BlogPayload body) {
        return blogService.findByIdAndUpdate(blogId, body);
    }

    @DeleteMapping("/{blogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void findBlogByIdAndDelete(@PathVariable UUID blogId){
        blogService.findByIdAndDelete(blogId);
    }
}
