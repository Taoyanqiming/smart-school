package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.FeedBacksDTO;
import com.sky.dto.FeedBacksPageQueryDTO;
import com.sky.dto.RepliesDTO;
import com.sky.entity.Replies;
import com.sky.mapper.MessageMapper;
import com.sky.mapper.ReplyMapper;
import com.sky.result.PageResult;
import com.sky.service.MessageService;
import com.sky.vo.FeedBacksVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private ReplyMapper replyMapper;

    /**
     * 分页查询反馈
     * @param feedBacksPageQueryDTO
     * @return
     */
    @Override
    public PageResult getMessageList(FeedBacksPageQueryDTO feedBacksPageQueryDTO){
        PageHelper.startPage(feedBacksPageQueryDTO.getPage(),feedBacksPageQueryDTO.getPageSize());
        Page<FeedBacksVO> page = messageMapper.selectFeedbacks(feedBacksPageQueryDTO);
        long total = page.getTotal();
        List<FeedBacksVO> records = page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 回复反馈
     * @param repliesDTO
     * @return
     */
    @Override
    public void reply(RepliesDTO repliesDTO){
        replyMapper.insertReplies(repliesDTO);
    }

    /**
     * 删除反馈信息
     */
    @Override
   public void deleteFeedbacks(Integer feedbackId){
        messageMapper.deleteFeedback(feedbackId);
    }

    /**
     * 删除回复信息
     */
    public void deleteReplies(Integer replyId,Integer feedbackId){
        replyMapper.deleteReplies(replyId,feedbackId);
    }

    /**
     * 用户创建反馈信息
     * @param feedBacksDTO
     */
    @Override
   public void createFeedback(FeedBacksDTO feedBacksDTO){
       messageMapper.createFeedbacks(feedBacksDTO);
   }

    /**
     * 查询回复信息
     * @param feedbackId
     * @return
     */
    @Override
    public List<Replies> getReplies( Integer feedbackId){
        return replyMapper.getReply(feedbackId);
    }

}