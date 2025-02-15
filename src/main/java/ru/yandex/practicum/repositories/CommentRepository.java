package ru.yandex.practicum.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.yandex.practicum.dao.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);

}
