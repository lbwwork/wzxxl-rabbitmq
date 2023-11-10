package com.wzxxl.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author libaowen
 * @Date 2023/10/23 16:51
 * @Version 1.0
 */
@Configuration
public class SendMessageConfig {

    /**
     * 手机短信队列
     */
    public static final String MESSAGE_QUEUE = "message_queue";

    /**
     * 浙政钉队列
     */
    public static final String ZZD_QUEUE = "zzd_queue";

    /**
     * 消息提醒交换机
     */
    public static final String SEND_MESSAGE_EXCHANGE = "send_message_exchange";

    /**
     * 手机短信路由key
     */
    public static final String MESSAGE_ROUTING_KEY = "message_routing_key";

    /**
     * 浙政钉路由key
     */
    public static final String ZZD_ROUTING_KEY = "zzd_routing_key";

    /**
     * 死信队列
     */
    public static final String MEET_REMIND_DEAD_LETTER_QUEUE = "meet_remind_dead_letter_queue";

    /**
     * 死信队列路由key
     */
    public static final String MEET_REMIND_DEAD_LETTER_QUEUE_ROUTING_KEY = "meet_remind_dead_letter_queue_routing_key";

    /**
     * 死信交换机
     */
    public static final String MEET_REMIND_DEAD_LETTER_EXCHANGE = "meet_remind_dead_letter_exchange";

    /**
     * 消息提醒交换机
     * @return
     */
    @Bean(SEND_MESSAGE_EXCHANGE)
    public DirectExchange delayExchange(){
        return new DirectExchange(SEND_MESSAGE_EXCHANGE, true, false);
    }

    /**
     * 手机短信队列
     * @return
     */
    @Bean(MESSAGE_QUEUE)
    public Queue messageQueue(){
        return new Queue(MESSAGE_QUEUE, true, false, false, null);
    }


    /**
     * 浙政钉队列
     * @return
     */
    @Bean(ZZD_QUEUE)
    public Queue zzdQueue(){
        return new Queue(ZZD_QUEUE, true, false, false, null);
    }

    /**
     * 绑定手机短信队列
     * @param queue
     * @param exchange
     * @return
     */
    @Bean("messageQueueBinding")
    public Binding messageQueueBind(@Qualifier(MESSAGE_QUEUE)Queue queue,
                                  @Qualifier(SEND_MESSAGE_EXCHANGE)DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(MESSAGE_ROUTING_KEY);
    }

    /**
     * 绑定浙政钉队列
     * @param queue
     * @param exchange
     * @return
     */
    @Bean("zzdQueueBinding")
    public Binding zzdQueueBind(@Qualifier(ZZD_QUEUE)Queue queue,
                                  @Qualifier(SEND_MESSAGE_EXCHANGE)DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ZZD_ROUTING_KEY);
    }
}
