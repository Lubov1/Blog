package ru.yandex.practicum.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.configuration.ThymeleafConfiguration;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.repositories.PostRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = {
        DataSourceConfiguration.class}) // Указываем конфигурационный класс
public class JDBCTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;



    @Test
    public void testSaveAndFindUser() {
        Long postId = postRepository.save(new Post("title",
                null, "content")).getId();

        Post foundUser = postRepository.findById(postId).orElse(new Post());
        assertNotNull(foundUser);
        assertEquals("title", foundUser.getTitle());
    }


}