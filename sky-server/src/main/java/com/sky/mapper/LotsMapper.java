package com.sky.mapper;

import com.sky.dto.ParkingLotsAddDTO;
import com.sky.dto.UpdateLotsDTO;
import com.sky.entity.ParkingLots;
import com.sky.entity.ParkingSpaces;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LotsMapper {

    /**
     *修改某个停车场信息
     */
    void updateLots(UpdateLotsDTO updateLotsDTO);


    /**
     * 获取停车场信息
     * @param parkingLotId
     * @return
     */
    ParkingLots searchLots(Integer parkingLotId);
    /**
     * 预约车位后停车场空闲车位增减
     */
    void addLots(ParkingLotsAddDTO p);
}
