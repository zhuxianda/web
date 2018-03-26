package com.zxd.controller; /**
 * Created by zhuxianda on 2018/2/13.
 */

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Properties;

@Component
public class TestKafkaConsumer implements InitializingBean {

    private Properties props = new Properties();

    @Autowired
    private KafkaProperties properties;

    @Override
    public void afterPropertiesSet() throws Exception {
        props.put("bootstrap.servers", properties.bootstrapServers);
        props.put("group.id", properties.groupId);
        props.put("enable.auto.commit", properties.enableAutoCommit);
        props.put("auto.commit.interval.ms", properties.autoCommitIntervalMs);
        props.put("key.deserializer", properties.keySerializer);
        props.put("value.deserializer", properties.valueSerializer);

    }

    public KafkaConsumer<String, String> getInstance() {
        KafkaConsumer<String, String> consumer = null;
        try {
            consumer = new KafkaConsumer<String, String>(props);
            System.out.println("参数呀1!");
            System.out.println(props.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return consumer;
    }

    public void run() {

        try {
            System.out.print("Begin to process Kafuka");

            KafkaConsumer<String, String> consumer = getInstance();
            consumer.subscribe(Arrays.asList(properties.topic));
            System.out.println(properties);
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    String msg = record.value();
                    try {
                        System.out.println("process start");
                        System.out.println("process " + msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("LogTask error");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        TestKafkaConsumer testKafkaConsumer = ac.getBean(TestKafkaConsumer.class);
        testKafkaConsumer.run();

    }

}
