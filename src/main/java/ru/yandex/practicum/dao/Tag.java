package ru.yandex.practicum.dao;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("tags")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    @Id
    private Long id;
    private String text;

    @Column("post_id")
    private Long postId;

    public Tag(String text, Long postId) {
        this.text = text;
        this.postId = postId;
    }
}
