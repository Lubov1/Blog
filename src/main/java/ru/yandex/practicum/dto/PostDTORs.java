package ru.yandex.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.Tag;

import java.util.List;

@Getter
@Setter
public class PostDTORs {
    private Long id;
    private String title;
    private byte[] image;
    private int likes;
    private String content;
    private List<Comment> comments;
    private List<Tag> tags;

    public PostDTORs(Post post, List<Comment> comments, List<Tag> tags) {
        this.title = post.getTitle();
        this.image = post.getImage();
        this.likes = post.getLikes();
        this.content = post.getContent();
        this.id = post.getId();
        this.comments = comments;
        this.tags = tags;
    }
}
