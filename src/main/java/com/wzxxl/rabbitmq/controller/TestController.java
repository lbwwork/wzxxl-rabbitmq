package com.wzxxl.rabbitmq.controller;

import com.tuojing.pbrabbitmq.api.service.RabbitMqService;
import com.tuojing.sdk.api.domain.dto.MessageDto;
import com.tuojing.sdk.api.utils.Response;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;


/**
 * @Author libaowen
 * @Date 2023/10/20 10:19
 * @Version 1.0
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RabbitMqService rabbitMqService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/test")
    public Response<String> test(@RequestBody MessageDto messageDto) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration("4000");
        Message message = new Message("testcc".getBytes(StandardCharsets.UTF_8), messageProperties);
        rabbitTemplate.convertAndSend(messageDto.getExchange(), messageDto.getRoutingKey(), message);
        return Response.success("操作成功");
    }

//    @RabbitListener(queues = "meet_remind_dead_letter_queue")
//    public void getMsg1(String msg, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws IOException {
//        System.out.println(msg);
//        channel.basicAck(deliveryTag, false);
////        //手动确认回滚 拒绝deliveryTag对应的消息，第二个参数是否requeue，true则重新入队列，否则丢弃或者进入死信队列。
////        channel.basicReject(deliveryTag, true);
//    }
}
