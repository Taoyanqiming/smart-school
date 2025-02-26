package com.sky.service;

import com.sky.dto.ReservationsPageQueryDTO;
import com.sky.dto.UpdateReservationsDTO;
import com.sky.entity.Reservations;
import com.sky.result.PageResult;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    /**
     * 根据条件展示记录（默认展示全部） 根据时间排序记录
     */
    PageResult getReservation(ReservationsPageQueryDTO reservationsPageQueryDTO);

    /**
     * 创建预约信息
     * @param reservation
     */
    void createReservation(Reservations reservation);

    /**
     * 修改预约信息
     * @param updateReservationsDTO
     */
    void updateReservation(UpdateReservationsDTO updateReservationsDTO);

    /**
     * 找过期时间
     * @param now
     * @return
     */
    List<Reservations> findExpiredReservations(LocalDateTime now);
}
