package com.sky.service;

import com.sky.dto.ParkingLotsAddDTO;
import com.sky.dto.ParkingSpacesQueryDTO;
import com.sky.dto.UpdateLotsDTO;
import com.sky.entity.ParkingLots;
import com.sky.entity.ParkingSpaces;
import com.sky.result.PageResult;

import java.util.Map;
import java.util.List;

public interface ParkingService {
    /**
     * 获取停车场信息
     * @param parkingLotId
     * @return
     */
     ParkingLots getLotInfo(Integer parkingLotId);

    /**
     *修改某个停车场信息
     */
     void updateLots(UpdateLotsDTO updateLotsDTO);

    /**
     * 分页展示停车位信息，根据条件筛选（如果有）
     */
    PageResult getParkingSpaces(ParkingSpacesQueryDTO parkingSpacesQueryDTO);

    /**
     * 根据id查询停车位
     */
    ParkingSpaces getSpacesById(Integer spaceId, Integer parkingLotId);

    /**
     * 更新车位状态
     * @param parkingSpace
     */
    void updateParkingSpace(ParkingSpaces parkingSpace);

    /**
     * 预约车位后停车场空闲车位增减
     * @param p
     */
    void addLots(ParkingLotsAddDTO p);
}
