package com.wust.rocketmq;

import com.wust.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 消费者
 */

//@Configuration
@Slf4j
//@Component
@Service
public class MQConsumerConfiguration{

    @Value("${rocketmq.namesrvaddr}")
    private String namesrvAddr;
    @Value("${rocketmq.conumerGroup}")
    private String groupName;

    @Autowired
    private MQConsumeMsgListenerProcessor mqMessageListenerProcessor;


    /**
     * 顺序消费:适用于订单消息消费者
     *
     * 如果是顺序消息,这边的监听就要使用MessageListenerOrderly监听
     * 并且,返回结果也要使用ConsumeOrderlyStatus
     */
    public void orderedConsumer() {

        if (StringUtil.IsNullOrEmpty(groupName)){
            log.info("RocketMQ.PARAMM_NULL: groupName is blank");
            throw new NullPointerException();
        }
        if (StringUtil.IsNullOrEmpty(namesrvAddr)){
            log.info("RocketMQ.PARAMM_NULL: namesrvAddr is blank");
            throw new NullPointerException();
        }

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setVipChannelEnabled(false);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        //消费线程最小数量,默认20
        //consumer.setConsumeThreadMin(30);
        //消费线程最大数量,默认64
        //consumer.setConsumeThreadMax(100);

        Set set = new HashSet();

        try {
            consumer.subscribe("TopicTest", "*");

            consumer.registerMessageListener(new MessageListenerOrderly() {

                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs,
                                                           ConsumeOrderlyContext context) {

                    //设置自动提交,如果不设置自动提交就算返回SUCCESS,消费者关闭重启 还是会重复消费的
                    context.setAutoCommit(true);

                    try {
                        for (MessageExt msg:msgs) {

                            /**
                             * 解决重复消费问题
                             * 保证每条消息都有唯一编号且保证消息处理成功与去重表的日志同时出现
                             * 即利用一张日志表来记录已经处理成功的消息的ID，如果新到的消息ID已经在日志表中，那么就不再处理这条消息。
                             */
                            String msgId = msg.getMsgId();
                            if (set.contains(msgId)){
                                System.out.println(" 重复消费信息 ==> msgId: "+msgId+ " ,content: " + new String(msg.getBody()));
                            }else {
                                System.out.println(" 消费信息==> 当前线程:"+Thread.currentThread().getName()+" ,msgId: "+msgId+" ,queneID: "+msg.getQueueId()+ " ,content: " + new String(msg.getBody()));
                                set.add(msgId);

                                // 此处模拟业务处理
                                Thread.sleep(500);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //如果出现异常,消费失败，挂起消费队列一会会，稍后继续消费
                        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                    }
                    //消费成功
                    return ConsumeOrderlyStatus.SUCCESS;
                }
            });

            consumer.start();
            log.info("consumer is start !!! groupName:{},namesrvAddr:{}",groupName,namesrvAddr);

        } catch (Exception e) {
            log.error("consumer is error");
            e.printStackTrace();
        }
    }


}
