package com.sky.controller.user;

import com.sky.dto.*;
import com.sky.entity.Comments;
import com.sky.entity.PostTags;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.PostService;
import com.sky.vo.PostVO;
import com.sky.vo.PostStatusVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 帖子相关操作的控制器
 */
@RestController
@RequestMapping("/post")
@Api(tags = "帖子相关接口")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 添加帖子
     * @param postDTO 帖子信息传输对象
     * @return 操作结果
     */
    @PostMapping("/add")
    @ApiOperation("新增帖子")
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
    @ApiOperation("新增评论")
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
    @ApiOperation("新增点赞")
    public Result<String> addLike(@RequestBody LikeDTO likeDTO) {
        postService.addLike(likeDTO);
        return Result.success("点赞成功");
    }
    /**
     * 评论点赞
     * @param likeCommentDTO
     */

    @PostMapping("/comment/like/add")
    @ApiOperation("新增评论点赞")
    public Result<String> addLike(@RequestBody LikeCommentDTO likeCommentDTO) {
        postService.addLikeComment(likeCommentDTO);
        return Result.success("点赞成功");
    }
//    /**
//     * 添加收藏
//     * @param favoriteDTO 收藏信息传输对象
//     * @return 操作结果
//     */
//    @PostMapping("/favorite/add")
//    @ApiOperation("新增收藏")
//    public Result<String> addFavorite(@RequestBody FavoriteDTO favoriteDTO) {
//        postService.addFavorite(favoriteDTO);
//        return Result.success("收藏成功");
//    }

//    /**
//     * 取消点赞
//     * @param likeDTO 点赞信息传输对象
//     * @return 操作结果
//     */
//    @PostMapping("/like/cancel")
//    @ApiOperation("取消点赞")
//    public Result<String> cancelLike(@RequestBody LikeDTO likeDTO) {
//        postService.cancelLike(likeDTO);
//        return Result.success("取消点赞成功");
//    }
//
//    /**
//     * 取消收藏
//     * @param favoriteDTO 收藏信息传输对象
//     * @return 操作结果
//     */
//    @PostMapping("/favorite/cancel")
//    @ApiOperation("取消收藏")
//    public Result<String> cancelFavorite(@RequestBody FavoriteDTO favoriteDTO) {
//        postService.cancelFavorite(favoriteDTO);
//        return Result.success("取消收藏成功");
//    }

//    /**
//     * 删除帖子（前提是属于用户发布）
//     * @param postId 帖子ID
//     * @return 操作结果
//     */
//    @DeleteMapping("/delete/{postId}")
//    @ApiOperation("删除帖子")
//    public Result<String> deletePost(@PathVariable Integer postId) {
//        postService.deletePost(postId);
//        return Result.success("删除帖子成功");
//    }

//    /**
//     * 删除评论（前提是属于用户发布）
//     * @param commentId 评论ID
//     * @return 操作结果
//     */
//    @DeleteMapping("/comment/delete/{commentId}")
//    @ApiOperation("删除评论")
//    public Result<String> deleteComment(@PathVariable Integer commentId) {
//        postService.deleteComment(commentId);
//        return Result.success("删除评论成功");
//    }

    /**
     * 根据帖子ID获取帖子信息
     * @param postId 帖子ID
     * @return 帖子信息视图对象
     */
    @GetMapping("/{postId}")
    @ApiOperation("根据帖子ID获取帖子信息")
    public Result<PostVO> getPostById(@PathVariable Integer postId) {
        PostVO postVO = postService.getPostById(postId);

        return Result.success(postVO);
    }

//    /**
//     * 根据帖子ID分页获取评论列表
//     * @param commentPageQueryDTO 评论分页查询数据传输对象
//     * @return 评论分页结果
//     */
//    @GetMapping("/comments")
//    @ApiOperation("根据帖子ID分页获取评论列表")
//    public Result<PageResult> getCommentsByPostId(@RequestBody CommentPageQueryDTO commentPageQueryDTO) {
//        PageResult pageResult = postService.getCommentsByPostId(commentPageQueryDTO);
//        return Result.success(pageResult);
//    }

//    /**
//     * 根据标签分页获取帖子列表
//     * @param postPageQueryDTO 帖子分页查询数据传输对象
//     * @return 帖子分页结果
//     */
//    @GetMapping("/listByTag")
//    @ApiOperation("根据标签分页获取帖子列表")
//    public Result<PageResult> getPostsByTagNames(@RequestBody PostPageQueryDTO postPageQueryDTO) {
//        PageResult pageResult = postService.getPostsByTagNames(postPageQueryDTO);
//        return Result.success(pageResult);
//    }

//    /**
//     * 首页分页展示所有帖子
//     * @param postPageQueryDTO 帖子分页查询数据传输对象
//     * @return 帖子分页结果
//     */
//    @GetMapping("/list")
//    @ApiOperation("首页分页展示所有帖子")
//    public Result<PageResult> getPostsByPage(@RequestBody PostPageQueryDTO postPageQueryDTO) {
//        PageResult pageResult = postService.getPostsByPage(postPageQueryDTO);
//        return Result.success(pageResult);
//    }

//    /**
//     * 为帖子添加标签
//     * @param postTags 帖子 - 标签关联对象
//     * @return 操作结果
//     */
//    @PostMapping("/addTag")
//    @ApiOperation("为帖子添加标签")
//    public Result<String> addPostTag(@RequestBody PostTags postTags) {
//        postService.addPostTag(postTags);
//        return Result.success("添加标签成功");
//    }


}