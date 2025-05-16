package com.sky.rabbitmq;
import com.sky.dto.CommentDTO;
import com.sky.dto.LikeCommentDTO;
import com.sky.dto.LikeDTO;
import com.sky.entity.Message;
import com.sky.entity.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送点赞消息
     * @param message 点赞消息对象
     */
    public void sendMessage(Message message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "message", message);
    }


    /**
     * 发送订单消息
     * @param order 订单消息对象
     */
    public void sendOrderMessage(Order order) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "order", order);
    }

}
