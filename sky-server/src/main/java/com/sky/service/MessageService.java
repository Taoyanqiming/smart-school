package com.sky.service;

import com.sky.dto.FeedBacksDTO;
import com.sky.dto.FeedBacksPageQueryDTO;
import com.sky.dto.RepliesDTO;
import com.sky.entity.Replies;
import com.sky.result.PageResult;

import java.util.List;
import java.util.Map;

public interface MessageService {
     /**
      * 分页查看所有反馈信息
      */
     PageResult getMessageList(FeedBacksPageQueryDTO feedBacksPageQueryDTO);

     /**
      * 管理员回复反馈
      * @param repliesDTO
      * @return
      */
     void reply(RepliesDTO repliesDTO);
     /**
      * 删除反馈信息
      */
     void deleteFeedbacks(Integer feedbackId);
     /**
      * 删除回复信息
      */
     void deleteReplies(Integer replyId);

     /**
      * 用户创建反馈信息
      * @param feedBacksDTO
      */
     void createFeedback(FeedBacksDTO feedBacksDTO);

//     /**
//      * 验证反馈是否属于该用户
//      * @param feedbackId 反馈 ID
//      * @param userId 用户 ID
//      * @return 是否属于该用户
//      */
//     boolean isFeedbackOwner(Integer feedbackId, Integer userId);


}
