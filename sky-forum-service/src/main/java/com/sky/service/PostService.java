package com.sky.service;

import com.sky.dto.*;
import com.sky.dto.CommentDTO;
import com.sky.entity.Posts;
import com.sky.result.PageResult;
import com.sky.vo.PostVO;
import com.sky.vo.PostViewVO;

import java.util.List;

public interface PostService {
    void addPost(PostDTO postDTO);

    void addComment(CommentDTO commentDTO);

    void addLike(LikeDTO likeDTO);
    void addFavorite(FavoriteDTO favoriteDTO);
    void addLikeComment(LikeCommentDTO likeCommentDTO);

    void deletePost(Integer postId);

    void deleteComment(Integer commentId);

    /**
     * 获取帖子信息
     * @param postId
     * @return
     */
    Posts getPostById(Integer postId);

    PageResult getCommentsByPostId(CommentPageQueryDTO commentPageQueryDTO);

    PageResult getPostsByPage(PostPageQueryDTO postPageQueryDTO);

    List<PostViewVO> getTopPostsByViewToday();






}