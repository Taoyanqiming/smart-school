-- 打印输入参数
redis.log(redis.LOG_DEBUG, "===== Lua脚本执行开始 =====")
redis.log(redis.LOG_DEBUG, string.format("输入参数: seckillId=%s, userId=%s, orderId=%s, quantity=%s",
    ARGV[1], ARGV[2], ARGV[3], ARGV[4]))

local seckillId = ARGV[1]
local userId = ARGV[2]
local orderId = ARGV[3]
local quantity = tonumber(ARGV[4])

-- 参数校验：quantity 必须为正整数
if quantity == nil or quantity <= 0 then
    redis.log(redis.LOG_DEBUG, "返回-1: 无效购买数量（quantity必须为正整数）")
    return -1
end

-- 库存Key
local stockKey = 'seckill:stock:' .. seckillId
-- 用户购买记录Key
local userRecordKey = 'seckill:user_record:' .. seckillId .. ':' .. userId

-- 获取当前时间（秒级时间戳）
local currentTime = tonumber(redis.call('time')[1])
redis.log(redis.LOG_DEBUG, string.format("当前时间戳(秒): %d", currentTime))

-- 校验秒杀时间
local startTime = tonumber(redis.call('GET', 'seckill:start_time:' .. seckillId)) or math.huge
local endTime = tonumber(redis.call('GET', 'seckill:end_time:' .. seckillId)) or -math.huge

if currentTime < startTime then
    redis.log(redis.LOG_DEBUG, "返回1: 秒杀未开始（当前时间 < 开始时间）")
    return 1
end

if currentTime > endTime then
    redis.log(redis.LOG_DEBUG, "返回2: 秒杀已结束（当前时间 > 结束时间）")
    return 2
end

-- 校验库存（处理 nil 为 0）
local stock = tonumber(redis.call('GET', stockKey)) or 0
redis.log(redis.LOG_DEBUG, string.format("当前库存: %d", stock))

if stock < quantity then
    redis.log(redis.LOG_DEBUG, "返回3: 库存不足（库存%d < 购买数量%d）", stock, quantity)
    return 3
end

-- 校验限购（处理 nil 为无限制）
local limit = tonumber(redis.call('GET', 'seckill:limit:' .. seckillId)) or math.huge
local userPurchased = tonumber(redis.call('GET', userRecordKey)) or 0
redis.log(redis.LOG_DEBUG, string.format("用户已购买数量: %d, 限购数量: %s", userPurchased, limit == math.huge and "无限制" or tostring(limit)))

if userPurchased + quantity > limit then
    redis.log(redis.LOG_DEBUG, "返回4: 超过限购数量（已购%d + 购买%d > 限购%d）", userPurchased, quantity, limit)
    return 4
end

-- 原子扣减库存 & 更新购买记录
redis.log(redis.LOG_DEBUG, string.format("准备执行原子操作: 扣减库存(%s) %d, 更新用户记录(%s) %d",
    stockKey, quantity, userRecordKey, quantity))

-- 扣减库存（原子操作）
local newStock = redis.call('INCRBY', stockKey, -quantity)
-- 更新用户购买记录
local newRecord = redis.call('INCRBY', userRecordKey, quantity)

-- 记录用户参与（使用有序集合或集合，根据业务需求）
redis.call('SADD', 'seckill:participants:' .. seckillId, userId)

redis.log(redis.LOG_DEBUG, "===== 秒杀成功 =====")
redis.log(redis.LOG_DEBUG, string.format("新库存: %d, 用户累计购买: %d", newStock, newRecord))
return 0