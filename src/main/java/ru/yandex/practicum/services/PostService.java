package ru.yandex.practicum.services;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.PostDTORs;
import ru.yandex.practicum.dto.TagDTOrq;
import ru.yandex.practicum.repositories.PostRepository;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final TagService tagService;


    public PostService(PostRepository postRepository, CommentService commentService, TagService tagService) {
        this.postRepository = postRepository;
        this.commentService = commentService;
        this.tagService = tagService;
    }

    public Page<PostDTORs> getAllPosts(int page, int size) {
        int offset = page * size;
        List<Post> posts = postRepository.getAllPostsWithPagination(size, offset);
        long total = postRepository.countPosts();
        return new PageImpl<>(posts.stream().map(this::convertPostToDTOrs).toList(),
                PageRequest.of(page, size), total);
    }

    public Page<PostDTORs> getAllPostsByTag(String tag, int page, int size) {
        int offset = page * size;
        List<Post> posts = postRepository.getAllPostsByTagWithPagination(tag, size, offset);
        long total = postRepository.countPostsByTag(tag);
        return new PageImpl<>(posts.stream().map(this::convertPostToDTOrs)
                .toList(), PageRequest.of(page, size), total);
    }


    public PostDTORs convertPostToDTOrs(Post post) {
        List<Comment> comments = commentService.getComments(post.getId());
        List<Tag> tags = tagService.getTags(post.getId());
        return new PostDTORs(post, comments, tags);
    }

    //todo existsbyid delete
    public Post getPostById(Long id) throws NotFoundException {
        return postRepository.findById(id).orElseThrow(()->new NotFoundException("post doesn't exist"));
    }

    public PostDTORs getPostDTOById(Long id) throws NotFoundException {
        return convertPostToDTOrs(getPostById(id));
    }

    @Transactional
    public void deletePost(Long id) throws NotFoundException {
        if (!postRepository.existsById(id)) {
            throw new NotFoundException("Post with id " + id + " does not exist");
        }
        postRepository.deleteById(id);
    }


    @Transactional
    public void likePost(Long postId) throws NotFoundException {
        Post post = getPostById(postId);
        int likes = post.getLikes();
        post.setLikes(++likes);
        postRepository.save(post);
    }

    List<TagDTOrq> tagsConverter(List<String> tags) {
        return tags.stream().map(TagDTOrq::new).toList();
    }

    byte[] imageConverter(MultipartFile image) throws IOException {
        byte[] im = new byte[0];
        if (image != null && !image.isEmpty()) {
            im = image.getBytes();
        }
        return im;
    }

    //todo logging
    @Transactional
    public void createPost(List<String> tags, MultipartFile image,String title, String content) throws IOException {
        Long postId = postRepository.save(new Post(title,
                imageConverter(image), content)).getId();
        for (TagDTOrq tag : tagsConverter(tags)) {
            tagService.saveTag(tag, postId);
        }
    }

    @Transactional
    public Long updatePost(List<String> tags, MultipartFile image, String title, String content, Long id) throws NotFoundException, IOException {
        var post = getPostById(id);
        post.setTitle(title);
        post.setImage(imageConverter(image));
        post.setContent(content);
        postRepository.save(post);
        for (TagDTOrq tag : tagsConverter(tags)) {
            tagService.saveTag(tag, id);
        }
        return id;
    }
}
