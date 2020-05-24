package com.mountblue.canduitRestAPI.controller;

import com.mountblue.canduitRestAPI.DAO.CommentRepository;
import com.mountblue.canduitRestAPI.entity.Comments;
import com.mountblue.canduitRestAPI.entity.Post;
import com.mountblue.canduitRestAPI.services.CommentService;
import com.mountblue.canduitRestAPI.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    @PostMapping("/comments/{postId}")
    public Comments addComments(@RequestBody Comments comments, @PathVariable int postId) {

        Post post = postService.findById(postId);

        if (post == null) {
            throw new PostNotFoundException("post id not found : " + postId);
        }

        comments.setId(0);
        comments.setPost(post);
        commentService.save(comments);

        return comments;
    }

    @GetMapping("/comments/{postId}")
    public List<Comments> getComments(@PathVariable int postId){
        return commentRepository.findByPostId(postId);
     }

}
