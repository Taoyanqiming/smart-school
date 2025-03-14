package com.sky.controller.user;

import com.sky.dto.ReservationsPageQueryDTO;
import com.sky.dto.UpdateReservationsDTO;
import com.sky.entity.ParkingSpaces;
import com.sky.entity.Reservations;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.ParkingService;
import com.sky.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user/reservation")
@Slf4j
@Api(tags = "用户预约记录接口")
public class UserReservationController {
    @Autowired
    private ReservationService reservationService;

    /**
     *修改预约
     */
    @PutMapping("/update")
    @ApiOperation("修改车位预约信息")
    public Result update(@RequestBody UpdateReservationsDTO updateReservationsDTO) {
        reservationService.updateReservation(updateReservationsDTO);
        return Result.success("修改成功");

    }
    /**
     * 分页根据条件展示记录（默认展示全部） 根据时间排序记录
     */
    @GetMapping("/page")
    @ApiOperation("查询车位预约记录")
    public Result<PageResult> getReservations(ReservationsPageQueryDTO reservationsPageQueryDTO) {
        PageResult pageResult = reservationService.getReservation(reservationsPageQueryDTO);
        return Result.success(pageResult);
    }

}
