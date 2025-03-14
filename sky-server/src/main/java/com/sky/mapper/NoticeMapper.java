package com.sky.mapper;

import com.sky.dto.SentNoticeDTO;
import com.sky.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
    /**
     * 发送通知
     * @param notice
     */
    void createNotice(Notice notice);
    /**
     * 删除通知
     * @param noticeId
     */
    void deleteNotice(Integer noticeId);
    List<Notice> selectNoticesByUserId(Integer userId);
}
