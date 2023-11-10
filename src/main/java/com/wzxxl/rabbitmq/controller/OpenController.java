package com.wzxxl.rabbitmq.controller;


import com.tuojing.pbrabbitmq.api.dto.CreateExchangeDto;
import com.tuojing.pbrabbitmq.api.dto.CreateQueueAndBindDto;
import com.tuojing.pbrabbitmq.api.service.RabbitMqService;
import com.tuojing.sdk.api.annotations.LoginRequired;
import com.tuojing.sdk.api.domain.dto.MessageDto;
import com.tuojing.sdk.api.utils.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lbw
 * @version 1.0
 * @since 2023/1/31
 */
@RestController
@RequestMapping("/rabbitmq/open")
public class OpenController {

    @Autowired
    private RabbitMqService rabbitMqService;

    @PostMapping("/createExchange")
    @ApiOperation(value = "创建交换机并绑定一个队列", httpMethod = "POST")
    public Response<String> createExchange(@RequestBody CreateExchangeDto createExchangeDto) {
        return rabbitMqService.createExchangesAndBindQueue(createExchangeDto.getExchange(), createExchangeDto.getType(),
                createExchangeDto.getQueue(), createExchangeDto.isDurable(), createExchangeDto.isExclusive(),
                createExchangeDto.isAutoDelete(), createExchangeDto.getQueueArguments(), createExchangeDto.getRoutingKey(), createExchangeDto.getExchangeArguments());
    }

    @PostMapping("/createQueue")
    @ApiOperation(value = "创建队列并绑定到交换机", httpMethod = "POST")
    public Response<String> testCreateQueue(@RequestBody CreateQueueAndBindDto createQueueAndBindDto) {
        return rabbitMqService.createQueueAndBind(createQueueAndBindDto.getExchange(), createQueueAndBindDto.getQueue(),
                createQueueAndBindDto.isDurable(), createQueueAndBindDto.isExclusive(), createQueueAndBindDto.isAutoDelete(),
                createQueueAndBindDto.getArguments(), createQueueAndBindDto.getRoutingKey());
    }

    @PostMapping("/sendMessage")
    @ApiOperation(value = "发送消息", httpMethod = "POST")
    @LoginRequired
    public Response<String> sendMsg(@RequestBody MessageDto messageDto) {
        return rabbitMqService.sendMsg(messageDto.getExchange(), messageDto.getRoutingKey(), messageDto.getMessage(), messageDto.isNeedRePublish());
    }
}
