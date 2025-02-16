package ru.yandex.practicum.controller;

import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    private final CommentService commentService;
    private final PostService postService;

    public PostController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) throws NotFoundException {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{postId}/comments/delete/{commentId}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) throws NotFoundException {
        commentService.deleteComment(commentId);
        return "redirect:/post/" + postId;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public String getPost(@PathVariable Long id, Model model) throws NotFoundException {
        PostDTORs post = postService.getPostDTOById(id);
        model.addAttribute("post", post);
        return "post";
    }

    @ResponseStatus(HttpStatus.OK)
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

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/{postId}/comments/edit/{commentId}")
    public String updateComment(@PathVariable Long postId, @PathVariable Long commentId
    , @RequestParam String commentText) throws NotFoundException {
        commentService.updateComment(commentText, commentId);
        return "redirect:/post/" + postId;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/addcomment")
    public String addComment(@PathVariable Long id, @RequestParam String commentText) {
        commentService.addComment(id, commentText);
        return "redirect:/post/" + id;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/like")
    public String likePost(@RequestParam Long postId) throws NotFoundException {
        postService.likePost(postId);
        return "redirect:/post/" + postId;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
