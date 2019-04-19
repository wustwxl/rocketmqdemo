package com.wust.vo;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * @author : F7687967
 * Date: 2017/10/16
 * Time: 上午 08:51
 * Description: 统一返回类型
 */
@Data
public class MessageVo {
    private Integer code;
    private String info;
    private Object data;
}
