package ru.yandex.practicum.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dao.Post;
import ru.yandex.practicum.dto.PostDTORq;
import ru.yandex.practicum.dto.PostDTORs;
import ru.yandex.practicum.dto.TagDTOrq;
import ru.yandex.practicum.services.CommentService;
import ru.yandex.practicum.services.PostService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) throws NotFoundException {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    @PostMapping("/{postId}/comments/delete/{commentId}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) throws NotFoundException {
        commentService.deleteComment(commentId);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        PostDTORs post = postService.getPostDTOById(id);
        model.addAttribute("post", post);
        return "post";
    }

    @PostMapping(value = "/edit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updatePost(@RequestParam("title") String title
            ,@RequestParam("content") String content,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             @RequestParam(value = "tags", required = false) List<String> tags,
                             @PathVariable Long id
    ) throws IOException, NotFoundException {
        List<TagDTOrq> tg = tags.stream().map(TagDTOrq::new).toList();
        byte[] im = new byte[0];
        if (image != null && !image.isEmpty()) {
            im = image.getBytes();
        }
        Long postId = postService.updatePost(new PostDTORq(title, content, im, tg), id);
        return "redirect:/post/"+postId;
    }

    @PostMapping(value = "/{postId}/comments/edit/{commentId}")
    public String updateComment(@PathVariable Long postId, @PathVariable Long commentId
    , @RequestParam String commentText) throws NotFoundException {
        commentService.updateComment(commentText, commentId);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/{id}/addcomment")
    public String addComment(@PathVariable Long id, @RequestParam String commentText) throws NotFoundException {
        commentService.addComment(id, commentText);
        return "redirect:/post/" + id;
    }
    @PostMapping("/like")
    public String likePost(@RequestParam Long postId) {
        postService.likePost(postId);
        return "redirect:/post/" + postId;
    }

}
