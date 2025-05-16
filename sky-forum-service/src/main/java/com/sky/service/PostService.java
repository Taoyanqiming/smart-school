package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Comments;
import com.sky.entity.Posts;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.PostVO;

import java.util.List;

public interface PostService {
    void addPost(PostDTO postDTO);

    void addComment(CommentDTO commentDTO);

    void addLike(LikeDTO likeDTO);
  //  void addFavorite(FavoriteDTO favoriteDTO);
    void addLikeComment(LikeCommentDTO likeCommentDTO);


//    void addFavorite(FavoriteDTO favoriteDTO);

//    void cancelLike(LikeDTO likeDTO);
//
//    void cancelFavorite(FavoriteDTO favoriteDTO);
//
//    void deletePost(Integer postId);
//
//    void deleteComment(Integer commentId);

    /**
     * 获取帖子信息
     * @param postId
     * @return
     */
   PostVO getPostById(Integer postId);

//    PageResult getCommentsByPostId(CommentPageQueryDTO commentPageQueryDTO);

//    PageResult getPostsByPage(PostPageQueryDTO postPageQueryDTO);
//
//    void addPostTag(PostTags postTags);
//
//    PageResult getPostsByTagNames(PostPageQueryDTO postPageQueryDTO);

 //   void incrementViewCount(Integer postId);





}