package ru.yandex.practicum.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.configuration.ThymeleafConfiguration;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.PostDTORs;
import ru.yandex.practicum.repositories.CommentRepository;
import ru.yandex.practicum.repositories.PostRepository;
import ru.yandex.practicum.repositories.TagRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringJUnitConfig(classes = { WebConfiguration.class, ServicesConfig.class,
        DataSourceConfiguration.class, ThymeleafConfiguration.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
@Transactional
public class PostControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;
    @BeforeEach
    void setUpEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    void likePost() throws Exception {

        Long postId = 1L;
        mockMvc.perform(post("/post/like").param("postId", String.valueOf(postId)))
                .andExpect(status().isSeeOther());

        assertEquals(11, postRepository.getPostById(postId).getLikes());
    }


    @Test
    void likePostFailed() throws Exception {

        Long postId = 100L;
        mockMvc.perform(post("/post/like").param("postId", String.valueOf(postId)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePost() throws Exception {
        Long postId = 1L;
        mockMvc.perform(post("/post/delete/{postId}", postId))
                .andExpect(status().isSeeOther());

        assertFalse(postRepository.existsById(postId));
    }

    @Test
    void deletePostFailed() throws Exception {
        Long postId = 100L;
        mockMvc.perform(post("/post/delete/{postId}", postId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteComment() throws Exception {
        Long postId = 1L;
        mockMvc.perform(post("/post/{postId}/comments/delete/{commentId}", 1, 1))
                .andExpect(status().isSeeOther());

        assertEquals(1, commentRepository.findAllByPostId(postId).size());
    }

    @Test
    void getPost() throws Exception {
        Long postId = 1L;
        Post postDao = new Post("Первый пост", null, 10, "Содержимое поста");
        postDao.setId(1L);
        List<Comment> comments = Arrays.asList(new Comment(1L,"comment1", 1L), new Comment(2L, "comment2", 1L));
        List<Tag> tags = Arrays.asList(new Tag(1L,"tag1", 1L), new Tag(2L,"tag2", 1L));

        PostDTORs postDto = new PostDTORs(postDao, comments, tags);
        var result = mockMvc.perform(get("/post/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andReturn();

        PostDTORs post = ((PostDTORs) Objects.requireNonNull(result.getModelAndView()).getModel().get("post"));
        assertEquals(postDto, post);
    }

    @Test
    void addComment() throws Exception {
        Long postId = 1L;
        mockMvc.perform(post("/post/{id}/addcomment", postId)
                        .param("commentText", "commentAdded"))
        .andExpect(status().isSeeOther());

        assertEquals(3, commentRepository.findAllByPostId(postId).size());
        assertEquals(1, commentRepository.findAllByPostId(postId).stream().map(Comment::getText)
                .filter("commentAdded"::equals).count());
    }

    @Test
    void updateComment() throws Exception {
        Long postId = 1L;
        mockMvc.perform(post("/post/{postId}/comments/edit/{commentId}", postId, 1)
                        .param("commentText", "commentUpdated"))
                .andExpect(status().isSeeOther());

        assertEquals(2, commentRepository.findAllByPostId(postId).size());
        assertEquals(1, commentRepository.findAllByPostId(postId).stream().map(Comment::getText)
                .filter("commentUpdated"::equals).count());
    }

    @Test
    void updatePost() throws Exception {
        Long postId = 1L;
        Post post = new Post("post4", new byte[0], 10, "content4");
        post.setId(postId);

        mockMvc.perform(post("/post/edit/{id}", postId)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .param("title", "post4")
                        .param("image", "null")
                        .param("tags", "tag1","tag5")
                        .param("content", "content4"))
                .andExpect(status().isSeeOther());


        assertEquals(post, postRepository.findById(postId).get());
        assertArrayEquals(Arrays.asList("tag1", "tag2", "tag1","tag5").toArray(), tagRepository.findAllByPostId(postId)
                .stream().map(Tag::getText).toArray());
        assertEquals(2, postRepository.countPosts());
    }

}
