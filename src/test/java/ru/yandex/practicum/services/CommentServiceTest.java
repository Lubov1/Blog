package ru.yandex.practicum.services;

import javassist.NotFoundException;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.repositories.CommentRepository;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import ru.yandex.practicum.dao.Comment;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;


    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        Mockito.reset(commentRepository);
    }

    @Test
    void getComments() {
        val postId = 1L;
        val comments = Stream.of("one", "two").map(c->new Comment(c,postId)).toList();
        when(commentRepository.findAllByPostId(postId)).thenReturn(comments);

        commentService.getComments(postId);
        verify(commentRepository, times(1)).findAllByPostId(postId);
        assertArrayEquals(comments.toArray(), commentService.getComments(postId).toArray());
    }

    @Test
    void deleteCommentFail() {
        val postId = 1L;
        when(commentRepository.existsById(postId)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> commentService.deleteComment(postId));
    }

    @Test
    void deleteComment() throws NotFoundException {
        val postId = 1L;
        when(commentRepository.existsById(postId)).thenReturn(true);
        commentService.deleteComment(postId);
        verify(commentRepository, times(1)).deleteById(postId);
    }

    @Test
    void updateComment() throws NotFoundException {
        val postId = 1L;

        val comment = new Comment("one", postId);
        when(commentRepository.findById(postId)).thenReturn(Optional.of(comment));
        commentService.updateComment("two", postId);
        assertEquals("two", comment.getText());
        verify(commentRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addComment() {
        val postId = 1L;
        val comment = new Comment("one", postId);
        when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        commentService.addComment(postId, "one");
        verify(commentRepository,times(1)).save(Mockito.any(Comment.class));
        assertEquals("one", comment.getText());
    }
}