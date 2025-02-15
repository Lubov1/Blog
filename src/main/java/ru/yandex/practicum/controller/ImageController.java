package ru.yandex.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.repositories.PostRepository;

import java.util.Optional;

@RestController
public class ImageController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent() && post.get().getImage() != null) {
            byte[] imageBytes = post.get().getImage();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/jpeg");

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}

