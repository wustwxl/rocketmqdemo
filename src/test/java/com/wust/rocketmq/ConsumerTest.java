package com.wust.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class ConsumerTest {

    public static void main(String[] args) throws MQClientException {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name");

        // Specify name server addresses.
        consumer.setNamesrvAddr("IP:PORT");

        //消费者关闭VIP通道
        consumer.setVipChannelEnabled(false);

        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 设置消费模型，集群还是广播，默认为集群
        //consumer.setMessageModel(MessageModel.CLUSTERING);

        try {
            /**
             * 设置该消费者订阅的主题和tag，如果是订阅该主题下的所有tag，则tag使用*;
             * 如果需要指定订阅该主题下的某些tag，则使用||分割，例如tag1||tag2||tag3
             */

            // 订阅PushTopic下所有消息
            consumer.subscribe("TopicTest", "*");

            /*String[] topicTagsArr = topics.split(";");
            for (String topicTags : topicTagsArr) {
                String[] topicTag = topicTags.split("~");
                consumer.subscribe(topicTag[0],topicTag[1]);
            }*/


            // 在此监听中消费信息，并返回消费的状态信息(方法一:自己封装实现了监听器的接口)
            // consumer.registerMessageListener(mqMessageListenerProcessor);

            // 在此监听中消费信息，并返回消费的状态信息(方法二:调用消息监听的方法)
            consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
                try {
                    for (Message message : list) {
                        System.err.println("消费消息: " + new String(message.getBody()));//输出消息内容
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
            });

            consumer.start();
            System.out.println("consumer is start !!!");

        } catch (Exception e) {
            System.out.println("consumer is error");
            e.printStackTrace();
        }
    }
}
