package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import ru.yandex.practicum.dao.Tag;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PostDTORq {
    private String title;
    private String content;
    private byte[] image;

    private List<TagDTOrq> tags;
}
