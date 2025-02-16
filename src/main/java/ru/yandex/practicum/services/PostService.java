package ru.yandex.practicum.services;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.PostDTORq;
import ru.yandex.practicum.dto.PostDTORs;
import ru.yandex.practicum.dto.TagDTOrq;
import ru.yandex.practicum.repositories.PostRepository;

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
        return new PageImpl<>(posts.stream().map(this::convertPostToDTOrs).toList(), PageRequest.of(page, size), total);
    }

    public Page<PostDTORs> getAllPostsByTag(String tag, int page, int size) {
        int offset = page * size;
        List<Post> posts = postRepository.getAllPostsByTagWithPagination(tag, size, offset);
        long total = postRepository.countPostsByTag(tag);
        System.out.println("total" + total);
        log.debug("total" + total);
        return new PageImpl<>(posts.stream().map(this::convertPostToDTOrs).toList(), PageRequest.of(page, size), total);
    }


    public PostDTORs convertPostToDTOrs(Post post) {
        List<Comment> comments = commentService.getComments(post.getId());
        List<Tag> tags = tagService.getTags(post.getId());
        return new PostDTORs(post, comments, tags);
    }

    public Post getPostById(Long id) throws NotFoundException {
        if (!postRepository.existsById(id)) {
            throw new NotFoundException("post doesn't exist");
        }
        return postRepository.getPostById(id);
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

    @Transactional
    public void createPost(PostDTORq post) {
        Long postId = postRepository.save(new Post(post.getTitle(),
                post.getImage(), post.getContent())).getId();
        for (TagDTOrq tag : post.getTags()) {
            tagService.saveTag(tag, postId);
        }
    }

    @Transactional
    public Long updatePost(PostDTORq postDTORq, Long id) throws NotFoundException {
        Post post = getPostById(id);
        post.setTitle(postDTORq.getTitle());
        post.setImage(postDTORq.getImage());
        post.setContent(postDTORq.getContent());
        postRepository.save(post);
        for (TagDTOrq tag : postDTORq.getTags()) {
            tagService.saveTag(tag, id);
        }
        return id;
    }
}
