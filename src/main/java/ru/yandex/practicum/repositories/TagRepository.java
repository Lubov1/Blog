package ru.yandex.practicum.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.yandex.practicum.dao.Tag;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {
    List<Tag> findAllByPostId(Long postId);
}
