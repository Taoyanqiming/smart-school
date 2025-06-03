package com.sky.aop;

import com.sky.annotation.AutoFill;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现时间字段自动填充（仅处理 create_time 和 update_time）
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点：拦截 Mapper 层带有 @AutoFill 注解的方法
     */
    @Pointcut("execution(* com.sky.mapper..*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     * 前置通知：在方法执行前填充时间字段
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始自动填充时间字段");

        // 获取操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        // 遍历所有参数，查找实体对象
        for (Object arg : joinPoint.getArgs()) {
            if (arg != null) {
                fillEntityFields(arg, operationType);
            }
        }
    }

    private void fillEntityFields(Object entity, OperationType operationType) {
        LocalDateTime now = LocalDateTime.now();
        try {
            if (operationType == OperationType.INSERT) {
                setFieldValue(entity, "setCreateTime", LocalDateTime.class, now);
                setFieldValue(entity, "setUpdateTime", LocalDateTime.class, now);
            } else if (operationType == OperationType.UPDATE) {
                setFieldValue(entity, "setUpdateTime", LocalDateTime.class, now);
            }
        } catch (Exception e) {
            log.error("时间字段自动填充失败: {}", e.getMessage(), e);
            throw new RuntimeException("自动填充失败", e);
        }
    }

    /**
     * 通过反射设置实体类时间字段
     */
    private void setFieldValue(Object entity, String methodName, Class<?> paramType, Object value) throws Exception {
        if (entity == null || value == null) {
            return;
        }
        try {
            Method method = entity.getClass().getDeclaredMethod(methodName, paramType);
            method.setAccessible(true); // 允许访问私有方法（如有需要）
            method.invoke(entity, value);
            log.debug("成功填充字段: {}", methodName);
        } catch (NoSuchMethodException e) {
            log.debug("实体类 {} 缺少方法 {}, 跳过填充", entity.getClass().getName(), methodName);
        }
    }
}