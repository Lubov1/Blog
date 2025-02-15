package ru.yandex.practicum.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("TAGS")
@Getter
@Setter
public class Tag {

    @Id
    private Long id;
    private String text;

    @Column("POST_ID")
    private Long postId;

    public Tag(String text, Long postId) {
        this.text = text;
        this.postId = postId;
    }
}
