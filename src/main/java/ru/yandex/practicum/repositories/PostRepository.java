package ru.yandex.practicum.repositories;

import org.springframework.data.jdbc.repository.query.Query;
import ru.yandex.practicum.dao.Post;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    Post getPostById(Long id);

    @Query(value = "SELECT p.* FROM POSTS p " +
            "JOIN TAGS t ON t.POST_ID = p.id " +
            "WHERE t.TEXT = :tagName ORDER BY p.id LIMIT :limit OFFSET :offset")
    List<Post> getAllPostsByTagWithPagination(String tagName, int limit, int offset);

    @Query(value = "SELECT COUNT(*) FROM POSTS p " +
            "JOIN TAGS t ON t.POST_ID = p.id " +
            "WHERE t.TEXT = :tagName")
    long countPostsByTag(String tagName);


    @Query("SELECT * FROM posts ORDER BY posts.id LIMIT :limit OFFSET :offset")
    List<Post> getAllPostsWithPagination(int limit, int offset);

    @Query("SELECT COUNT(*) FROM posts")
    long countPosts();

    void deletePostById(Long id);
}