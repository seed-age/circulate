package com.sunnsoft.hibernate.interceptor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author llade
 * 操作Hibernate拦截器参数的帮助类
 */
public class InterceptorHelper {
	
	public static final int INSERT = 0;
	public static final int UPDATE = 1;
	public static final int DELETE = 2;
	public static final int LOAD = 3;
	
	private Object[] currentValues;
	private Map<String, Object> currentMap;
	private Map<String, Object> previousMap;
	private Map<String, Integer> propertyNameIndex;
	private Set<String> dirtys = new HashSet<String>();
	
	private boolean insert;
	private boolean update;
	private boolean delete;
	private boolean load;
	private Serializable entityId;
	
	public boolean isInsert() {
		return insert;
	}
	
	public boolean isUpdate() {
		return update;
	}
	
	public boolean isDelete() {
		return delete;
	}
	
	public boolean isLoad() {
		return load;
	}
	
	/**
	 * 实体的ID 
	 * @return
	 */
	public Serializable getEntityId() {
		return entityId;
	}

	/**
	 * 判断是否脏数据
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	private boolean isDirty(Object oldValue, Object newValue) {
		if( oldValue == null &&  newValue == null ){
			return false;
		}
		if(	(oldValue != null && newValue == null)
			|| (oldValue == null && newValue != null)
			|| !oldValue.equals(newValue)
			){
			return true;
		}
		return false;
	}
	
	public InterceptorHelper(Serializable entityId,Object[] currentValues, Object[] previousValues,
			String[] propertyNames,int mode) {
		super();
		this.currentValues = currentValues;
		this.currentMap = new HashMap<String, Object>(propertyNames.length);
		this.previousMap = new HashMap<String, Object>(propertyNames.length);
		this.propertyNameIndex = new HashMap<String, Integer>(propertyNames.length);
		insert = (mode == INSERT);
		update = (mode == UPDATE);
		delete = (mode == DELETE);
		load = (mode == LOAD);
		
		for (int i = 0; i < propertyNames.length; i++) {
			this.currentMap.put(propertyNames[i], currentValues[i]);
			if(update){
				this.previousMap.put(propertyNames[i], previousValues[i]);
			}
			this.propertyNameIndex.put(propertyNames[i], i);
			
		}
		if(update){
			for (int i = 0; i < previousValues.length; i++) {
				if(isDirty(previousValues[i],currentValues[i])){
					dirtys.add(propertyNames[i]);
				}
			}
		}
	}
	
	
	
	/**
	 * 对于insert、delete、load，表示属性的当前值，对于update，表示update后的值
	 * @param propertyName
	 * @return
	 */
	public Object getValue(String propertyName){
		if(!this.currentMap.containsKey(propertyName)) throw new NoSuchPropertyException(propertyName);
		return this.currentMap.get(propertyName);
	}
	/**
	 * 设置属性的值，注意，这里会修改保存到数据库的最终结果,如果设置的值和
	 * @param propertyName
	 * @param value
	 */
	public void setValue(String propertyName,Object newValue){
		if(!this.currentMap.containsKey(propertyName)) throw new NoSuchPropertyException(propertyName);
		this.currentValues[this.propertyNameIndex.get(propertyName)] = newValue;
	}
	/**
	 * 获取update之前的旧值，update的时候才用到
	 * @param propertyName
	 * @return
	 */
	public Object getPreviousValue(String propertyName) {
		if(!update)throw new NotInUpdateModeException();
		if(!this.currentMap.containsKey(propertyName)) throw new NoSuchPropertyException(propertyName);
		return this.previousMap.get(propertyName);
	}
	/**
	 * 判断属性是否变动过，update的时候才用到
	 * @param propertyName
	 * @return
	 */
	public boolean dirty(String propertyName) {
		if(!update)throw new NotInUpdateModeException();
		if(!this.currentMap.containsKey(propertyName)) throw new NoSuchPropertyException(propertyName);
		return this.dirtys.contains(propertyName);
	}
	/**
	 * 获取值有变动的属性名列表，update的时候才用到
	 * @return
	 */
	public Set<String> getDirtyPropertyNames() {
		if(!update)throw new NotInUpdateModeException();
		return this.dirtys;
	}
}
