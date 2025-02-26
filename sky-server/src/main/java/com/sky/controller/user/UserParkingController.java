//package com.sky.controller.user;
//
//import com.sky.dto.ParkingSpacesQueryDTO;
//import com.sky.dto.UpdateLotsDTO;
//import com.sky.dto.UpdateSpacesDTO;
//import com.sky.entity.ParkingLots;
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
//import org.springframework.web.bind.annotation.*;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//
//@RestController
//@RequestMapping("/user/parking")
//@Slf4j
//@Api(tags = "停车位相关接口")
//public class UserParkingController {
//
//    @Autowired
//    private ParkingService parkingService;
//
//    @Autowired
//    private ReservationService reservationService;
//
//        /**
//         * 分页展示停车位信息，根据条件筛选（如果有）
//         * @param parkingSpacesQueryDTO
//         * @return
//         */
//        @GetMapping("/page/spaces/{parkingLotId}")
//        @ApiOperation("展示停车位信息")
//        public Result<PageResult> getParkingSpaces( ParkingSpacesQueryDTO parkingSpacesQueryDTO) {
//            log.info("分页查询停车位信息", parkingSpacesQueryDTO);
//
//            PageResult pageResult = parkingService.getParkingSpaces(parkingSpacesQueryDTO);
//
//            // 返回查询结果
//            return Result.success(pageResult);
//        }
//
//    /**
//     * 预约车位
//     * @param updateSpacesDTO
//     * @return
//     */
//    @ApiOperation("预约车位")
//    @PutMapping("/parking-spaces/reserve")
//    public Result<String> updateParkingSpaces(@RequestBody UpdateSpacesDTO updateSpacesDTO) {
//        // 校验停车位是否可用
//        ParkingSpaces parkingSpace = parkingService.getSpacesById(updateSpacesDTO.getSpaceId());
//        if ("空闲".equals(parkingSpace.getStatus())) {
//            // 更新停车位状态为 "占用"
//            parkingSpace.setStatus("占用");
//            parkingSpace.setUserId(updateSpacesDTO.getUserId());
//            parkingSpace.setReservedTime(LocalDateTime.now());
//            parkingSpace.setOccupiedTime(updateSpacesDTO.getStartTime());
//
//            // 保存停车位状态更新
//            parkingService.updateParkingSpace(parkingSpace);
//
//            // 计算时间差（小时）
//            Duration duration = Duration.between(updateSpacesDTO.getStartTime(), updateSpacesDTO.getEndTime());
//            long hours = duration.toHours();
//
//            // 将 pricePerHour 转换为数值类型
//            double pricePerHour = Double.parseDouble(parkingSpace.getPricePerHour());
//
//            // 计算总费用
//            double totalFee = pricePerHour * hours;
//
//            // 创建预约记录
//            Reservations reservation = new Reservations();
//            reservation.setUserId(updateSpacesDTO.getUserId());
//            reservation.setParkingLotId(parkingSpace.getParkingLotId());
//            reservation.setSpaceId(parkingSpace.getSpaceId());
//            reservation.setStartTime(updateSpacesDTO.getStartTime());
//            reservation.setEndTime(updateSpacesDTO.getEndTime());
//            reservation.setTotalFee(totalFee);
//            reservation.setStatus("active");
//
//            // 插入预约记录到 Reservations 表
//            reservationService.createReservation(reservation);
//
//            return Result.success("车位已成功预定");
//        } else {
//            // 如果车位不可用，返回错误信息
//            return Result.error("车位不可用，无法预定");
//        }
//    }
//
//
//    /**
//     * 根据id查询停车位全部信息
//     */
//    @ApiOperation("根据id查询停车位")
//    @GetMapping("/{spaceId}")
//    public Result<ParkingSpaces>getSpacesById(@PathVariable Integer spaceId) {
//        ParkingSpaces parkingSpaces = parkingService.getSpacesById(spaceId);
//        return Result.success(parkingSpaces);
//    }
//
//    /**
//     *根据id获取停车场信息
//     */
//    @ApiOperation("根据id查询停车场")
//    @GetMapping("/parkingLots/{parkingLotId}")
//    public  Result<ParkingLots> getParkingLots(@PathVariable Integer parkingLotId){
//        ParkingLots parkingLots = parkingService.getLotInfo(parkingLotId);
//        return Result.success(parkingLots);
//    }
//
//
//
//
//
//}
