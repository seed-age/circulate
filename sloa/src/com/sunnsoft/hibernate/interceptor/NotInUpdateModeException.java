package com.sunnsoft.hibernate.interceptor;

/**
 * 
 * @author llade
 * 调用Helper类的时候，使用了只能在update数据库的时候调用的方法。会抛出此异常。
 *
 */
public class NotInUpdateModeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
