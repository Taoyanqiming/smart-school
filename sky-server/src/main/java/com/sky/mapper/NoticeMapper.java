package com.sky.mapper;

import com.sky.dto.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {
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
