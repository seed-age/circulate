package com.sunnsoft.sloa.hibernate;

import org.springframework.stereotype.Component;

import com.sunnsoft.hibernate.interceptor.InterceptorHelper;
import com.sunnsoft.hibernate.interceptor.PojoInterceptor;
import com.sunnsoft.sloa.db.vo.User;

/**
 * 
 * @author llade
 * Hibernate拦截器例子
 */
@Component
public class UserInterceptor extends PojoInterceptor<User> {

	@Override
	public boolean onEvent(InterceptorHelper helper) {
		if(helper.isUpdate()){//如果是update
			if(helper.dirty("accountName")){//判断用户名是否被更改了。
				//helper.setValue("userName", "xxxxx"); //设置新值
			}
		}
		if(helper.isInsert()){//如果是insert
			//......
		}
		return false;//按约定，对属性做了修改的，返回true,否则返回false;
	}

}
