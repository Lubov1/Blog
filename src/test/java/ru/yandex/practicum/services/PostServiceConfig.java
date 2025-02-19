package ru.yandex.practicum.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.repositories.PostRepository;

import static org.mockito.Mockito.mock;

@Configuration
public class PostServiceConfig {
    @Bean
    public PostService postService(CommentService commentService,
                                   TagService tagService, PostRepository postRepository) {
        return new PostService(postRepository, commentService, tagService);
    }

    @Bean
    public CommentService commentService() {
        return mock(CommentService.class);
    }
    @Bean
    public TagService tagService() {
        return mock(TagService.class);
    }
    @Bean
    public PostRepository postRepository() {
        return mock(PostRepository.class);
    }
}
