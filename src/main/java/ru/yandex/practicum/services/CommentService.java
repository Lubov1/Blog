package ru.yandex.practicum.services;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.repositories.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getComments(Long id) {
        return commentRepository.findAllByPostId(id);
    }

    @Transactional
    public void deleteComment(Long id) throws NotFoundException {
        if (!commentRepository.existsById(id)) {
            throw new NotFoundException("Comment not found");
        }
        commentRepository.deleteById(id);
    }

    @Transactional
    public void updateComment(String text, Long id) throws NotFoundException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(()->new NotFoundException("comment not found"));
        comment.setText(text);
        commentRepository.save(comment);
    }

    @Transactional
    public void addComment(Long id, String commentText){
        commentRepository.save(new Comment(commentText, id));
    }

}
