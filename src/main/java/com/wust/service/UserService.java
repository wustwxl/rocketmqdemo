package com.wust.service;

import com.wust.entity.Users;

import java.util.List;

public interface UserService {

	/**
	 * 插入用户数据
	 * @param record
	 * @return
	 */
	int insert(Users record);

	/**
	 * 根据条件查询用户列表
	 * @param record
	 * @return
	 */
	List<Users> selectByRecord(Users record);

	/**
	 * 查询所有用户列表
	 * @return
	 */
	List<Users> selectAllUser();

	/**
	 * 根据手机号查询指定用户
	 * @param phone
	 * @return
	 */
	Users selectByPhone(String phone);

	/**
	 * 根据手机号删除指定用户
	 * @param phone
	 * @return
	 */
	int deleteByPhone(String phone);

	/**
	 * 更新用户数据
	 * @param record
	 * @return
	 */
	int updateByRecord(Users record);

	/**
	 * 更新用户手机号
	 * @param newPhone
	 * @param oldPhone
	 * @return
	 */
	int updatePhone(String newPhone, String oldPhone);

	/**
	 * 查询满足条件用户数目
	 * @return
	 */
	Long countByRecord(Users record);

	/**
	 * 根据手机号查询注册用户数量
	 * @param phone
	 * @return
	 */
	int countByPhone(String phone);

	/**
	 * 根据手机号更换密码
	 * @param phone
	 * @param pwd
	 * @return
	 */
	int updatePwd(String phone, String pwd);

	/**
	 * 从缓存中获取用户信息
	 * @param phone
	 * @return
	 */
	Users getUserCache(String phone);

	/**
	 * 在缓存中存储信息
	 * @param entity
	 * @return
	 */
	boolean insertUserCache(Users entity);

	/**
	 * 增加输入密码错误次数
	 * @param key
	 */
	void incrLoginCount(String key);

	/**
	 * 获取密码输错次数
	 * @param key
	 * @return
	 */
	String getLoginCount(String key);

	/**
	 * 设置过期时间
	 * @param key
	 */
	void expire(String key);

	/**
	 * 重置密码输错次数
	 * @param key
	 */
	void removeCount(String key);

	/**
	 * 删除缓存
	 * @param key
	 */
	void removeKey(String key);
}
