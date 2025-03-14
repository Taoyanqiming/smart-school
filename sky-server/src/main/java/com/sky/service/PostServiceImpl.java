package com.sky.serviceImpl;

import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.mapper.PostMapper;
import com.sky.result.Result;
import com.sky.service.PostService;
import com.sky.vo.PostVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Override
    public Result<String> addPost(PostDTO postDTO) {
        postMapper.insertPost(postDTO);
        return Result.success("发帖成功");
    }

    @Override
    public Result<String> addComment(CommentDTO commentDTO) {
        postMapper.insertComment(commentDTO);
        return Result.success("评论成功");
    }

    @Override
    public Result<String> addLike(LikeDTO likeDTO) {
        Likes like = postMapper.getLikeByPostAndUser(likeDTO);
        if (like == null) {
            postMapper.insertLike(likeDTO);
            return Result.success("点赞成功");
        }
        return Result.error("你已经点过赞了");
    }

    @Override
    public Result<String> addFavorite(FavoriteDTO favoriteDTO) {
        Favorites favorite = postMapper.getFavoriteByPostAndUser(favoriteDTO);
        if (favorite == null) {
            postMapper.insertFavorite(favoriteDTO);
            return Result.success("收藏成功");
        }
        return Result.error("你已经收藏过了");
    }

    @Override
    public Result<String> deletePost(Integer postId) {
        postMapper.deletePost(postId);
        return Result.success("删帖成功");
    }

    @Override
    public Result<String> deleteComment(Integer commentId) {
        postMapper.deleteComment(commentId);
        return Result.success("删除回复成功");
    }

    @Override
    public Result<PostVO> getPostById(Integer postId) {
        Posts post = postMapper.getPostById(postId);
        if (post != null) {
            PostVO postVO = new PostVO();
            BeanUtils.copyProperties(post, postVO);
            return Result.success(postVO);
        }
        return Result.error("帖子不存在");
    }

    @Override
    public Result<List<Comments>> getCommentsByPostId(Integer postId) {
        List<Comments> comments = postMapper.getCommentsByPostId(postId);
        return Result.success(comments);
    }
}