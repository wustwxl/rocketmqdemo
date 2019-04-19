package com.wust.rocketmq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;

public class ProducerTest {

    @Test
    public  void ProducerTest() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        /*
        首先在conf/broker.conf配置IP

        服务器启动经纪人方法
        nohup sh bin/mqbroker -n IP:PORT -c conf/broker.conf autoCreateTopicEnable=true &
         */

        //Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer("please_rename_unique_group_name");
        // Specify name server addresses.
        producer.setNamesrvAddr("IP:PORT");
        //Launch the instance.
        producer.start();

        //生产者关闭VIP通道
        producer.setVipChannelEnabled(false);

        for (int i = 0; i < 100; i++) {

            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ: " + i).getBytes() /* Message body */
            );
            //Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);

        }

        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }
}
