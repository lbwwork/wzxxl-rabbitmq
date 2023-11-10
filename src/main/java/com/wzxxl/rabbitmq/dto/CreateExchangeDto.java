package com.wzxxl.rabbitmq.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * @author lbw
 * @version 1.0
 * @since 2023/1/31
 */
@Data
public class CreateExchangeDto {

    /**
     * 交换机名称
     */
    @NotEmpty
    private String exchange;

    /**
     * 类型
     */
    @NotEmpty
    private String type;

    /**
     * 队列名称
     */
    @NotEmpty
    private String queue;

    /**
     * 路由键
     */
    @NotEmpty
    private String routingKey;

    /**
     * 是否持久化
     */
    private boolean durable;

    /**
     * 是否排外
     */
    private boolean exclusive;

    /**
     * 是否自动删除
     */
    private boolean autoDelete;

    /**
     * 队列额外参数
     */
    private Map<String, Object> queueArguments;

    /**
     * 交换机额外参数
     */
    private Map<String, Object> exchangeArguments;
}
