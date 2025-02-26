package com.sky.controller.admin;

import com.sky.dto.NoticeDTO;
import com.sky.dto.ParkingLotsAddDTO;
import com.sky.dto.ReservationsPageQueryDTO;
import com.sky.dto.UpdateReservationsDTO;
import com.sky.entity.*;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.FeeService;
import com.sky.service.NoticeService;
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
@RequestMapping("/admin/reservation")
@Slf4j
@Api(tags = "预约记录接口")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ParkingService parkingService;
    @Autowired
    private FeeService feeService;
    @Autowired
    private NoticeService noticeService;
    /**
     * 分页根据条件展示记录（默认展示全部） 根据时间排序记录
     */
    @GetMapping("/page")
    @ApiOperation("查询车位预约记录")
    public Result<PageResult> getReservations(ReservationsPageQueryDTO reservationsPageQueryDTO) {
        PageResult pageResult = reservationService.getReservation(reservationsPageQueryDTO);
        return Result.success(pageResult);
    }

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
     * 检查reservation状态，如果预约时间过期，则修改车位状态空闲，设置预约为结束，生成支付订单
     */
    @Scheduled(fixedRate = 3600000) // 每小时执行一次
    public void updateReservationsStatus() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 查找所有active并且结束时间已经过去的预约
        List<Reservations> expiredReservations = reservationService.findExpiredReservations(now);

        // 遍历过期的预约记录是否同时生成支付订单
        for (Reservations reservation : expiredReservations) {
            // 为每个预约创建 UpdateReservationsDTO 对象
            UpdateReservationsDTO updateReservationsDTO = UpdateReservationsDTO.builder()
                    .parkingLotId(reservation.getParkingLotId())
                    .spaceId(reservation.getSpaceId())
                    .startTime(reservation.getStartTime())
                    //如果后续添加小程序延长时间，可以修改endTime也可以添加费用修改
                    .endTime(reservation.getEndTime())
                    .status("completed") // 将状态设置为已完成
                    .build();

            // 更新预约状态信息
            reservationService.updateReservation(updateReservationsDTO);

            // 获取相应停车位，并设置状态为空闲
            ParkingSpaces space = new ParkingSpaces();

            if (space != null) {
                space.setSpaceId(reservation.getSpaceId());
                space.setStatus("空闲");
                parkingService.updateParkingSpace(space);
            }
            //修改停车场车位+1
            //更新停车场状态，空闲车位-1
            ParkingLotsAddDTO p = new ParkingLotsAddDTO();
            p.setParkingLotId(reservation.getParkingLotId());
            p.setAccount(+1);
            parkingService.addLots(p);

            //生成支付订单
            Payments payments = Payments.builder()
                    .userId(reservation.getUserId())
                    .reservationId(reservation.getReservationId())
                    .amount(reservation.getTotalFee())
                    .status("未支付")
                    .paymentMethod("微信")
                    .build();
            feeService.createPay(payments);

            //发送通知信息
            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .userId(reservation.getUserId())
                    .title("账单支付通知")
                    .content("您有一笔待支付的账单")
                    .build();
            noticeService.createNotice(noticeDTO);
        }
    }


}
