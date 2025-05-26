package com.sky.mapper;

import com.sky.dto.*;
import com.sky.entity.Comments;
import com.sky.entity.Likes;
import com.sky.entity.Posts;

import com.github.pagehelper.Page;
import com.sky.vo.PostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    /**
     * 发帖
     * @param postDTO
     */
    void insertPost(PostDTO postDTO);
    /**
     * 发布评论
     * @param commentDTO
     */
    void insertComment(CommentDTO commentDTO);
    /**
     * 帖子点赞
     * @param likeDTO
     */
    void insertLike(LikeDTO likeDTO);
    /**
     * 帖子收藏
     * @param favoriteDTO
     */
   void insertFavorite(FavoriteDTO favoriteDTO);
    /**
     * 评论点赞
     */
    void insertCommentLike(LikeCommentDTO likeCommentDTO);
    /**
     * 取消点赞
     * @param likeDTO
     */
   void deleteLiked(LikeDTO likeDTO);
    void deleteCommentLiked(LikeCommentDTO likeCommentDTO);
    /**
     * 查询是否存在
     */
    PostVO getPostById(Integer postId);
    Likes isLiked(LikeDTO likeDTO);
    Likes isCommentLiked(LikeCommentDTO likeCommentDTO);
    /**
     * 修改值
     */
    void updateLiked(Integer postId, Integer account);
    void updateComment(Integer postId, Integer account);
    void incrementViewCount(Integer postId);
    void incrementFavoriteCount(Integer postId);
    void deletePost(Integer postId);
    void deleteComment(Integer commentId);
    Page<Comments> getCommentsByPostId(CommentPageQueryDTO commentPageQueryDTO);
    Page<PostVO> getPostsByPage(PostPageQueryDTO postPageQueryDTO);


}