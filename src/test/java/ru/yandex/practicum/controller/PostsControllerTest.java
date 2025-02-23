package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import ru.yandex.practicum.repositories.PostRepository;
import ru.yandex.practicum.repositories.TagRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringJUnitConfig(classes = { WebConfiguration.class, ServicesConfig.class,
        DataSourceConfiguration.class, ThymeleafConfiguration.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
class PostsControllerTest {

    @Autowired
    private PostController postController;


    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private static List<PostDTORs> postsDtoRsDefault;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUpEach() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @BeforeAll
    static void setUp() {
        Post post1 = new Post("Первый пост", null, 10, "Содержимое поста");
        post1.setId(1L);
        Post post2 = new Post("Второй пост", null, 10, "Содержимое поста");
        post2.setId(2L);
        List<Comment> comments = Arrays.asList(new Comment(1L,"comment1", 1L), new Comment(2L, "comment2", 1L));
        List<Comment> comments2 = Arrays.asList(new Comment(3L,"comment3", 2L));
        List<Tag> tags = Arrays.asList(new Tag(1L,"tag1", 1L), new Tag(2L,"tag2", 1L));
        List<Tag> tags2 = Arrays.asList(new Tag(3L,"tag3", 2L));

        postsDtoRsDefault = Arrays.asList(new PostDTORs(post1, comments, tags), new PostDTORs(post2, comments2, tags2));
    }

    @Test
    void getAllPosts() throws Exception {
        assertEquals(2, postRepository.countPosts());
        var result = mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andReturn();


        List<PostDTORs> posts = ((Page<PostDTORs>) Objects.requireNonNull(result.getModelAndView()).getModel().get("posts")).stream().toList();
        assertEquals(2, posts.size());
        assertArrayEquals(postsDtoRsDefault.toArray(), posts.toArray());
        assertEquals(1, result.getModelAndView().getModel().get("totalPages"));
        assertEquals(0, result.getModelAndView().getModel().get("currentPage"));
        assertEquals(10, result.getModelAndView().getModel().get("pageSize"));
    }

    @Test
    void likePost() throws Exception {
        assertEquals(2, postRepository.countPosts());

        Long postId = 1L;
        mockMvc.perform(post("/posts/like").param("postId", String.valueOf(postId)))
                .andExpect(status().is3xxRedirection());

        assertEquals(11, postRepository.getPostById(postId).getLikes());
    }

    @Test
    void likePostFailed() throws Exception {
        assertEquals(2, postRepository.countPosts());

        Long postId = 100L;
        mockMvc.perform(post("/posts/like").param("postId", String.valueOf(postId)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPost() throws Exception {
        assertEquals(2, postRepository.countPosts());
        Long postId = 3L;
        Post post = new Post("post4", new byte[0], 0, "content4");
        post.setId(postId);

        mockMvc.perform(post("/posts/createPost").contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .param("title", "post4")
                        .param("image", "null")
                        .param("tags", "tag1","tag5")
                        .param("content", "content4"))
                .andExpect(status().isSeeOther());


        assertEquals(post, postRepository.findById(postId).get());
        assertArrayEquals(Arrays.asList("tag1","tag5").toArray(), tagRepository.findAllByPostId(postId).stream().map(Tag::getText).toArray());
        assertEquals(3, postRepository.countPosts());
    }
}