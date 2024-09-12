package kassandrafalsitta.u2w2d3.services;

import kassandrafalsitta.u2w2d3.entities.Author;
import kassandrafalsitta.u2w2d3.entities.Blog;
import kassandrafalsitta.u2w2d3.entities.BlogPayload;
import kassandrafalsitta.u2w2d3.exceptions.BadRequestException;
import kassandrafalsitta.u2w2d3.exceptions.NotFoundException;
import kassandrafalsitta.u2w2d3.repositories.AuthorsRepository;
import kassandrafalsitta.u2w2d3.repositories.BlogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BlogService {
    @Autowired
    private BlogsRepository blogsRepository;
    @Autowired
    private AuthorService authorsService;

    public Page<Blog> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.blogsRepository.findAll(pageable);
    }

    public Blog saveBlog(BlogPayload body) {
        Optional<Blog> titleBlog = blogsRepository.findByTitle(body.getTitle());
        Optional<Blog> contentBlog = blogsRepository.findByContent(body.getContent());
        if (titleBlog.isPresent() && contentBlog.isPresent()) {
            throw new BadRequestException("Il titolo " + body.getTitle() + " e il contenuto " + body.getContent() + " sono già in uso!");
        }
        Author author = authorsService.findById(body.getAuthorId());
        Blog blog = new Blog(body.getCategory(), body.getTitle(), body.getCover(), body.getContent(), body.getReadingTime(), author);
        return this.blogsRepository.save(blog);
    }

    public Blog findById(UUID blogId) {
        return this.blogsRepository.findById(blogId).orElseThrow(() -> new NotFoundException(blogId));
    }

    public Blog findByIdAndUpdate(UUID blogId, BlogPayload updatedBlog) {
        Blog found = findById(blogId);
        found.setCategory(updatedBlog.getCategory());
        found.setTitle(updatedBlog.getTitle());
        found.setReadingTime(updatedBlog.getReadingTime());
        found.setCover(updatedBlog.getCover());
        found.setContent(updatedBlog.getContent());

        Author author = authorsService.findById(updatedBlog.getAuthorId());
        found.setAuthorId(author);
        return this.blogsRepository.save(found);
    }

    public void findByIdAndDelete(UUID blogId) {
        this.blogsRepository.delete(this.findById(blogId));
    }
}
