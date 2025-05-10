package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.mapper.PostMapper;
import com.sky.rabbitmq.MessageSender;
import com.sky.service.PostService;
import com.sky.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 帖子服务实现类，实现了 PostService 接口中的所有方法。
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MessageSender messageSender;

    /**
     * 添加新帖子。
     * @param postDTO 包含帖子信息的数据传输对象
     */
    @Override
    public void addPost(PostDTO postDTO) {
        postMapper.insertPost(postDTO);
    }

    /**
     * 为指定帖子添加评论。
     *
     * @param commentDTO 包含评论信息的数据传输对象
     */
    @Override
    @Transactional
    public void addComment(CommentDTO commentDTO) {
        //插入评论表
        postMapper.insertComment(commentDTO);
        //修改帖子评论数量
        postMapper.updateComment(commentDTO.getPostId(),+1);
        //处理消息自段
        Message message = Message.builder()
                .userId(commentDTO.getUserId())
                .postId(commentDTO.getPostId())
                .commentId(commentDTO.getCommentId())
                .messageType("comment")
                .messageContent("您的帖子被评论了")
                .build();
        //加入消息队列
        messageSender.sendMessage(message);
    }

    /**
     * 为指定帖子添加点赞。
     * @param likeDTO 包含点赞信息的数据传输对象
     */
    @Override
    @Transactional
    public void addLike(LikeDTO likeDTO) {
        //判断是否已经点赞
        Likes likes = postMapper.isLiked(likeDTO);
        if(likes!=null){
            postMapper.deleteLiked(likeDTO);
            //减少帖子点赞数量
            postMapper.updateLiked(likeDTO.getPostId(),-1);
        }else{
            //用户点赞更新数据库，更新缓存
             postMapper.insertLike(likeDTO);
            postMapper.updateLiked(likeDTO.getPostId(),1);
            //加入消息队列
            Message message = Message.builder()
                    .userId(likeDTO.getUserId())
                    .postId(likeDTO.getPostId())
                    .likeId(likeDTO.getLikeId())
                    .messageType("like_post")
                    .messageContent("您的帖子被点赞了")
                    .build();
            messageSender.sendMessage(message);
        }

    }

    /**
     * 评论点赞
     */
    @Override
    @Transactional
    public void addLikeComment(LikeCommentDTO likeCommentDTO) {
        //判断是否已经点赞
        Likes likes = postMapper.isCommentLiked(likeCommentDTO);
        if(likes!=null){
            postMapper.deleteCommentLiked(likeCommentDTO);
            //减少评论点赞数量
            postMapper.updateLiked(likeCommentDTO.getCommentId(),-1);
        }else{

            //用户点赞更新数据库，更新缓存
            postMapper.insertCommentLike(likeCommentDTO);
            postMapper.updateLiked(likeCommentDTO.getCommentId(),1);
            //加入消息队列
            Message message = Message.builder()
                    .userId(likeCommentDTO.getUserId())
                    .commentId(likeCommentDTO.getCommentId())
                    .likeCommentId(likeCommentDTO.getLikeCommentId())
                    .messageType("like_post")
                    .messageContent("您的评论被点赞了")
                    .build();
            messageSender.sendMessage(message);
        }

    }
//    /**
//     * 为指定帖子添加收藏。
//     *
//     * @param favoriteDTO 包含收藏信息的数据传输对象
//     */
//    @Override
//    public void addFavorite(FavoriteDTO favoriteDTO) {
//        //判断是否收藏
//        postMapper.insertFavorite(favoriteDTO);
//        postMapper.incrementFavoriteCount(favoriteDTO.getPostId());
//    }



//    /**
//     * 删除指定帖子。
//     *
//     * @param postId 要删除的帖子的 ID
//     */
//    @Override
//    public void deletePost(Integer postId) {
//        postMapper.deletePost(postId);
//    }

//    /**
//     * 删除指定评论。
//     *
//     * @param commentId 要删除的评论的 ID
//     */
//    @Override
//    public void deleteComment(Integer commentId) {
//        postMapper.deleteComment(commentId);
//    }



//    /**
//     * 根据帖子 ID 分页获取评论列表。
//     *
//     * @param commentPageQueryDTO 包含分页和查询条件的数据传输对象
//     * @return 包含评论列表的分页结果对象
//     */
//    @Override
//    public PageResult getCommentsByPostId(CommentPageQueryDTO commentPageQueryDTO) {
//        PageHelper.startPage(commentPageQueryDTO.getPage(), commentPageQueryDTO.getPageSize());
//        Page<Comments> page = postMapper.getCommentsByPostId(commentPageQueryDTO);
//        long total = page.getTotal();
//        List<Comments> records = page.getResult();
//        return new PageResult(total, records);
//    }

//    /**
//     * 分页获取所有帖子列表。
//     *
//     * @param postPageQueryDTO 包含分页和查询条件的数据传输对象
//     * @return 包含帖子列表的分页结果对象
//     */
//    @Override
//    public PageResult getPostsByPage(PostPageQueryDTO postPageQueryDTO) {
//        PageHelper.startPage(postPageQueryDTO.getPage(), postPageQueryDTO.getPageSize());
//        Page<PostVO> page = postMapper.getPostsByPage(postPageQueryDTO);
//        long total = page.getTotal();
//        List<PostVO> records = page.getResult();
//        return new PageResult(total, records);
//    }

//    /**
//     * 为帖子添加标签。
//     *
//     * @param postTags 包含帖子和标签关联信息的实体对象
//     */
//    @Override
//    public void addPostTag(PostTags postTags) {
//        postMapper.insertPostTag(postTags);
//    }

//    /**
//     * 根据标签名称分页获取帖子列表。
//     *
//     * @param postPageQueryDTO 包含分页和标签查询条件的数据传输对象
//     * @return 包含帖子列表的分页结果对象
//     */
//    @Override
//    public PageResult getPostsByTagNames(PostPageQueryDTO postPageQueryDTO) {
//        PageHelper.startPage(postPageQueryDTO.getPage(), postPageQueryDTO.getPageSize());
//        Page<PostVO> page = postMapper.getPostsByTagNames(postPageQueryDTO);
//        long total = page.getTotal();
//        List<PostVO> records = page.getResult();
//        return new PageResult(total, records);
//    }
    /**
     * 获取帖子信息
     * @param postId
     * @return
     */
    public PostVO getPostById(Integer postId){
        postMapper.incrementViewCount(postId);
        return postMapper.getPostById(postId);
    }


}