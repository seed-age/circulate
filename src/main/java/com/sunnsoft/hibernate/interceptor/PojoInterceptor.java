package com.sunnsoft.hibernate.interceptor;

import java.lang.reflect.ParameterizedType;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 
 * @author llade
 * 本平台的Hibernate拦截器的父类，只要继承了这个父类，并且注册为Spring的bean。
 * 则会自动加入到InterceptorManager的拦截器队列里。
 * @param <T> 需要拦截的具体实体的类。
 */
@SuppressWarnings("unchecked")
public abstract class PojoInterceptor <T>{
	
	/**
	 * 自动注入 的全局唯一的InterceptorManager实例
	 */
	@Resource
	protected InterceptorManager interceptorManager;
	/**
	 * 实体类对象
	 */
	protected Class<T> entityClass;
	
	/**
	 * 初始化方法，
	 * 1.获取泛型类型。
	 * 2.全局唯一的InterceptorManager实例中注册（加入到拦截器队列）
	 */
	@PostConstruct
	public void init(){
		 entityClass =(Class<T>) ((ParameterizedType) getClass()
                 .getGenericSuperclass()).getActualTypeArguments()[0];
		 this.interceptorManager.addInterceptor(this);
	}

	/**
	 * 由InterceptorManager实例调用，用来确定拦截器的泛型类。
	 * @return
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}

	
	/**
	 * 实体的CRUD的时候，会调用的方法
	 * @param insert 当 insert的时候为true,其他时候为false
	 * @param update 当 update的时候为true,其他时候为false
	 * @param delete 当 delete的时候为true,其他时候为false
	 * @param load 当 load的时候为true,其他时候为false
	 * @param id 实体的id
	 * @param currentValues 对于insert、delete、load，表示实体的各个属性的值，对于update，表示update后的值，可以直接修改数组元素引用的值，来达到修改update后的值得目的。
	 * @param previousValues 只有update时候有效，表示update前各个属性的值，用来和update后的值做对比。
	 * @param propertyNames 和value值对应的属性名。
	 * @return 按约定，对属性做了修改的，返回true,否则返回false;
	 */
	public abstract boolean onEvent(
			InterceptorHelper helper);
	
}
