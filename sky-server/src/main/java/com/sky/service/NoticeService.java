package com.sky.service;

import com.sky.dto.NoticeDTO;

public interface NoticeService {
    /**
     * 发送通知
     * @param noticeDTO
     */
    void createNotice(NoticeDTO noticeDTO);

    /**
     * 删除通知
     * @param noticeId
     */
    void deleteNotice(Integer noticeId);
}
