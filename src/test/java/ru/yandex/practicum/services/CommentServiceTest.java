package ru.yandex.practicum.services;

import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.repositories.CommentRepository;

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
        var postId = 1L;
        var comments = Stream.of("one", "two").map(c->new Comment(c,postId)).toList();
        when(commentRepository.findAllByPostId(postId)).thenReturn(comments);

        commentService.getComments(postId);
        verify(commentRepository, times(1)).findAllByPostId(postId);
        assertArrayEquals(comments.toArray(), commentService.getComments(postId).toArray());
    }

    @Test
    void deleteCommentFail() {
        var postId = 1L;
        when(commentRepository.existsById(postId)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> commentService.deleteComment(postId));
    }

    @Test
    void deleteComment() throws NotFoundException {
        var postId = 1L;
        when(commentRepository.existsById(postId)).thenReturn(true);
        commentService.deleteComment(postId);
        verify(commentRepository, times(1)).deleteById(postId);
    }

    @Test
    void updateComment() throws NotFoundException {
        var postId = 1L;

        var comment = new Comment("one", postId);
        when(commentRepository.findById(postId)).thenReturn(Optional.of(comment));
        commentService.updateComment("two", postId);

        var updcomment = new Comment("two", postId);
        verify(commentRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).save(eq(updcomment));
    }

    @Test
    void addComment() {
        var postId = 1L;
        var comment = new Comment("one", postId);
        when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        commentService.addComment(postId, "one");
        verify(commentRepository,times(1)).save(eq(comment));
        assertEquals("one", comment.getText());
    }
}