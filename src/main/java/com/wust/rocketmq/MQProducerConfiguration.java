package com.wust.rocketmq;

import com.wust.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.*;


/**
 * 生产者
 */

//@Configuration
@Slf4j
@Service
public class MQProducerConfiguration {

    /**
     * 发送同一类消息的设置为同一个group，保证唯一,默认不需要设置，rocketmq会使用ip@pid(pid代表jvm名字)作为唯一标示
     */
    @Value("${rocketmq.producerGroup}")
    private String groupName;

    @Value("${rocketmq.namesrvaddr}")
    private String namesrvAddr;

    private DefaultMQProducer producer;

    //@PostContruct是spring框架的注解，在方法上加该注解会在项目启动的时候执行该方法，也可以理解为在spring容器初始化的时候执行该方法。
    @PostConstruct
    public void produder()  {

        if(StringUtil.IsNullOrEmpty(groupName)){
            log.info("RocketMQ.PARAMM_NULL: groupName is blank");
            throw new NullPointerException();
        }

        if (StringUtil.IsNullOrEmpty(namesrvAddr)) {
            log.info("RocketMQ.PARAMM_NULL: nameServerAddr is blank");
            throw new NullPointerException();
        }

        producer = new DefaultMQProducer(groupName);
        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(namesrvAddr);
        //生产者关闭VIP通道
        producer.setVipChannelEnabled(false);

        //如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
        //producer.setInstanceName(instanceName);

        try {
            producer.start();
            log.info(String.format("producer is start ! groupName:[%s],namesrvAddr:[%s]", groupName, namesrvAddr));

        } catch (MQClientException e) {
            log.error("producer is error");
            e.printStackTrace();
        }
    }

    /**
     * 同步发送消息
     *
     * 用于广泛的场景，如重要的通知消息，短信通知，短信营销系统等。
     * @param message
     * @return
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     * @throws MQBrokerException
     */
    public String syncSend(Message message) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        StopWatch stop = new StopWatch();
        stop.start();
        SendResult result = producer.send(message);
        System.out.println("发送响应：MsgId:" + result.getMsgId() + "，发送状态:" + result.getSendStatus());
        stop.stop();
        return "{\"MsgId\":\""+result.getMsgId()+"\","+"\"UsedTime\":\""+stop.getTime()+"ms\"}";
    }

    /**
     * 异步发送消息
     * 通常用于响应时间敏感的业务场景
     * @param message
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     */
    public void asyncSend(Message message,Integer id) throws InterruptedException, RemotingException, MQClientException {

        producer.setRetryTimesWhenSendAsyncFailed(0);

        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.printf("%-5d Send OK, MsgId:%s %n", id, sendResult.getMsgId());
            }
            @Override
            public void onException(Throwable e) {
                System.out.printf("%-5d Send Exception, %s %n", id, e);
                e.printStackTrace();
            }
        });
    }

    /**
     * 单向模式发送消息
     * 用于需要中等可靠性的情况，例如日志收集
     * @param message
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     */
    public void onewaySend(Message message,Integer id) throws InterruptedException, RemotingException, MQClientException {

        StopWatch stop = new StopWatch();
        stop.start();
        producer.sendOneway(message);
        stop.stop();
        System.out.println(" "+ id +" Send OK,"+" it Used "+stop.getTime()+"ms");
    }

    /**
     * 订单消息
     * RocketMQ使用FIFO顺序提供有序消息。
     * @param message
     * @param orderId
     * @return
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     * @throws MQBrokerException
     */
    public String orderedSend(Message message,Integer orderId) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {

        StopWatch stop = new StopWatch();
        stop.start();
        SendResult result = producer.send(message, new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {

                //这里的arg就是orderId传进来的
                Integer id = (Integer) arg;
                //取模决定放在哪个数据库
                int index = id % mqs.size();
                return mqs.get(index);
            }
        }, orderId);
        System.out.println("发送响应：MsgId:" + result.getMsgId() + "，发送状态:" + result.getSendStatus());
        stop.stop();
        return "{\"MsgId\":\""+result.getMsgId()+"\",\"orderId\":\""+orderId+"\","+"\"UsedTime\":\""+stop.getTime()+"ms\"}";
    }

    /**
     * 事务消息(多用于跨系统转账交易)
     *
     * 创建事务生成器
     * 使用TransactionMQProducer类创建生成器客户端，
     * 指定唯一的producerGroup，并设置自定义线程池来处理检查请求。
     * 执行本地事务后，需要根据执行结果回复MQ。
     *
     * @return
     * @throws UnsupportedEncodingException
     * @throws MQClientException
     */
    public void transactionalSend() throws MQClientException, UnsupportedEncodingException {

        // 未决事务，MQ服务器回查客户端
        // 当RocketMQ发现`Prepared消息`时，会根据这个Listener实现的策略来决断事务
        TransactionListener transactionListener = new MQTransactionListenerImpl();
        // 构造事务消息的生产者
        TransactionMQProducer producer = new TransactionMQProducer(groupName);
        producer.setNamesrvAddr(namesrvAddr);
        // 设置事务决断处理类
        producer.setTransactionListener(transactionListener);

        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
        producer.setExecutorService(executorService);

        producer.start();

        // 构造MSG
        Message msg = new Message("TopicTest", "Tags", "KEY", ("transactionalSend").getBytes(RemotingHelper.DEFAULT_CHARSET));

        /**
         * sendMessageInTransaction
         * 1.发送消息
         * 2.如果消息发送成功，处理与消息关联的本地事务单元
         * 3.结束事务 == endTransaction方法会将请求发往broker(mq server)去更新事务消息的最终状态
         *   3.1 根据sendResult找到Prepared消息,sendResult包含事务消息的ID
         *   3.2 根据localTransaction更新消息的最终状态
         *   3.3 如果endTransaction方法执行失败，数据没有发送到broker，导致事务消息的状态更新失败，broker会有回查线程定时（默认1分钟）扫描每个存储事务状态的表格文件，
         *       如果是已经提交或者回滚的消息直接跳过,
         *       如果是prepared状态则会向Producer发起CheckTransaction请求，Producer会调用DefaultMQProducerImpl.checkTransactionState()方法来处理broker的定时回调请求，
         *       而checkTransactionState会调用我们的事务设置的决断方法来决定是回滚事务还是继续执行，
         *       最后调用endTransactionOneway让broker来更新消息的最终状态。
         *
         *  以跨银行转账:Bob向Smith转账100块为例
         *  如果Bob的账户的余额已经减少，且消息已经发送成功，
         *  则Smith端开始消费这条消息，这个时候就会出现消费失败和消费超时两个问题:
         *  解决超时问题的思路就是一直重试，直到消费端消费消息成功，
         *  解决消息重复的问题就是根据消息ID进行日志记录,已消费的则不再消费,
         *  消费失败问题可进行人工解决,(回滚整个流程的话系统复杂度将大大提升)
         */
        SendResult result = producer.sendMessageInTransaction(msg, "处理本地事务");

        System.out.println("发送响应：MsgId:" + result.getMsgId() + "，发送状态:" + result.getSendStatus());

    }

}