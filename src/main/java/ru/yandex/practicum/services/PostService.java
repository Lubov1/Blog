package ru.yandex.practicum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.PostDTORq;
import ru.yandex.practicum.dto.PostDTORs;
import ru.yandex.practicum.dto.TagDTOrq;
import ru.yandex.practicum.repositories.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class PostService {
    private final PostRepository postRepository;
    private CommentService commentService;
    private TagService tagService;


    public PostService(PostRepository postRepository, CommentService commentService, TagService tagService) {
        this.postRepository = postRepository;
        this.commentService = commentService;
        this.tagService = tagService;
    }

    public List<PostDTORs> getAllPosts() {
        return StreamSupport.stream(postRepository.findAll().spliterator(), true)
                .map(this::convertPostToDTOrs).toList();
    }

    public PostDTORs convertPostToDTOrs(Post post) {
        List<Comment> comments = commentService.getComments(post.getId());
        List<Tag> tags = tagService.getTags(post.getId());
        return new PostDTORs(post, comments, tags);
    }

    public Post getPostById(Long id) {
        return postRepository.getPostById(id);
    }

    @Transactional
    public void likePost(Long postId) {
        Post post = postRepository.getPostById(postId);
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
}
