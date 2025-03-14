package com.sky.controller.pub;


import com.sky.dto.ParkingLotsAddDTO;
import com.sky.dto.ParkingSpacesQueryDTO;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/pub/parking")
@Slf4j
@Api(tags = "公共停车位展示相关接口")
public class PubParkingController {

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/page/spaces")
    @ApiOperation("分页展示停车位信息")
    public Result<PageResult> getParkingSpaces(ParkingSpacesQueryDTO parkingSpacesQueryDTO) {
        log.info("分页查询停车位信息，参数: {}", parkingSpacesQueryDTO);

        // 生成 Redis 缓存的 key，包含所有可能影响查询结果的参数
        String key = "parking_spaces_page:" +
                parkingSpacesQueryDTO.getPage() + ":" +
                parkingSpacesQueryDTO.getPageSize() + ":" +
                (parkingSpacesQueryDTO.getParkingLotId() != null ? parkingSpacesQueryDTO.getParkingLotId() : "null") + ":" +
                (parkingSpacesQueryDTO.getStatus() != null ? parkingSpacesQueryDTO.getStatus() : "null") + ":" +
                (parkingSpacesQueryDTO.getType() != null ? parkingSpacesQueryDTO.getType() : "null");

        // 先从 Redis 中获取数据
        PageResult pageResult = (PageResult) redisTemplate.opsForValue().get(key);

        if (pageResult == null) {
            // 如果 Redis 中没有数据，从数据库中查询
            pageResult = parkingService.getParkingSpaces(parkingSpacesQueryDTO);

            // 将查询结果缓存到 Redis 中，设置缓存时间为 1 小时
            redisTemplate.opsForValue().set(key, pageResult, 1, TimeUnit.HOURS);
        }

        // 返回查询结果
        return Result.success(pageResult);
    }


    /**
     * 根据id获取停车场信息
     * @param parkingLotId 停车场 ID
     * @return 停车场信息
     */
    @ApiOperation("根据id查询停车场")
    @GetMapping("/parkingLots/{parkingLotId}")
    public  Result<ParkingLots> getParkingLots(@PathVariable Integer parkingLotId){
        ParkingLots parkingLots = parkingService.getLotInfo(parkingLotId);
        return Result.success(parkingLots);
    }



}
