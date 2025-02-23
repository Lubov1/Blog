package ru.yandex.practicum.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.repositories.CommentRepository;
import ru.yandex.practicum.repositories.PostRepository;
import ru.yandex.practicum.repositories.TagRepository;
import ru.yandex.practicum.services.CommentService;
import ru.yandex.practicum.services.PostService;
import ru.yandex.practicum.services.TagService;

@Configuration
public class ServicesConfig {
    @Bean
    public PostService postService(PostRepository postRepository, CommentService commentService,
                                   TagService tagService) {
        return new PostService(postRepository, commentService, tagService);
    }

    @Bean public CommentService commentService(CommentRepository commentRepository) {
        return new CommentService(commentRepository);
    }

    @Bean public TagService tagService(TagRepository tagRepository) {
        return new TagService(tagRepository);
    }
}
