package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.ParkingDTO;
import com.sky.dto.ParkingSpacesQueryDTO;
import com.sky.dto.UpdateLotsDTO;
import com.sky.entity.ParkingLots;
import com.sky.entity.ParkingSpaces;
import com.sky.vo.ParkingVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ParkingMapper {
    /**
     * 更新车位状态
     * @param parkingSpace
     */
    void updateSpaces(ParkingSpaces parkingSpace);
    /**
     * 展示停车位信息，根据条件筛选（如果有）
     */
    Page<ParkingSpaces> selectSpaces(ParkingSpacesQueryDTO parkingSpacesQueryDTO);
    /**
     * 根据id查询停车位
     */
    ParkingSpaces getSpacesById(Integer spaceId, Integer parkingLotId);
}
