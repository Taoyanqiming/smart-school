package com.sky.service.impl;

import com.sky.dto.SentNoticeDTO;
import com.sky.entity.Notice;
import com.sky.entity.User;
import com.sky.mapper.NoticeMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.NoticeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private UserMapper userMapper;
    /**
     * 自动发送通知
     * @param notice
     */
    @Override
   public void createNotice(Notice notice){
       noticeMapper.createNotice(notice);
   }

    /**
     * 删除通知
     * @param noticeId
     */
    @Override
   public void deleteNotice(Integer noticeId){
        noticeMapper.deleteNotice(noticeId);
    }

    /**
     * 创建通知
     * @param sentNoticeDTO
     */
    public void createSentNotice(SentNoticeDTO sentNoticeDTO){
        User user = userMapper.getByPhone(sentNoticeDTO.getPhoneNumber());
        Notice notice =new Notice();
        BeanUtils.copyProperties(sentNoticeDTO,notice);
        notice.setUserId(user.getUserId());

        noticeMapper.createNotice(notice);

    }
    /**
     * 用户查询通知
     * @param userId
     * @return
     */
    public List<Notice> selectNoice(Integer userId){
        List<Notice> notices =  noticeMapper.selectNoticesByUserId(userId);
        return notices;

    }
}
