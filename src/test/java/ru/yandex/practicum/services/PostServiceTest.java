package ru.yandex.practicum.services;


import javassist.NotFoundException;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.TagDTOrq;
import ru.yandex.practicum.repositories.PostRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PostServiceConfig.class)
class PostServiceTest {
    @Autowired
    CommentService commentService;

    @Autowired
    TagService tagService;
    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void setUp() {
        Mockito.reset(postRepository);
    }

    @Test
    void createPost() throws IOException {
        Mockito.when(postRepository.save(Mockito.any(Post.class))).thenReturn(new Post());
        postService.createPost(new ArrayList<>(), null, "title", "content");
        Mockito.verify(postRepository, Mockito.times(1)).save(Mockito.any(Post.class));
        Mockito.verify(tagService, Mockito.times(0)).saveTag(Mockito.any(TagDTOrq.class), Mockito.anyLong());
    }

    @Test
    void updatePostSuccess() throws IOException, NotFoundException {

        Post post = new Post();
        post.setId(1L);
        val tags = Arrays.asList("one", "two", "three");


        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        postService.updatePost(tags, null, "title", "content", 1L);
        assertEquals(post.getTitle(), "title");
        assertEquals(post.getContent(), "content");
        assertEquals(post.getId(), 1L);
        assertEquals(post.getImage().length, 0);
        Mockito.verify(postRepository, Mockito.times(1)).save(Mockito.any(Post.class));
        Mockito.verify(tagService, Mockito.times(3)).saveTag(Mockito.any(TagDTOrq.class), Mockito.anyLong());
    }

    @Test
    void likePost() throws NotFoundException {

        Post post = new Post();
        post.setId(1L);
        post.setLikes(5);


        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        postService.likePost(1L);

        assertEquals(post.getLikes(), 6);
        Mockito.verify(postRepository, Mockito.times(1)).save(Mockito.any(Post.class));
        Mockito.verify(tagService, Mockito.times(0)).saveTag(Mockito.any(TagDTOrq.class), Mockito.anyLong());
    }

    @Test
    void updatePostFail() {

        Post post = new Post();
        post.setId(1L);
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> postService.updatePost(new ArrayList<>(), null, "title", "content", 1L));
    }

    @Test
    void imageConverter() throws IOException {
        val image = postService.imageConverter(null);
        assertNotNull(image);
        assertEquals(image.length, 0);
    }

    @Test
    void convertPostToDTOrs() {
        val post = new Post();
        val postId = 1L;
        post.setId(postId);
        List<Tag> tags = List.of(new Tag("one", postId));
        List<Comment> comments = Arrays.asList(new Comment("one", postId),
        new Comment("two", postId));

        when(commentService.getComments(postId)).thenReturn(comments);
        when(tagService.getTags(postId)).thenReturn(tags);

        val postDTORs = postService.convertPostToDTOrs(post);
        assertArrayEquals(tags.toArray(),postDTORs.getTags().toArray());
        assertArrayEquals(comments.toArray(),postDTORs.getComments().toArray());
        verify(commentService, times(1)).getComments(postId);
        verify(tagService, times(1)).getTags(postId);
    }

}