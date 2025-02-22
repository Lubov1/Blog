package ru.yandex.practicum.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.repositories.TagRepository;

@Configuration
@Import({TagRepository.class})
public class TagRepositoryConfig {
}
