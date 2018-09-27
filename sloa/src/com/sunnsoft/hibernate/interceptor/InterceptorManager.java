package com.sunnsoft.hibernate.interceptor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.*;

/**
 * 
 * @author llade
 * Hibernate拦截器总管
 */
@SuppressWarnings("unchecked")
public class InterceptorManager extends EmptyInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5997938489917895206L;
	
	private Map<Class, Set<PojoInterceptor>> interceptors = new HashMap<Class, Set<PojoInterceptor>> (20);
	
	/**
	 * 添加拦截器，给PojoInterceptor子类调用的组装器方法。
	 * @param interceptor
	 */
	public void addInterceptor(PojoInterceptor interceptor){
		Set<PojoInterceptor> interceptorSet = interceptors.get(interceptor.getEntityClass());
		if(interceptorSet == null){
			interceptorSet = new HashSet<PojoInterceptor>();
			this.interceptors.put(interceptor.getEntityClass(), interceptorSet);
		}
		interceptorSet.add(interceptor);
	}
	
	/**
	 * 遍历拦截器并调用。
	 * @param entity
	 * @param helper
	 * @return
	 */
	private boolean process(Object entity,InterceptorHelper helper){
		boolean result = false;
		Set<PojoInterceptor> interceptorSet = interceptors.get(entity.getClass());
		if(interceptorSet!=null){
			for (Iterator<PojoInterceptor> iterator = interceptorSet.iterator(); iterator.hasNext();) {
				PojoInterceptor pojoInterceptor =  iterator.next();
				boolean b = pojoInterceptor.onEvent(helper);
				if(b) {
					result = b; //按约定，如果一个拦截器返回真值，则表示结果被改动过。
				}
			}
		}
		return result;
	}

	/**
	 * delete实体会调用到。
	 */
	@Override
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		InterceptorHelper helper = new InterceptorHelper(id,state,null,propertyNames,InterceptorHelper.DELETE);
		process(entity,helper);
	}

	
	
	/**
	 * update 或 insert 会调用到
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		InterceptorHelper helper = new InterceptorHelper(id,currentState,previousState,propertyNames,(previousState != null?InterceptorHelper.UPDATE:InterceptorHelper.INSERT));
		return process(entity,helper);
	}

	/**
	 * 从数据库读取实体会调用到
	 */
	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		InterceptorHelper helper = new InterceptorHelper(id,state,null,propertyNames,InterceptorHelper.LOAD);
		return process(entity,helper);
	}

	/**
	 * insert会调用到。
	 */
	
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		InterceptorHelper helper = new InterceptorHelper(id,state,null,propertyNames,InterceptorHelper.INSERT);
		return process(entity,helper);
	}

	
	
}
