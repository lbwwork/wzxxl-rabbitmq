package com.wzxxl.rabbitmq.service;

import com.tuojing.sdk.api.utils.Response;

import java.util.Map;

/**
 * @author lbw
 * @version 1.0
 * @since 2023/1/30
 */
public interface RabbitMqService {

    /**
     * 创建交换机和队列，并将队列绑定到交换机上
     * @param exchange 交换机名称
     * @param type 交换机类型 常见的如fanout 广播使用，发送给所有绑定到这个交换机的队列中、direct 定点推送，发送给精确匹配的队列中、topic 发送给通配的队列中
     * @param queue 队列名称
     * @param durable 是否持久化
     * @param exclusive 是否排外，当设置为true时有可能会造成数据丢失
     * @param autoDelete 是否自动删除
     * @param routingKey 路由key,用于将队列绑定到交换机
     * @param queueArguments 队列额外参数  官方文档https://www.rabbitmq.com/queues.html
     *                  常用的值有:
     *                  x-message-ttl:  发送到队列的消息可以存活多长时间（毫秒）。简单来说,就是队列中消息的过期时间
     *                  x-expires: 队列在被自动删除（毫秒）之前可以存活多长时间。简单来说,就是队列的过期时间
     *                  x-max-length: 队列中可以存储处于ready状态的消息数量。简单来说,就是队列最多可以容纳多少条消息,超出部分从头部开始删除
     *                  x-max-length-bytes:  队列中可以存储处于ready状态的消息占用的内存空间，单位：字节
     *                  x-overflow: 队列溢出行为，决定当队列达到设置的最大长度或者最大存储空间时接收到新消息的处理方式
     *                      drop-head（删除queue头部的消息）
     *                      reject-publish（最近发来的消息将被丢弃）
     *                      reject-publish-dlx（拒绝发送消息到死信交换器）
     * @param exchangeArguments  交换机额外参数
     * @return
     */
    Response<String> createExchangesAndBindQueue(String exchange, String type, String queue, boolean durable,
                                                 boolean exclusive, boolean autoDelete, Map<String, Object> queueArguments,
                                                 String routingKey, Map<String, Object> exchangeArguments);


    /**
     * 创建队列并绑定到交换机
     * @param exchange
     * @param queue
     * @param durable
     * @param exclusive
     * @param autoDelete
     * @param arguments
     * @param routingKey
     * @return
     */
    Response<String> createQueueAndBind(String exchange, String queue, boolean durable, boolean exclusive, boolean autoDelete,
                                        Map<String, Object> arguments, String routingKey);
    /**
     * 发送消息
     * @param exchange 交换机名称
     * @param routingKey 路由key
     * @param msg 消息
     * @param needRePublish 发送失败时是否需要重发
     * @return
     */
    Response<String> sendMsg(String exchange, String routingKey, Object msg, boolean needRePublish);
}
