package kassandrafalsitta.u2w2d3.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kassandrafalsitta.u2w2d3.entities.Author;
import kassandrafalsitta.u2w2d3.entities.Blog;
import kassandrafalsitta.u2w2d3.exceptions.BadRequestException;
import kassandrafalsitta.u2w2d3.exceptions.NotFoundException;
import kassandrafalsitta.u2w2d3.payloads.BlogDTO;
import kassandrafalsitta.u2w2d3.repositories.BlogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class BlogService {
    @Autowired
    private BlogsRepository blogsRepository;
    @Autowired
    private AuthorService authorsService;

    @Autowired
    private Cloudinary cloudinaryUploader;

    public Page<Blog> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.blogsRepository.findAll(pageable);
    }

    public Blog saveBlog(BlogDTO body) {
        Optional<Blog> titleBlog = blogsRepository.findByTitle(body.title());
        Optional<Blog> contentBlog = blogsRepository.findByContent(body.content());
        if (titleBlog.isPresent() && contentBlog.isPresent()) {
            throw new BadRequestException("Il titolo " + body.title() + " e il contenuto " + body.content() + " sono già in uso!");
        }
        Author author = authorsService.findById(body.authorId());
        Blog blog = new Blog(body.category(), body.title(), body.content(), body.readingTime(), author, "https://ui-avatars.com/api/?name=" + body.title() + "+" + body.category());
        return this.blogsRepository.save(blog);
    }

    public Blog findById(UUID blogId) {
        return this.blogsRepository.findById(blogId).orElseThrow(() -> new NotFoundException(blogId));
    }

    public Blog findByIdAndUpdate(UUID blogId, BlogDTO updatedBlog) {
        Blog found = findById(blogId);
        found.setCategory(updatedBlog.category());
        found.setTitle(updatedBlog.title());
        found.setReadingTime(updatedBlog.readingTime());
        found.setContent(updatedBlog.content());
        Author author = authorsService.findById(updatedBlog.authorId());
        found.setAuthorId(author);
        return this.blogsRepository.save(found);
    }

    public void findByIdAndDelete(UUID blogId) {
        this.blogsRepository.delete(this.findById(blogId));
    }

    public Blog uploadImage(UUID blogId, MultipartFile file) throws IOException {
        Blog found = findById(blogId);
        String cover = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setCover(cover);
        return this.blogsRepository.save(found);
    }
}
