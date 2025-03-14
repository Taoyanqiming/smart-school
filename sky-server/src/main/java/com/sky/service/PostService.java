package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.result.Result;
import com.sky.vo.PostVO;

import java.util.List;

public interface PostService {
    Result<String> addPost(PostDTO postDTO);
    Result<String> addComment(CommentDTO commentDTO);
    Result<String> addLike(LikeDTO likeDTO);
    Result<String> addFavorite(FavoriteDTO favoriteDTO);
    Result<String> deletePost(Integer postId);
    Result<String> deleteComment(Integer commentId);
    Result<PostVO> getPostById(Integer postId);
    Result<List<Comments>> getCommentsByPostId(Integer postId);
}