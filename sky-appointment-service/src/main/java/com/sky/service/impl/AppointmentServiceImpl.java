package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.AppointmentDTO;
import com.sky.dto.MessageDTO;
import com.sky.entity.Appointment;
import com.sky.entity.AppointmentLog;
import com.sky.mapper.AppointmentLogMapper;
import com.sky.mapper.AppointmentMapper;
import com.sky.rabbitmq.appointSender;
import com.sky.service.AppointmentService;
import com.sky.utils.RabbitMQConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private AppointmentLogMapper appointmentLogMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private appointSender appointSendersl;


    private static final String APPOINTMENT_KEY_PREFIX = "appointment:";
    private static final String USER_APPOINTMENTS_KEY_PREFIX = "user_appointments:";

    @Override
    @Transactional
    public void submit(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(appointmentDTO, appointment);
        appointment.setStatus(0); // 初始状态为待审批
        appointment.setCreateTime(LocalDateTime.now());
        appointment.setUpdateTime(LocalDateTime.now());
        appointmentMapper.insert(appointment);

        MessageDTO messageDTO = MessageDTO.builder()
                .userId(appointmentDTO.getUserId())
                .type(5)
                .sourceModule("/api/appointment/" + appointmentDTO.getAppointmentId())
                .sourceId(appointment.getAppointmentId())
                .content("您已成功预约")
                .createTime(LocalDateTime.now())
                .build();
        appointSendersl.sendAppointmentMessage(messageDTO);
        // 清除用户预约缓存
        clearUserAppointmentsCache(appointment.getUserId());
    }

    @Override
    @Transactional
    public void update(AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentMapper.getById(appointmentDTO.getAppointmentId());
        if (appointment == null) {
            throw new RuntimeException("预约记录不存在");
        }

        // 只有待审批状态可以修改
        if (appointment.getStatus() != 0) {
            throw new RuntimeException("只有待审批状态的预约可以修改");
        }

        BeanUtils.copyProperties(appointmentDTO, appointment);
        appointment.setUpdateTime(LocalDateTime.now());

        appointmentMapper.update(appointment);

        // 清除缓存
        clearCache(appointment.getAppointmentId());
        clearUserAppointmentsCache(appointment.getUserId());
    }

    @Override
    @Transactional
    public void approve(Integer appointmentId, Integer status, Integer approverId) {
        Appointment appointment = appointmentMapper.getById(appointmentId);
        if (appointment == null) {
            throw new RuntimeException("预约记录不存在");
        }

        // 更新预约状态
        appointment.setStatus(status);
        appointment.setUpdateTime(LocalDateTime.now());
        appointmentMapper.update(appointment);

        // 记录审批日志
        AppointmentLog log = new AppointmentLog();
        log.setAppointmentId(appointmentId);
        log.setApproverId(approverId);
        log.setApproveStatus(status);
        log.setApproveTime(LocalDateTime.now());
        log.setUpdateTime(LocalDateTime.now());
        appointmentLogMapper.insert(log);

        MessageDTO messageDTO = MessageDTO.builder()
                .userId(appointment.getUserId())
                .type(5)
                .sourceModule("/api/appointment/" + appointmentId)
                .sourceId(appointmentId)
                .createTime(LocalDateTime.now())
                .build();
        appointSendersl.sendAppointmentMessage(messageDTO);
        // 发送消息通知用户
        if (status == 1) { // 审核通过
            messageDTO.setContent("预约成功");
            appointSendersl.sendAppointmentMessage(messageDTO);
        } else if (status == 2) { // 审核拒绝
            messageDTO.setContent("预约失败");
            appointSendersl.sendAppointmentMessage(messageDTO);
        }

        // 清除缓存
        clearCache(appointmentId);
        clearUserAppointmentsCache(appointment.getUserId());
    }

    @Override
    public List<Appointment> getByUserId(Integer userId) {
        String key = USER_APPOINTMENTS_KEY_PREFIX + userId;
        List<Appointment> appointments = (List<Appointment>) redisTemplate.opsForValue().get(key);

        if (appointments == null) {
            appointments = appointmentMapper.getByUserId(userId);
            redisTemplate.opsForValue().set(key, appointments, 30, TimeUnit.MINUTES);
        }

        return appointments;
    }

    @Override
    public Appointment getById(Integer appointmentId) {
        String key = APPOINTMENT_KEY_PREFIX + appointmentId;
        Appointment appointment = (Appointment) redisTemplate.opsForValue().get(key);

        if (appointment == null) {
            appointment = appointmentMapper.getById(appointmentId);
            if (appointment != null) {
                redisTemplate.opsForValue().set(key, appointment, 30, TimeUnit.MINUTES);
            }
        }

        return appointment;
    }

    private void clearCache(Integer appointmentId) {
        redisTemplate.delete(APPOINTMENT_KEY_PREFIX + appointmentId);
    }

    private void clearUserAppointmentsCache(Integer userId) {
        redisTemplate.delete(USER_APPOINTMENTS_KEY_PREFIX + userId);
    }
}