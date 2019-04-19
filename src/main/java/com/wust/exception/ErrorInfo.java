package com.wust.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * @author : F7687967
 * Date: 2017/10/16
 * Time: 下午 05:00
 * Description: 统一异常处理返回对象类
 */
@Setter
@Getter
public class ErrorInfo<T> {

	public static final Integer OK = 0;
	public static final Integer ERROR = 1;

	//code：消息类型
	private Integer code;
	//info：消息内容
	private String info;
	//url：请求的url
	private String url;
	//data：返回异常信息
	private Object data;

}
