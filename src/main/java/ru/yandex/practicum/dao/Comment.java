package ru.yandex.practicum.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table("comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    public Comment(String text, Long postId) {
        this.text = text;
        this.postId = postId;
    }

    @Id
    private Long id;
    private String text;

    @Column("post_id")
    private Long postId;
}
