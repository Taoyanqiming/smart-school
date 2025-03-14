package com.sky.controller.user;

import com.sky.dto.ParkingLotsAddDTO;
import com.sky.dto.ParkingSpacesQueryDTO;
import com.sky.dto.ReservationDTO;
import com.sky.entity.*;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user/parking")
@Slf4j
@Api(tags = "用户停车位相关接口")
public class UserParkingController {

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








}
