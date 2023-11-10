package com.wzxxl.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author lbw
 * @version 1.0
 * @since 2023/1/29
 */
@Configuration
public class RabbitMqUtil {
    @Value("${spring.rabbitmq.addresses}")

    private String addresses;
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

    /**
     * 创建连接工厂，使用懒汉式单例模式
     */
    private static volatile ConnectionFactory connectionFactory;


    private synchronized void initConnectionFactory() {
        if (connectionFactory == null) {
            connectionFactory = new ConnectionFactory();
            String[] split = addresses.split(":");
            connectionFactory.setHost(split[0]);
            //设置端口号
            connectionFactory.setPort(Integer.parseInt(split[1]));
            //设置主机名称
            connectionFactory.setVirtualHost(virtualHost);
            //设置访问虚拟主机的用户名和密码
            connectionFactory.setUsername(username);
            connectionFactory.setPassword(password);
        }
    }

    //提供一个创建连接的方法
    public Connection getConnection() {
        try {
            if (connectionFactory == null) {
                initConnectionFactory();
            }
            return connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeChannelAndConnection(Channel channel, Connection connection) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
