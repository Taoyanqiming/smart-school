package com.sky.service.impl;

import com.sky.dto.NoticeDTO;
import com.sky.mapper.NoticeMapper;
import com.sky.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    /**
     * 发送通知
     * @param noticeDTO
     */
    @Override
   public void createNotice(NoticeDTO noticeDTO){
       noticeMapper.createNotice(noticeDTO);
   }

    /**
     * 删除通知
     * @param noticeId
     */
    @Override
   public void deleteNotice(Integer noticeId){
        noticeMapper.deleteNotice(noticeId);
    }
}
