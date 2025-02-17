package ru.yandex.practicum.controller;


import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dto.PostDTORs;
import ru.yandex.practicum.services.PostService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class AllPostsController {

    private final PostService postService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public String getPosts(Model model, @RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String filter,
                           @RequestParam(defaultValue = "10") int pageSize) {
        Page<PostDTORs> posts;
        if (filter != null && !filter.isEmpty() && !filter.equals("null")) {
            posts = postService.getAllPostsByTag(filter, page, pageSize);
        } else {
            posts = postService.getAllPosts(page, pageSize);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("tag", filter);
        model.addAttribute("pageSize", pageSize);
        return "posts";
    }

    @ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
    @PostMapping("/like")
    public String likePost(@RequestParam Long postId) throws NotFoundException {
        postService.likePost(postId);
        return "redirect:/posts";
    }

    @ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
    @PostMapping(value ="/createPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createPost(@RequestParam("title") String title
            ,@RequestParam("content") String content,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             @RequestParam(value = "tags", required = false) List<String> tags
                             ) throws IOException {

        postService.createPost(tags, image, title, content);
        return "redirect:/posts";
    }

}
