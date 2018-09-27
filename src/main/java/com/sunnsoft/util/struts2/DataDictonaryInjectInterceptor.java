package com.sunnsoft.util.struts2;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.sunnsoft.util.base.DataDictonary;

public class DataDictonaryInjectInterceptor extends
		SpringApplicationContextInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DataDictonary dataDictonary;
	private DataDictonary userDictonary;

	@Override
	public void init() {
		this.dataDictonary = (DataDictonary)this.appContext.getBean("dataDictonary");
		this.userDictonary = (DataDictonary)this.appContext.getBean("userDictonary");
		super.init();
	}



	@Override
	public String intercept(ActionInvocation invoke) throws Exception {
		ValueStack stack = invoke.getStack();
		stack.set("_dic", dataDictonary);
		stack.set("_userDic", userDictonary);
//		System.out.println(this.dataDictonary);
		return invoke.invoke();
	}

	
}
