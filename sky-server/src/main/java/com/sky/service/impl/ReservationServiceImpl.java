package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.ReservationsPageQueryDTO;
import com.sky.dto.UpdateReservationsDTO;
import com.sky.entity.Reservations;
import com.sky.mapper.ReservationsMapper;
import com.sky.result.PageResult;
import com.sky.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationsMapper reservationsMapper;
    /**
     * 根据条件展示记录（默认展示全部） 根据时间排序记录
     */
    @Override
    public PageResult getReservation(ReservationsPageQueryDTO reservationsPageQueryDTO){
        PageHelper.startPage(reservationsPageQueryDTO.getPage(), reservationsPageQueryDTO.getPageSize());
        Page<Reservations> page = reservationsMapper.selectReservation(reservationsPageQueryDTO);
        long total = page.getTotal();
        List<Reservations> records = page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 创建预约信息
     * @param reservation
     */
    @Override
     public void createReservation(Reservations reservation){
        reservationsMapper.createReservations(reservation);
   }


    /**
     * 修改预约信息
     * @param updateReservationsDTO
     */
    @Override
   public void updateReservation(UpdateReservationsDTO updateReservationsDTO){
       reservationsMapper.updateReservation(updateReservationsDTO);
   }

    /**
     * 找过期时间
     * @param now
     * @return
     */
    @Override
    public List<Reservations> findExpiredReservations(LocalDateTime now){
      return reservationsMapper.findTime(now);
    }


}


