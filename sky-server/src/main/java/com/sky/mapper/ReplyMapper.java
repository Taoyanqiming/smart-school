package com.sky.mapper;

import com.sky.dto.RepliesDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReplyMapper {
    /**
     * 回复反馈
     * @param repliesDTO
     * @return
     */
    void insertReplies(RepliesDTO repliesDTO);

    /**
     * 根据 ID 删除回复信息
     * @param replyId 回复 ID
     */
    void deleteById(Integer replyId);

    /**
     * 用户查看自己反馈的回复信息
     * @param userId 用户 ID
     * @return 回复信息列表
     */
    List<Map<String, Object>> getMyReplies(Integer userId);
}
