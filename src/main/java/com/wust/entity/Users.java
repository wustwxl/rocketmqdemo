package com.wust.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

//@NonNull:标识对象是否为空，为空则抛出异常
//@Getter:自动生成Getter方法
//@Setter:自动生成Setter
//@ToString:覆盖tostring方法
//@EqualsAndHashCode:覆盖equal和hashCode方法
//@Data:@Getter/@Setter, @ToString, @EqualAndHashCode等组合
//@Slf4j:默认使用slf4j的日志对象
@Slf4j
@Data
public class Users implements Serializable {

    /**
     * @Description: 将对象进行序列化, 可实现在Redis中存储该对象
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String pwd;

    private String phone;

    private Integer age;

    private Integer sex;

    private String createTime;

    private Long creater;

    private String updateTime;

    private Long updater;

    private Integer deleted;

    //用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.
    private Integer status;

    // 一个用户具有多个角色
    private List<Roles> roleList;

    /**
     * 注册用户部分信息
     * @return
     */
    public String toStringInfo() {
        return "注册用户信息： [姓名=" + username
                + ", 联系方式=" + phone + ", 年龄="
                + age + ", 性别=" + sex + ", 状态=" + status + "]";
    }

    /**
     * 密码盐
     * @return
     */
    public byte[] getCredentialsSalt() {

        return "wust".getBytes();
    }
}