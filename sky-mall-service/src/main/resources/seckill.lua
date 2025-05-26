local seckillId = ARGV[1]
local userId = ARGV[2]
local orderId = ARGV[3]
local quantity = tonumber(ARGV[4])

-- 库存Key
local stockKey = 'seckill:stock:' .. seckillId
-- 用户购买记录Key
local userRecordKey = 'seckill:user_record:' .. seckillId .. ':' .. userId

-- 获取当前时间（秒级时间戳）
local currentTime = tonumber(redis.call('time')[1])

-- 校验秒杀时间（提前存入Redis的start/end时间需为秒级时间戳）
local startTime = tonumber(redis.call('GET', 'seckill:start_time:' .. seckillId))
local endTime = tonumber(redis.call('GET', 'seckill:end_time:' .. seckillId))

if startTime == nil or currentTime < startTime then
    return 1 -- 秒杀未开始
end

if endTime == nil or currentTime > endTime then
    return 2 -- 秒杀已结束
end

-- 校验库存
local stock = tonumber(redis.call('GET', stockKey))
if stock == nil or stock < quantity then
    return 3 -- 库存不足
end

-- 校验限购
local userPurchased = tonumber(redis.call('GET', userRecordKey)) or 0
local limit = tonumber(redis.call('GET', 'seckill:limit:' .. seckillId))
if limit ~= nil and userPurchased + quantity > limit then
    return 4 -- 超过限购
end

-- 原子扣减库存 & 更新购买记录
redis.call('MULTI')  -- 开启事务（可选，确保两个操作原子性）
redis.call('INCRBY', stockKey, -quantity)
redis.call('INCRBY', userRecordKey, quantity)
redis.call('EXEC')    -- 执行事务

-- 记录用户参与（去重）
redis.call('SADD', 'seckill:order:' .. seckillId, userId)

return 0 -- 成功（仅返回状态，不处理消息）