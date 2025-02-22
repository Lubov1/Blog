package ru.yandex.practicum.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.repositories.PostRepository;

@Configuration
@Import({PostRepository.class})
public class PostRepositoryConfig {
}
