package com.wust.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;

public class PasswordHelper {

	private static String algorithmName = "md5";
	private static int hashIterations = 2;


	/**
	 * 利用密码生成MD5加密密码
	 * @param pwd
	 * @return
	 */
	public static String encryptPassword(String pwd) {
		String pwdMD5 = new SimpleHash(algorithmName, pwd, ByteSource.Util.bytes("wust"), hashIterations).toHex();

		return pwdMD5;
	}


	@Test
	public void pwdHelper() {

		String pwd = "185";
		String newPassword = new SimpleHash(algorithmName, pwd, ByteSource.Util.bytes("wust"), hashIterations).toHex();

		System.out.println("Password:" + newPassword);
	}

}
