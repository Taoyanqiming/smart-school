package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.FeedBacksDTO;
import com.sky.dto.FeedBacksPageQueryDTO;
import com.sky.dto.RepliesDTO;
import com.sky.entity.FeedBacks;
import com.sky.vo.FeedBacksVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {
    /**
     * 分页查看所有反馈信息
     */
    Page<FeedBacksVO> selectFeedbacks(FeedBacksPageQueryDTO feedBacksPageQueryDTO);

    /**
     * 删除反馈信息
     */
    void deleteFeedback(Integer feedbackId);
    /**
     * 用户创建反馈信息
     * @param feedBacksDTO
     */
    void createFeedbacks(FeedBacksDTO feedBacksDTO);
    /**
     * 根据 ID 删除反馈信息
     * @param feedbackId 反馈 ID
     */

    /**
     * 根据 ID 获取反馈信息
     * @param feedbackId 反馈 ID
     * @return 反馈信息
     */
    FeedBacks getById(Integer feedbackId);
}