//package com.sky.controller.user;
//
//import com.sky.dto.ReservationsPageQueryDTO;
//import com.sky.dto.UpdateReservationsDTO;
//import com.sky.entity.ParkingSpaces;
//import com.sky.entity.Reservations;
//import com.sky.result.PageResult;
//import com.sky.result.Result;
//import com.sky.service.ParkingService;
//import com.sky.service.ReservationService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RestController
//@RequestMapping("/user/reservation")
//@Slf4j
//@Api(tags = "预约记录接口")
//public class UserReservationController {
//    @Autowired
//    private ReservationService reservationService;
//    @Autowired
//    private ParkingService parkingService;
//    /**
//     * 分页根据条件展示记录（默认展示全部） 根据时间排序记录
//     */
//    @GetMapping("/page")
//    @ApiOperation("查询车位预约记录")
//    public Result<PageResult> getReservations(ReservationsPageQueryDTO reservationsPageQueryDTO) {
//        PageResult pageResult = reservationService.getReservation(reservationsPageQueryDTO);
//        return Result.success(pageResult);
//    }
//
//    /**
//     *修改预约
//     */
//    @PutMapping("/update")
//    @ApiOperation("修改车位预约信息")
//    public Result update(@RequestBody UpdateReservationsDTO updateReservationsDTO) {
//        reservationService.updateReservation(updateReservationsDTO);
//        return Result.success("修改成功");
//
//    }
//
//    /**
//     * 检查预约时间
//     */
//    @Scheduled(fixedRate = 3600000) // 每小时执行一次
//    public void updateReservationsStatus() {
//        // 获取当前时间
//        LocalDateTime now = LocalDateTime.now();
//
//        // 查找所有active并且结束时间已经过去的预约
//        List<Reservations> expiredReservations = reservationService.findExpiredReservations(now);
//
//        // 遍历过期的预约记录是否同时生成支付订单
//        for (Reservations reservation : expiredReservations) {
//            // 为每个预约创建 UpdateReservationsDTO 对象
//            UpdateReservationsDTO updateReservationsDTO = UpdateReservationsDTO.builder()
//                    .parkingLotId(reservation.getParkingLotId())
//                    .spaceId(reservation.getSpaceId())
//                    .startTime(reservation.getStartTime())
//                    //如果后续添加小程序延长时间，可以修改endTime也可以添加费用修改
//                    .endTime(reservation.getEndTime())
//                    .status("completed") // 将状态设置为已完成
//                    .build();
//
//            // 更新预约信息
//            reservationService.updateReservation(updateReservationsDTO);
//
//            // 获取相应停车位，并设置状态为空闲
//            ParkingSpaces space = parkingService.getSpacesById(reservation.getSpaceId());
//            if (space != null) {
//                space.setStatus("空闲"); // 将停车位状态设置为空闲
//                parkingService.updateParkingSpace(space);
//            }
//        }
//    }
//
//
//}
