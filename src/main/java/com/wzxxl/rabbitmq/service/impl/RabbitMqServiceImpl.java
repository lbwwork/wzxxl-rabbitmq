package com.wzxxl.rabbitmq.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.tuojing.pbrabbitmq.api.service.RabbitMqService;
import com.tuojing.pbrabbitmq.api.utils.RabbitMqUtil;
import com.tuojing.sdk.api.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lbw
 * @version 1.0
 * @since 2023/1/30
 */
@Service
@Slf4j
public class RabbitMqServiceImpl implements RabbitMqService {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqUtil rabbitMqUtil;

    @Override
    public Response<String> createExchangesAndBindQueue(String exchange, String type, String queue, boolean durable
            , boolean exclusive, boolean autoDelete, Map<String, Object> queueArguments, String routingKey, Map<String, Object> exchangeArguments) {
        Connection connection = null;
        Channel channel = null;
        try {
            connection = rabbitMqUtil.getConnection();
            channel = connection.createChannel();
            //参数一交换机名称，参数二交换机类型 创建交换机
            channel.exchangeDeclare(exchange, type, durable, autoDelete, exchangeArguments);
            //参数一：队列名称,参数二：是否持久化,参数三：是否独占队列，参数四：是否在消费完成后自动删除队列，  创建并持久化队列
            channel.queueDeclare(queue, durable, exclusive, autoDelete, queueArguments);
            //参数一：队列名称,参数二交换机名称,参数三路由key  绑定队列到交换机
            channel.queueBind(queue, exchange, routingKey);
            return Response.success("创建成功");
        } catch (IOException e) {
            e.printStackTrace();
            return Response.error("创建失败");
        } finally {
            RabbitMqUtil.closeChannelAndConnection(channel, connection);
        }
    }

    @Override
    public Response<String> createQueueAndBind(String exchange, String queue, boolean durable, boolean exclusive
            , boolean autoDelete, Map<String, Object> arguments, String routingKey) {
        Connection connection = null;
        Channel channel = null;
        try {
            connection = rabbitMqUtil.getConnection();
            channel = connection.createChannel();
            //参数一：队列名称,参数二：是否持久化,参数三：是否独占队列，参数四：是否在消费完成后自动删除队列，  创建并持久化队列
            channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
//                //参数一：队列名称,参数二交换机名称,参数三路由key  绑定队列到交换机
            channel.queueBind(queue, exchange, routingKey);
            return Response.success("创建成功");
        } catch (IOException e) {
            e.printStackTrace();
            return Response.error("创建失败");
        } finally {
            RabbitMqUtil.closeChannelAndConnection(channel, connection);
        }
    }

    @Override
    public Response<String> sendMsg(String exchange, String routingKey, Object msg, boolean needRePublish) {
        try {
            Channel channel = rabbitTemplate.getConnectionFactory().createConnection().createChannel(false);
            channel.confirmSelect();
            channel.basicPublish(exchange, routingKey, null,
                    JSONObject.toJSONString(msg).getBytes(StandardCharsets.UTF_8));
            channel.addConfirmListener(new ConfirmListener() {
                //消息失败处理
                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    log.info("sendQueue-ack-confirm-fail==>exchange:{}--routingKey:{}--deliveryTag:{}--multiple:{}--message:{}", exchange, routingKey, deliveryTag, multiple, JSONObject.toJSONString(msg));
                    try {
                        TimeUnit.SECONDS.sleep(1L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    //重发
                    channel.basicPublish(exchange, routingKey, null, JSONObject.toJSONString(msg).getBytes(StandardCharsets.UTF_8));
                }
                //消息成功处理
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    log.info("sendQueue-ack-confirm-success==>exchange:{}--routingKey:{}--deliveryTag:{}--multiple:{}", exchange, routingKey, deliveryTag, multiple);
                }
            });
        } catch (Exception e) {
            log.error("sendQueue-ack-发送消息失败:{}", ExceptionUtils.getStackTrace(e));
            return Response.error("消息发送失败");
        }
        return Response.success("消息发送成功");
    }
}
