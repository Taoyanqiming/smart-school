package com.sky.controller;

import com.sky.dto.*;
import com.sky.entity.Comments;
import com.sky.result.Result;
import com.sky.service.PostService;
import com.sky.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/add")
    public Result<String> addPost(@RequestBody PostDTO postDTO) {
        return postService.addPost(postDTO);
    }

    @PostMapping("/comment/add")
    public Result<String> addComment(@RequestBody CommentDTO commentDTO) {
        return postService.addComment(commentDTO);
    }

    @PostMapping("/like/add")
    public Result<String> addLike(@RequestBody LikeDTO likeDTO) {
        return postService.addLike(likeDTO);
    }

    @PostMapping("/favorite/add")
    public Result<String> addFavorite(@RequestBody FavoriteDTO favoriteDTO) {
        return postService.addFavorite(favoriteDTO);
    }

    @DeleteMapping("/delete/{postId}")
    public Result<String> deletePost(@PathVariable Integer postId) {
        return postService.deletePost(postId);
    }

    @DeleteMapping("/comment/delete/{commentId}")
    public Result<String> deleteComment(@PathVariable Integer commentId) {
        return postService.deleteComment(commentId);
    }

    @GetMapping("/{postId}")
    public Result<PostVO> getPostById(@PathVariable Integer postId) {
        return postService.getPostById(postId);
    }

    @GetMapping("/comments/{postId}")
    public Result<List<Comments>> getCommentsByPostId(@PathVariable Integer postId) {
        return postService.getCommentsByPostId(postId);
    }
}