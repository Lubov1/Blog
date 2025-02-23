package ru.yandex.practicum.controller;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.configuration.ThymeleafConfiguration;
import ru.yandex.practicum.dto.PostDTORs;
import ru.yandex.practicum.repositories.PostRepository;
import ru.yandex.practicum.repositories.TagRepository;

import java.util.List;

@SpringJUnitConfig(classes = { WebConfiguration.class, ServicesConfig.class,
        DataSourceConfiguration.class, ThymeleafConfiguration.class})
@WebAppConfiguration
public class PostControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private static JdbcTemplate jdbcTemplate;

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

}
