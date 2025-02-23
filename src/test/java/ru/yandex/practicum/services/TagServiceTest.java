package ru.yandex.practicum.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.TagDTOrq;
import ru.yandex.practicum.repositories.TagRepository;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;


    @InjectMocks
    private TagService tagService;

    @BeforeEach
    void setUp() {
        Mockito.reset(tagRepository);
    }

    @Test
    void getTags() {
        var postId = 1L;
        var tags = Stream.of("one", "two").map(c->new Tag(c,postId)).toList();

        when(tagRepository.findAllByPostId(postId)).thenReturn(tags);

        tagService.getTags(postId);
        verify(tagRepository, times(1)).findAllByPostId(postId);
        assertArrayEquals(tags.toArray(), tagService.getTags(postId).toArray());
    }

    @Test
    void saveTag() {
        var postId = 1L;
        var tag = new Tag("one", postId);
        when(tagRepository.save(Mockito.any(Tag.class))).thenReturn(tag);
        tagService.saveTag(new TagDTOrq("one"), postId);
        verify(tagRepository,times(1)).save(eq(tag));
    }
}