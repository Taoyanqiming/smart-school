package com.sky.service;

import com.sky.dto.SentNoticeDTO;
import com.sky.entity.Notice;

import java.util.List;

public interface NoticeService {
    /**
     * 创建订单自动发送通知
     * @param notice
     */
    void createNotice(Notice notice);

    /**
     * 删除通知
     * @param noticeId
     */
    void deleteNotice(Integer noticeId);

    /**
     * 创建通知
     * @param sentNoticeDTO
     */
    void createSentNotice(SentNoticeDTO sentNoticeDTO);

    /**
     * 用户查询通知
     * @param userId
     * @return
     */
    List<Notice> selectNoice(Integer userId);
}
