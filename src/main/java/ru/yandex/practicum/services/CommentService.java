package ru.yandex.practicum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.repositories.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public List<Comment> getComments(Long id) {
        return commentRepository.findAllByPostId(id);
    }
}
