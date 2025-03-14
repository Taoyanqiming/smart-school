package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.ParkingLotsAddDTO;
import com.sky.dto.ParkingSpacesQueryDTO;
import com.sky.dto.UpdateLotsDTO;
import com.sky.entity.ParkingLots;
import com.sky.entity.ParkingSpaces;
import com.sky.mapper.LotsMapper;
import com.sky.mapper.ParkingMapper;
import com.sky.result.PageResult;
import com.sky.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    private ParkingMapper parkingMapper;

    @Autowired
    private LotsMapper lotsMapper;


    /**
     * 更新车位状态
     * @param parkingSpace
     */
    @Override
    public void updateParkingSpace(ParkingSpaces parkingSpace){
        parkingMapper.updateSpaces(parkingSpace);
    }


    /**
     * 展示停车位信息，根据条件筛选（如果有）
     */
    @Override
    public PageResult getParkingSpaces(ParkingSpacesQueryDTO parkingSpacesQueryDTO) {
        PageHelper.startPage(parkingSpacesQueryDTO.getPage(),parkingSpacesQueryDTO.getPageSize());
        Page<ParkingSpaces> page = parkingMapper.selectSpaces(parkingSpacesQueryDTO);
        long total = page.getTotal();
        List<ParkingSpaces> records = page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 根据id查询某个停车位
     */
    @Override
    public ParkingSpaces getSpacesById(Integer spaceId, Integer parkingLotId){
        return parkingMapper.getSpacesById(spaceId,parkingLotId);
    }
    /**
     * 返回一个停车场内全部停车位
     * @param parkingLotId 停车场 ID
     * @return 停车场内的全部停车位信息
     */
    public List<ParkingSpaces> getAllParkingSpacesInLot(Integer parkingLotId){
        return parkingMapper.getSpaces(parkingLotId);
    }


    /**
     *修改某个停车场信息
     */
    @Override
    public void updateLots(UpdateLotsDTO updateLotsDTO){
        lotsMapper.updateLots(updateLotsDTO);
    }


    /**
     * 根据ID获取停车场信息
     * @param parkingLotId
     * @return
     */
   public ParkingLots getLotInfo(Integer parkingLotId){
       return lotsMapper.searchLots(parkingLotId);
   }
    /**
     * 预约车位后停车场空闲车位增减
     * @param p
     */
   public void addLots(ParkingLotsAddDTO p){
        lotsMapper.addLots(p);
   }

}
