package ru.yandex.practicum.services;


import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PostService.class)
class PostServiceTest {
    @MockitoBean
    CommentService commentService;

    @MockitoBean
    TagService tagService;
    @Autowired
    PostService postService;

    @MockitoBean
    PostRepository postRepository;

    @BeforeEach
    void setUp() {
       reset(postRepository);
    }

    @Test
    void createPost() throws IOException {
        when(postRepository.save(Mockito.any(Post.class))).thenReturn(new Post());
        postService.createPost(new ArrayList<>(), null, "title", "content");
        verify(postRepository,times(1)).save(Mockito.eq(new Post(null,"title",new byte[0], 0, "content")));
        verify(tagService,times(0)).saveTag(Mockito.any(TagDTOrq.class),anyLong());
    }

    @Test
    void updatePostSuccess() throws IOException, NotFoundException {
        Post post = new Post();
        Long postId = 1L;
        post.setId(1L);
        var tags = Arrays.asList("one", "two", "three");
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.updatePost(tags, null, "title", "content", 1L);

        verify(postRepository,times(1)).save(Mockito.eq(new Post(1L,"title",new byte[0], 0, "content")));
        verify(tagService,times(3)).saveTag(Mockito.any(TagDTOrq.class),anyLong());
    }

    @Test
    void likePost() throws NotFoundException {

        Post post = new Post(1L, "title", null, 5,"content");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        postService.likePost(1L);


        verify(postRepository,times(1)).save(Mockito.eq(new Post(1L,"title", null, 6, "content")));
        verify(tagService,times(0)).saveTag(Mockito.any(TagDTOrq.class),anyLong());
    }

    @Test
    void updatePostFail() {

        Post post = new Post();
        post.setId(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> postService.updatePost(new ArrayList<>(), null, "title", "content", 1L));
    }

    @Test
    void imageConverter() throws IOException {
        var image = postService.imageConverter(null);
        assertNotNull(image);
        assertEquals(image.length, 0);
    }

    @Test
    void convertPostToDTOrs() {
        var post = new Post();
        var postId = 1L;
        post.setId(postId);
        List<Tag> tags = List.of(new Tag("one", postId));
        List<Comment> comments = Arrays.asList(new Comment("one", postId),
        new Comment("two", postId));

        when(commentService.getComments(postId)).thenReturn(comments);
        when(tagService.getTags(postId)).thenReturn(tags);

        var postDTORs = postService.convertPostToDTOrs(post);
        assertArrayEquals(tags.toArray(),postDTORs.getTags().toArray());
        assertArrayEquals(comments.toArray(),postDTORs.getComments().toArray());
        verify(commentService, times(1)).getComments(postId);
        verify(tagService, times(1)).getTags(postId);
    }

}