package com.zxd.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhuxianda on 2018/2/13.
 */
public class TestLog4j2Kafka {
    public static final Logger Test = LoggerFactory.getLogger("test_kafka");


    public static void main(String[] args) throws InterruptedException {
        for(int i = 0;i <= 10000; i++) {
            Test.info("This is Message [" + i + "] from log4j producer .. ");
            System.out.println(i);
            Thread.sleep(1000);
        }
    }
}