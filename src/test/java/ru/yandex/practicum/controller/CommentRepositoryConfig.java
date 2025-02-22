package ru.yandex.practicum.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.repositories.CommentRepository;

@Configuration
@Import({CommentRepository.class})
public class CommentRepositoryConfig {
}
