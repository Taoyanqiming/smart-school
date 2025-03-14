package com.sky.controller.admin;

import com.sky.dto.ParkingLotsAddDTO;
import com.sky.dto.UpdateLotsDTO;
import com.sky.dto.ReservationDTO;
import com.sky.entity.*;
import com.sky.result.Result;
import com.sky.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/admin")
@Slf4j
@Api(tags = "管理员停车位相关接口")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private FeeService feeService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;


    @ApiOperation("管理员修改停车位信息")
    @PutMapping("/parking-spaces/update")
    public Result<String> updateParkingSpace(@RequestBody ParkingSpaces parkingSpaces) {
        log.info("管理员修改停车位信息，参数: {}", parkingSpaces);

        try {
            // 调用服务层方法进行修改
            parkingService.updateParkingSpace(parkingSpaces);

            // 更新 Redis 缓存
            // 假设之前缓存了分页数据和单个停车位数据
            // 清除分页数据缓存
            redisTemplate.delete("parking_spaces_page:*");
            // 清除单个停车位数据缓存
            redisTemplate.delete("parking_space:" + parkingSpaces.getSpaceId() + ":" + parkingSpaces.getParkingLotId());

            return Result.success("停车位信息修改成功");
        } catch (Exception e) {
            log.error("修改停车位信息失败", e);
            return Result.error("停车位信息修改失败");
        }
    }

    @ApiOperation("返回一个停车场内全部停车位")
    @GetMapping("/parking-lots/{parkingLotId}/spaces")
    public Result<List<ParkingSpaces>> getAllParkingSpacesInLot(@PathVariable Integer parkingLotId) {
        log.info("查询停车场 ID 为 {} 的全部停车位信息", parkingLotId);

        // 生成 Redis 缓存的 key
        String key = "parking_lot_spaces:" + parkingLotId;

        // 先从 Redis 中获取数据
        List<ParkingSpaces> parkingSpaces = (List<ParkingSpaces>) redisTemplate.opsForValue().get(key);

        if (parkingSpaces == null) {
            // 如果 Redis 中没有数据，从数据库中查询
            parkingSpaces = parkingService.getAllParkingSpacesInLot(parkingLotId);

            // 将查询结果缓存到 Redis 中，设置缓存时间为 1 小时
            redisTemplate.opsForValue().set(key, parkingSpaces, 1, TimeUnit.HOURS);
        }

        return Result.success(parkingSpaces);
    }

    @ApiOperation("预约车位")
    @PostMapping("/parking-spaces/reserve")
    public Result<String> updateParkingSpaces(@RequestBody ReservationDTO reservationDTO) {
        // 生成 Redis 分布式锁的 key
        String lockKey = "parking_space_lock:" + reservationDTO.getSpaceId() + ":" + reservationDTO.getParkingLotId();
        // 尝试获取锁，设置锁的过期时间为 5 秒
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 5, TimeUnit.SECONDS);

        if (locked != null && locked) {
            try {
                // 校验停车位是否可用
                ParkingSpaces parkingSpace = parkingService.getSpacesById(reservationDTO.getSpaceId(), reservationDTO.getParkingLotId());
                // 检查 parkingSpace 是否为空
                if (parkingSpace == null) {
                    return Result.error("未找到对应的停车位，spaceId: " + reservationDTO.getSpaceId() + ", parkingLotId: " + reservationDTO.getParkingLotId());
                }
                if ("空闲".equals(parkingSpace.getStatus())) {
                    // 更新停车位状态为 "占用"
                    parkingSpace.setStatus("占用");
                    // 删除相关缓存
                    redisTemplate.delete("parking_space:" + reservationDTO.getSpaceId() + ":" + reservationDTO.getParkingLotId());
                    redisTemplate.delete("parking_lot_spaces:" + reservationDTO.getParkingLotId());

                    // 保存停车位状态更新
                    parkingService.updateParkingSpace(parkingSpace);
                    // 更新停车场状态，空闲车位-1
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
                    double pricePerHour;
                    try {
                        pricePerHour = Double.parseDouble(parkingSpace.getPricePerHour());
                    } catch (NumberFormatException e) {
                        return Result.error("停车位每小时价格格式错误，请检查");
                    }
                    // 计算总费用
                    double totalFee = pricePerHour * hours;

                    User user = userService.getByNumber(reservationDTO.getPhoneNumber());


                    if (user != null) {
                    // 创建预约记录
                    Reservations reservation = new Reservations();
                    reservation.setUserId(user.getUserId());
                    reservation.setParkingLotId(parkingSpace.getParkingLotId());
                    reservation.setSpaceId(parkingSpace.getSpaceId());
                    reservation.setStartTime(reservationDTO.getStartTime());
                    reservation.setEndTime(reservationDTO.getEndTime());
                    reservation.setTotalFee(totalFee);
                    reservation.setLicensePlate(reservationDTO.getLicensePlate());
                    reservation.setStatus("active");

                    // 插入预约记录到 Reservations 表
                    reservationService.createReservation(reservation);



                        // 生成支付订单
                        Payments payments = Payments.builder()
                                .userId(user.getUserId())
                                .reservationId(reservation.getReservationId())
                                .amount(reservation.getTotalFee())
                                .status("未支付")
                                .paymentMethod("微信")
                                .build();
                        feeService.createPay(payments);

                        // 发送通知信息
                        Notice notice = Notice.builder()
                                .userId(user.getUserId())
                                .title("账单支付通知")
                                .content("您有一笔待支付的账单")
                                .build();
                        noticeService.createNotice(notice);
                    } else {
                        return Result.error("未找到对应的用户，请检查电话号码是否正确");
                    }

                    return Result.success("车位已成功预定");
                } else {
                    // 如果车位不可用，返回错误信息
                    return Result.error("车位不可用，无法预定");
                }
            } finally {
                // 释放锁
                redisTemplate.delete(lockKey);
            }
        } else {
            return Result.error("当前车位正在被操作，请稍后再试");
        }
    }

//    /**
//     * 根据id查询停车位全部信息
//     * @param spaceId 停车位 ID
//     * @param parkingLotId 停车场 ID
//     * @return 停车位信息
//     */
//    @ApiOperation("根据id查询停车位")
//    @GetMapping("/{spaceId}/{parkingLotId}")
//    public Result<ParkingSpaces> getSpacesById(@PathVariable Integer spaceId, @PathVariable Integer parkingLotId) {
//        ParkingSpaces parkingSpaces = parkingService.getSpacesById(spaceId, parkingLotId);
//        return Result.success(parkingSpaces);
//    }



    @ApiOperation("修改停车场")
    @PutMapping("/parkingLots/update")
    public Result<String> updateParkingLots(@RequestBody UpdateLotsDTO updateLotsDTO) {
        try {
            parkingService.updateLots(updateLotsDTO);

            // 更新 Redis 缓存
            // 清除停车场信息缓存
            redisTemplate.delete("parking_lot:" + updateLotsDTO.getParkingLotId());
            // 清除该停车场内停车位信息缓存
            redisTemplate.delete("parking_lot_spaces:" + updateLotsDTO.getParkingLotId());

            return Result.success("修改成功");
        } catch (Exception e) {
            log.error("修改停车场信息失败", e);
            return Result.error("修改停车场信息失败");
        }
    }
}