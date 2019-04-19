package com.wust.web;


import com.wust.rocketmq.MQConsumerConfiguration;
import com.wust.rocketmq.MQProducerConfiguration;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/anon")
public class RocketMqController {

    @Autowired
    MQProducerConfiguration producer;

    @Autowired
    MQConsumerConfiguration consumer;

    @GetMapping("/consumer")
    public String orderedConsumer() {
        consumer.orderedConsumer();
        return "OK";
    }

    @GetMapping("/syncSend")
    public String syncSend() {

        try {
            for (int i = 0; i < 100; i++) {

                Message msg = new Message("TopicTest" /* Topic */,
                        "TagA" /* Tag */,
                        ("syncSend: " + i).getBytes() /* Message body */
                );

                String sendResult = producer.syncSend(msg);
                System.out.printf("%s%n", sendResult);
            }
            return "OK";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }


    @GetMapping("/asyncSend")
    public String asyncSend() {

        try {
            for (int i = 0; i < 100; i++) {

                Message msg = new Message("TopicTest" /* Topic */,
                        "TagA" /* Tag */,
                        ("asyncSend: " + i).getBytes() /* Message body */
                );
                producer.asyncSend(msg,i);
            }
            return "OK";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    @GetMapping("/onewaySend")
    public String onewaySend() {

        try {
            for (int i = 0; i < 100; i++) {

                Message msg = new Message("TopicTest" /* Topic */,
                        "TagA" /* Tag */,
                        ("onewaySend: " + i).getBytes() /* Message body */
                );
                producer.onewaySend(msg,i);
            }
            return "OK";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    @GetMapping("/orderedSend")
    public String orderedSend() {

        //String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};

        try {
            for (int i = 0; i < 100; i++) {
                //Create a message instance, specifying topic, tag and message body.
                Message msg = new Message("TopicTest", "orderTag" /*tags[i % tags.length]*/ , "KEY_" + i,
                        ("orderedSend " + i).getBytes());

                String sendResult = producer.orderedSend(msg,i);
                System.out.printf("%s%n", sendResult);
            }
            return "OK";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    @GetMapping("/transactionalSend")
    public String transactionalSend() throws MQClientException, UnsupportedEncodingException {

       producer.transactionalSend();
       return "OK";
    }

}
