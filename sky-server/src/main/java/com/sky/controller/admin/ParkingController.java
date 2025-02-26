package com.sky.controller.admin;

import com.sky.dto.ParkingLotsAddDTO;
import com.sky.dto.ParkingSpacesQueryDTO;
import com.sky.dto.UpdateLotsDTO;
import com.sky.dto.ReservationDTO;
import com.sky.entity.ParkingLots;
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
import org.springframework.web.bind.annotation.*;
import java.time.Duration;

@RestController
@RequestMapping("/admin")
@Slf4j
@Api(tags = "停车位相关接口")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private ReservationService reservationService;

        /**
         * 分页展示停车位信息，根据条件筛选（如果有）
         * @param parkingSpacesQueryDTO
         * @return
         */
        @GetMapping("/page/spaces")
        @ApiOperation("展示停车位信息")
        public Result<PageResult> getParkingSpaces( ParkingSpacesQueryDTO parkingSpacesQueryDTO) {
            log.info("分页查询停车位信息", parkingSpacesQueryDTO);

            PageResult pageResult = parkingService.getParkingSpaces(parkingSpacesQueryDTO);

            // 返回查询结果
            return Result.success(pageResult);
        }

    /**
     * 预约车位
     * @param reservationDTO
     * @return
     */
    @ApiOperation("预约车位")
    @PutMapping("/parking-spaces/reserve")
    public Result<String> updateParkingSpaces(@RequestBody ReservationDTO reservationDTO) {
        // 校验停车位是否可用
        ParkingSpaces parkingSpace = parkingService.getSpacesById(reservationDTO.getSpaceId(), reservationDTO.getParkingLotId());
        // 检查 parkingSpace 是否为空
        if (parkingSpace == null) {
            return Result.error("未找到对应的停车位，spaceId: " + reservationDTO.getSpaceId() + ", parkingLotId: " + reservationDTO.getParkingLotId()); // 或者根据业务需求返回合适的错误结果
        }
        if ("空闲".equals(parkingSpace.getStatus())) {
            // 更新停车位状态为 "占用"
            parkingSpace.setStatus("占用");

            // 保存停车位状态更新
            parkingService.updateParkingSpace(parkingSpace);
            //更新停车场状态，空闲车位-1
            ParkingLotsAddDTO p = new ParkingLotsAddDTO();
            p.setParkingLotId(parkingSpace.getParkingLotId());
            p.setAccount(-1);
            parkingService.addLots(p);
            // 计算时间差（小时）
            Duration duration = Duration.between(reservationDTO.getStartTime(), reservationDTO.getEndTime());
            // 获取总分钟数
            long totalMinutes = duration.toMinutes();

            // 不足 1 小时按 1 小时计算
            long hours = (totalMinutes + 59) / 60;

            // 将 pricePerHour 转换为数值类型
            double pricePerHour = Double.parseDouble(parkingSpace.getPricePerHour());
            // 计算总费用
            double totalFee = pricePerHour * hours;

            // 创建预约记录
            Reservations reservation = new Reservations();
            reservation.setUserId(reservationDTO.getUserId());
            reservation.setParkingLotId(parkingSpace.getParkingLotId());
            reservation.setSpaceId(parkingSpace.getSpaceId());
            reservation.setStartTime(reservationDTO.getStartTime());
            reservation.setEndTime(reservationDTO.getEndTime());
            reservation.setTotalFee(totalFee);
            reservation.setStatus("active");

            // 插入预约记录到 Reservations 表
            reservationService.createReservation(reservation);

            return Result.success("车位已成功预定");
        } else {
            // 如果车位不可用，返回错误信息
            return Result.error("车位不可用，无法预定");
        }
    }


    /**
     * 根据id查询停车位全部信息
     */
    @ApiOperation("根据id查询停车位")
    @GetMapping("/{spaceId}")
    public Result<ParkingSpaces>getSpacesById(@PathVariable Integer spaceId, Integer parkingLotId) {
        ParkingSpaces parkingSpaces = parkingService.getSpacesById(spaceId,parkingLotId);
        return Result.success(parkingSpaces);
    }

    /**
     *根据id获取停车场信息
     */
    @ApiOperation("根据id查询停车场")
    @GetMapping("/parkingLots/{parkingLotId}")
    public  Result<ParkingLots> getParkingLots(@PathVariable Integer parkingLotId){
        ParkingLots parkingLots = parkingService.getLotInfo(parkingLotId);
        return Result.success(parkingLots);
    }

    /**
     *修改某个停车场信息
     */
    @ApiOperation("修改停车场")
    @PutMapping("/parkingLots/update")
    public Result updateParkingLots(@RequestBody UpdateLotsDTO updateLotsDTO){
         parkingService.updateLots(updateLotsDTO);
        return Result.success("修改成功");
    }



}
