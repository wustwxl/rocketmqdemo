package com.wust.rocketmq;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 实现TransactionListener接口
 * “executeLocalTransaction”方法用于在发送半消息成功时执行本地事务。它返回三种事务状态之一。
 * “checkLocalTransaction”方法用于检查本地事务状态并响应MQ检查请求。它还返回三种事务状态之一。
 *
 * 交易状态
 * 事务性消息有三种状态：
 * （1）TransactionStatus.CommitTransaction：提交事务，这意味着允许消费者使用此消息。
 * （2）TransactionStatus.RollbackTransaction：回滚事务，表示该消息将被删除而不允许使用。
 * （3）TransactionStatus.Unknown：中间状态，表示需要MQ检查以确定状态。
 *
 *
 * 场景模拟
 * 跨银行转账:Bob向Smith转账100块。
 */
public class MQTransactionListenerImpl implements TransactionListener {

    @Override
    //执行本地事务的，一般就是操作DB相关内容
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {

        try {
            String msgBody = new String(msg.getBody(), "utf-8");
            System.out.println("executeLocalTransaction()==="+arg);
            // 执行本地业务的时候，再插入一条数据到事务表中，供checkLocalTransaction进行check使用
            // 避免doBusinessCommit业务成功，但是未返回Commit
            doBusinessCommit(msg.getKeys(),msgBody);
            return LocalTransactionState.COMMIT_MESSAGE;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            //此处会触发调用checkLocalTransaction()方法
            return LocalTransactionState.UNKNOW;
        }

    }

    @Override
    //用来提供给broker进行回查本地事务消息的，把本地事务执行的结果存储到redis或者DB中都可以，为回查做数据准备
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {

        System.out.println("checkLocalTransaction === 回查本地事务消息");
        Boolean result=checkBusinessStatus(msg.getKeys());
        if(result){
            return LocalTransactionState.COMMIT_MESSAGE;
        }else{
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }

    public static void doBusinessCommit(String messageKey,String msgbody){
        System.out.println("do something in DataBase");
        System.out.println("insert事务消息到redis或者DB中，消息执行成功，messageKey为："+messageKey+"，msgbody为："+msgbody);
    }

    public static Boolean checkBusinessStatus(String messageKey){

        Boolean result = true;//此处默认本地事务执行成功,线上环境从redis或者DB中查
        if(result){
            System.out.println("查询数据库 messageKey为"+messageKey+"的消息已经消费成功了，可以提交消息");
            return true;
        }else{
            System.out.println("查询数据库 messageKey为"+messageKey+"的消息不存在或者未消费成功，可以回滚消息");
            return false;
        }
    }

}