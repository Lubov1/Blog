package ru.yandex.practicum.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dao.Tag;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    Post getPostById(Long id);

//    @Query(value = "SELECT p.* FROM POSTS p " +
//            "JOIN TAGS t ON t.POST_ID = p.id " +
//            "WHERE t.TEXT = :tagName")
//    List<Post> findPostsByTagName(String tagName);
//
//    Page<Post> getAllPosts(Pageable pageable);
}