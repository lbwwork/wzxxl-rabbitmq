package com.wzxxl.rabbitmq.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lbw
 * @version 1.0
 * @since 2023/1/29
 */
@Data
public class TestDto implements Serializable {
    private static final long serialVersionUID = 1L;

    public String code;

    public byte[] file;
}
