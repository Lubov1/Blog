package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
