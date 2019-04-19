package com.wust.web;

import com.wust.exception.MyException;
import com.wust.utils.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/anon")
public class HelloController{

	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

	@ResponseBody
	@ApiOperation("测试页")
	@RequestMapping(value = "/json", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String json() throws MyException {

		String errorCode = StringUtil.getStringRandom(12);
		//打印12位随机字符串,用于日至搜索。
		logger.error("errorCode-----"+errorCode);

		throw new MyException(errorCode);
	}

	@ApiOperation("测试页")
	@RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String hello(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		return "anon/hello";

	}

	/**
	 * WebSocket测试
	 * @return
	 */
	@ApiOperation("测试页")
	@RequestMapping(value = "/socket", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String websocket(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		return "anon/socket";

	}
}
