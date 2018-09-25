package com.sunnsoft.exception;

/**
 * 
 * @author llade
 * 继承RuntimeException，是为了使容器能够捕捉改异常，而无需try catch块
 * 能使用exceptionType 在代码块之间传递部分异常信息。
 * 
 */
public class SystemException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8052685566184727751L;

	public SystemException(String message) {
		super(message);
	}
	
	public SystemException(String message, int exceptionType) {
		super(message);
		this.exceptionType = exceptionType;
	}

	public SystemException(String message, Throwable e) {
		super(message, e);
	}
	
	public SystemException(String message, Throwable e, int exceptionType) {
		super(message, e);
		this.exceptionType = exceptionType;
	}
	
	private int exceptionType = 0;

	public int getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(int exceptionType) {
		this.exceptionType = exceptionType;
	}
	
	

}
