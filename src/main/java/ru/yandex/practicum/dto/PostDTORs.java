package ru.yandex.practicum.dto;

import lombok.Data;
import ru.yandex.practicum.dao.Comment;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.Tag;

import java.util.Base64;
import java.util.List;

@Data
public class PostDTORs {
    private Long id;
    private String title;
    private String image;
    private int likes;
    private String content;
    private List<Comment> comments;
    private List<Tag> tags;
    private int commentsCount;

    public PostDTORs(Post post, List<Comment> comments, List<Tag> tags) {
        this.title = post.getTitle();
        if (post.getImage() != null) {
            this.image = Base64.getEncoder().encodeToString(post.getImage());
        }
        this.likes = post.getLikes();
        this.content = post.getContent();
        this.id = post.getId();
        this.comments = comments;
        this.commentsCount = comments.size();
        this.tags = tags;
    }
}
