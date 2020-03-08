/**
 * @Title: UserStatus.java
 * @Package com.shawn.sys.util
 * @author liuzyhn
 * @date 2016年3月11日 下午3:02:16
 * @version V1.0
 */
package com.shawn.sys.util;

/**
 * @ClassName: UserStatus
 * @Description: 用户状态
 */
public enum UserStatus {

	LOCKED("-1", "锁定"),
	NORMAL("0", "解锁");
	//UNLOCK("1", "解锁");

	private String code;
	private String text;
	private UserStatus(String code, String text) {
		this.code = code;
		this.text = text;
	}
	/**
	 * @return 获取 code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @return 获取 text
	 */
	public String getText() {
		return text;
	}
}
