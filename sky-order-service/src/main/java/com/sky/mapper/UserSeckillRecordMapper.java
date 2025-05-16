package com.sky.mapper;

import com.sky.entity.UserSeckillRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户秒杀记录数据访问层接口
 */
@Mapper
public interface UserSeckillRecordMapper {
    /**
     * 根据用户ID和秒杀商品ID获取用户秒杀记录
     * @param userId    用户ID
     * @param seckillId 秒杀商品ID
     * @return 用户秒杀记录
     */
    UserSeckillRecord getRecordByUserIdAndSeckillId(Integer userId, Integer seckillId);

    /**
     * 插入用户秒杀记录
     * @param userId    用户ID
     * @param seckillId 秒杀商品ID
     * @param quantity  购买数量
     */
    void insertRecord(Integer userId, Integer seckillId, Integer quantity);

    /**
     * 更新用户秒杀记录
     * @param userId    用户ID
     * @param seckillId 秒杀商品ID
     * @param quantity  购买数量
     */
    void updateRecord(Integer userId, Integer seckillId, Integer quantity);
}