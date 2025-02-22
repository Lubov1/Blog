package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.services.PostService;
import ru.yandex.practicum.services.PostServiceConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringJUnitConfig(classes = {
        DataSourceConfiguration.class,
        WebConfiguration.class, PostServiceConfig.class})
@WebAppConfiguration
@ComponentScan(basePackages = "ru.yandex.practicum")
@TestPropertySource(locations = "classpath:test-application.properties")
class PostsControllerTest {

    @Autowired
    private AllPostsController allPostsController;
//    private PostController postController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //    @Mock
//    private TagService tagService;
//    @Mock
//    private CommentService commentService;
//    @Mock
//    private PostRepository postRepository;
//

    private MockMvc mockMvc;



    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Очистка и заполнение тестовых данных в базе
        jdbcTemplate.execute("DELETE FROM POSTS");
        jdbcTemplate.execute("insert into POSTS (title, content, likes, image) values ('Первый пост', 'Содержимое поста', 10, NULL)");
        jdbcTemplate.execute("insert into POSTS (title, content, likes, image) values ('Второй пост', 'Содержимое поста', 10, NULL)");


        jdbcTemplate.execute("insert into COMMENTS (TEXT, POST_ID) values ( 'comment1' , 1)");
        jdbcTemplate.execute("insert into COMMENTS (TEXT, POST_ID) values ( 'comment2' , 1)");
        jdbcTemplate.execute("insert into COMMENTS (TEXT, POST_ID) values ( 'comment3' , 2)");

        jdbcTemplate.execute("insert into TAGS (TEXT, POST_ID) values ( 'tag1' , 1)");
        jdbcTemplate.execute("insert into TAGS (TEXT, POST_ID) values ( 'tag2' , 1)");
        jdbcTemplate.execute("insert into TAGS (TEXT, POST_ID) values ( 'tag1' , 2)");


    }

    @Test
    void getUsers_shouldReturnHtmlWithUsers() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(xpath("//table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//table/tbody/tr[1]/td[2]").string("Иван"));
    }
}