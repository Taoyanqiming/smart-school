package com.sky.controller;

import com.sky.dto.*;
import com.sky.dto.CommentDTO;
import com.sky.entity.Favorites;
import com.sky.entity.Likes;
import com.sky.entity.Posts;
import com.sky.mapper.PostMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.PostService;
import com.sky.vo.PostDetailsVO;
import com.sky.vo.PostViewVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子相关操作的控制器
 */
@RestController
@RequestMapping("/forum")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostMapper postMapper;

    /**
     * 添加帖子
     * @param postDTO 帖子信息传输对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result<String> addPost(@RequestBody PostDTO postDTO) {
        postService.addPost(postDTO);
        return Result.success("发布成功");
    }

    /**
     * 添加评论
     * @param commentDTO 评论信息传输对象
     * @return 操作结果
     */
    @PostMapping("/comment/add")
    public Result<String> addComment(@RequestBody CommentDTO commentDTO) {
        postService.addComment(commentDTO);
        return Result.success("评论成功");
    }

    /**
     * 添加点赞
     * @param likeDTO 点赞信息传输对象
     * @return 操作结果
     */
    @PostMapping("/like/add")
    public Result<String> addLike(@RequestBody LikeDTO likeDTO) {
        postService.addLike(likeDTO);
        return Result.success("点赞成功");
    }

    /**
     * 评论点赞
     * @param likeCommentDTO
     */
    @PostMapping("/comment/like/add")
    public Result<String> addLike(@RequestBody LikeCommentDTO likeCommentDTO) {
        postService.addLikeComment(likeCommentDTO);
        return Result.success("点赞成功");
    }

    /**
     * 添加收藏
     * @param favoriteDTO 收藏信息传输对象
     * @return 操作结果
     */
    @PostMapping("/favorite/add")
    public Result<String> addFavorite(@RequestBody FavoriteDTO favoriteDTO) {
        postService.addFavorite(favoriteDTO);
        return Result.success("收藏成功");
    }

    /**
     * 删除帖子（前提是属于用户发布）
     * @param postId 帖子ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{postId}")
    public Result<String> deletePost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return Result.success("删除帖子成功");
    }

    /**
     * 删除评论（前提是属于用户发布）
     * @param commentId 评论ID
     * @return 操作结果
     */
    @DeleteMapping("/comment/delete/{commentId}")
    public Result<String> deleteComment(@PathVariable Integer commentId) {
        postService.deleteComment(commentId);
        return Result.success("删除评论成功");
    }

    /**
     * 根据帖子ID获取帖子信息
     * @param postId 帖子ID
     * @return 帖子信息视图对象
     */
    @GetMapping("/{postId}")
    public Result<PostDetailsVO> getPostById(@PathVariable Integer postId, HttpServletRequest request) {
        Posts posts = postService.getPostById(postId);

        LikeDTO likeDTO = LikeDTO.builder()
                .postId(postId)
                .userId(Integer.valueOf( request.getHeader("X-User-Id")))
                .build();
        Likes Liked = postMapper.isLiked(likeDTO);
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        BeanUtils.copyProperties(likeDTO,favoriteDTO);
        Favorites favorites = postMapper.isFavor(favoriteDTO);
        PostDetailsVO postDetailsVO = new PostDetailsVO();
        BeanUtils.copyProperties(posts,postDetailsVO);
        if(Liked != null){
            postDetailsVO.setUserIsLiked(true);
        }
        if(favorites != null){
            postDetailsVO.setUserIsFavorited(true);
        }
        return Result.success(postDetailsVO);
    }

    /**
     * 根据帖子ID分页获取评论列表
     * @param commentPageQueryDTO 评论分页查询数据传输对象
     * @return 评论分页结果
     */
    @PostMapping("/comments")
    public Result<PageResult> getCommentsByPostId(@RequestBody CommentPageQueryDTO commentPageQueryDTO) {
        PageResult pageResult = postService.getCommentsByPostId(commentPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 首页分页展示所有帖子
     * @param postPageQueryDTO 帖子分页查询数据传输对象
     * @return 帖子分页结果
     */
    @PostMapping("/list")
    public Result<PageResult> getPostsByPage(@RequestBody PostPageQueryDTO postPageQueryDTO) {
        PageResult pageResult = postService.getPostsByPage(postPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 获取当天 view 字段最多的前 10 个帖子
     * @return 返回包含帖子列表的分页结果
     */
    @GetMapping("/top/view")
    public Result<List<PostViewVO>> getTopPostsByViewToday() {
        List<PostViewVO> topPosts = postService.getTopPostsByViewToday();
        return Result.success(topPosts);
    }


}