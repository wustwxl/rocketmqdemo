package com.wust.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 监听器
 */
@Configuration
@Slf4j
public class MQConsumeMsgListenerProcessor implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if(msgs.isEmpty() || msgs.size() == 0){
            log.info("接收到的消息为空，不做任何处理");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        MessageExt messageExt = msgs.get(0);
        log.info("接收到的消息是："+messageExt.toString());

        String msg = new String(messageExt.getBody());
        log.info("接收到的消息是："+msg);

        if(messageExt.getTopic().equals("你的topic")){
            if(messageExt.getTags().equals("你的tag")){
                int reconsumeTimes = messageExt.getReconsumeTimes();
                if(reconsumeTimes == 3){
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                //TODO 处理对应的业务逻辑

                System.out.println("接收到了消息："+ " 你的topic " + " 你的tag " );
            }
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
