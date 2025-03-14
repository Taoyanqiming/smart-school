package com.sky.mapper;

import com.sky.dto.*;
import com.sky.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    void insertPost(PostDTO postDTO);
    void insertComment(CommentDTO commentDTO);
    void insertLike(LikeDTO likeDTO);
    void insertFavorite(FavoriteDTO favoriteDTO);
    void deletePost(Integer postId);
    void deleteComment(Integer commentId);
    Posts getPostById(Integer postId);
    List<Comments> getCommentsByPostId(Integer postId);
    Likes getLikeByPostAndUser(LikeDTO likeDTO);
    Favorites getFavoriteByPostAndUser(FavoriteDTO favoriteDTO);
}