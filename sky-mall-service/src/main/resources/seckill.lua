
--- 秒杀商品 ID
local seckillId = ARGV[1]
-- 用户 ID
local userId = ARGV[2]
-- 订单 ID
local orderId = ARGV[3]
-- 购买数量
local quantity = tonumber(ARGV[4])

-- 库存 key
local stockKey = 'seckill:stock:' .. seckillId
-- 订单 key
local orderKey = 'seckill:order:' .. seckillId
-- 用户购买记录 key
local userRecordKey = 'seckill:user_record:' .. seckillId .. ':' .. userId

-- 获取当前时间（假设 Redis 服务器时间和业务系统时间一致）
local currentTime = tonumber(redis.call('time')[1])

-- 从 Redis 中获取秒杀开始时间和结束时间（需要提前将这些信息存入 Redis）
local startTimeKey = 'seckill:start_time:' .. seckillId
local endTimeKey = 'seckill:end_time:' .. seckillId
local startTime = tonumber(redis.call('get', startTimeKey))
local endTime = tonumber(redis.call('get', endTimeKey))

-- 判断秒杀活动是否开始
if startTime == nil or currentTime < startTime then
    return 1 -- 秒杀未开始
end

-- 判断秒杀活动是否结束
if endTime == nil or currentTime > endTime then
    return 2 -- 秒杀已结束
end

-- 库存是否充足
local stock = tonumber(redis.call('get', stockKey))
if stock == nil or stock < quantity then
    return 3 -- 库存不足
end

-- 获取用户已购买数量
local userPurchased = tonumber(redis.call('get', userRecordKey)) or 0

-- 从 Redis 中获取该秒杀商品的限购数量（需要提前将这些信息存入 Redis）
local limitKey = 'seckill:limit:' .. seckillId
local limit = tonumber(redis.call('get', limitKey))

-- 判断用户是否超过限购数量
if limit ~= nil and userPurchased + quantity > limit then
    return 4 -- 超过限购数量
end

-- 扣减库存
redis.call('incrby', stockKey, -quantity)

-- 更新用户购买记录
redis.call('incrby', userRecordKey, quantity)

-- 下单（保存用户）
redis.call('sadd', orderKey, userId)

-- 发送消息
redis.call('xadd', 'stream.orders', '*', 'userId', userId, 'seckillId', seckillId, 'orderId', orderId, 'quantity', quantity)

return 0 -- 秒杀成功    