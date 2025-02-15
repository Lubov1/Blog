package ru.yandex.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dao.Tag;
import ru.yandex.practicum.dto.PostDTORq;
import ru.yandex.practicum.dto.TagDTOrq;
import ru.yandex.practicum.services.PostService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private PostService postService;

    @GetMapping
    public String getPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts";
    }

    @PostMapping("/like/{postId}")
    public String likePost(@PathVariable("postId") Long postId) {
        postService.likePost(postId);
        return "redirect:/posts";
    }

    @PostMapping(value ="/createPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createPost(@RequestParam("title") String title
            ,@RequestParam("content") String content,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             @RequestParam(value = "tags", required = false) List<String> tags
                             ) throws IOException
                             {
        List<TagDTOrq> tg = tags.stream().map(TagDTOrq::new).toList();
        byte[] im = new byte[0];
        if (image != null && !image.isEmpty()) {
            im = image.getBytes();
        }
        postService.createPost(new PostDTORq(title, content, im, tg));
//           postService.createPost(new PostDTORq("title", "content", im, new ArrayList<>()));
        return "redirect:/posts";
    }
}
