package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.ReservationsPageQueryDTO;
import com.sky.dto.UpdateReservationsDTO;
import com.sky.entity.Reservations;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReservationsMapper {
    /**
     * 分页查询
     * @param reservationsPageQueryDTO
     * @return
     */
    Page<Reservations> selectReservation(ReservationsPageQueryDTO reservationsPageQueryDTO);
    /**
     * 创建预约信息
     * @param reservation
     */
    void createReservations(Reservations reservation);
    /**
     * 修改预约信息
     * @param updateReservationsDTO
     */
    void updateReservation(UpdateReservationsDTO updateReservationsDTO);

    /**
     * 找过期时间
     * @param now
     */
    List<Reservations> findTime(@Param("now") LocalDateTime now);

}
