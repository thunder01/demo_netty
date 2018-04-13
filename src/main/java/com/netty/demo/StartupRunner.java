package com.netty.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

/**
 * @author 冯志宇 2018/4/11
 */
@Order(1)
public class StartupRunner implements CommandLineRunner{
    @Autowired
    private DiscardServer discardServer;
    @Override
    public void run(String... strings) throws Exception {
        System.out.println("启动任务1");
        discardServer.run();
    }
}
