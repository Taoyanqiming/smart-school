package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.*;
import com.sky.entity.*;

import com.github.pagehelper.Page;
import com.sky.enumeration.OperationType;
import com.sky.vo.CommentsVO;
import com.sky.vo.PostVO;
import com.sky.vo.PostViewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PostMapper {
    /**
     * 发帖
     * @param postDTO
     */
    @AutoFill(OperationType.INSERT)
    void insertPost(PostDTO postDTO);
    /**
     * 发布评论
     * @param comments
     */
    @AutoFill(OperationType.INSERT)
    void insertComment(Comments comments);
    /**
     * 帖子点赞
     * @param likes
     */
    @AutoFill(OperationType.INSERT_CREATE_ONLY)
    void insertLike(Likes likes);
    /**
     * 帖子收藏
     * @param favorites
     */
    @AutoFill(OperationType.INSERT_CREATE_ONLY)
    void insertFavorite(Favorites favorites);
    /**
     * 评论点赞
     */
    @AutoFill(OperationType.INSERT_CREATE_ONLY)
    void insertCommentLike(CommentLikes commentLikes);
    /**
     * 取消点赞
     * @param likeDTO
     */
    void deleteLiked(LikeDTO likeDTO);
    void deleteCommentLiked(LikeCommentDTO likeCommentDTO);
    void deletePost(Integer postId);
    void deleteComment(Integer commentId);
    void deleteFavor(FavoriteDTO favoriteDTO);
    /**
     * 查询是否存在
     */

    Likes isLiked(LikeDTO likeDTO);
    CommentLikes isCommentLiked(LikeCommentDTO likeCommentDTO);
    Favorites isFavor(FavoriteDTO favoriteDTO);
    /**
     * 修改帖子点赞数量
     */
    void updateLiked(@Param("postId")Integer postId, @Param("account")Integer account);
    void updateCommnetLiked(@Param("commentId")Integer commentId, @Param("account")Integer account);
    /**
     * 修改帖子评论数量
     * @param postId
     * @param account
     */
    void updateComment(@Param("postId")Integer postId, @Param("account")Integer account);
    void incrementViewCount(Integer postId);
    /**
     * 修改帖子收藏数量
     * @param postId
     */
    void incrementFavoriteCount(@Param("postId")Integer postId,@Param("account")Integer account);

    /**
     * 查询
     * @param postId
     * @return
     */
    Posts getPostById(Integer postId);
    Page<CommentsVO> pageQueryByPostId(CommentPageQueryDTO queryDTO);
    Page<PostVO> getPostsByPage(PostPageQueryDTO postPageQueryDTO);

    /**
     * 查询一个评论的全部信息
     * @param commentId
     * @return
     */
    Comments selectComment(Integer commentId);

    // Java接口
    List<PostViewVO> getTopPostsByViewToday(LocalDateTime todayStart, LocalDateTime todayEnd);
}