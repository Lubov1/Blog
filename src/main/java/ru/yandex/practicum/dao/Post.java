package ru.yandex.practicum.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;


@Table("POSTS")
@Getter
@Setter
public class Post {


    @Id
    private Long id;
    private String title;
    private byte[] image;
    private int likes;
    private String content;

    public Post(String title, byte[] image, String content) {
        this.title = title;
        this.image = image;
        this.likes = 0;
        this.content = content;
    }

    public Post() {
    }
    public Post(String title, byte[] image, int likes, String content) {
        this.title = title;
        this.image = image;
        this.likes = likes;
        this.content = content;
    }
}
