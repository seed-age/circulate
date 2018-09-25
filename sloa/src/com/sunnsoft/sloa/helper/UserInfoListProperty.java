package com.sunnsoft.sloa.helper;

import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

public class UserInfoListProperty{
	
	public UserInfoListProperty(String propertyName, UserInfoHelper helper) {
		super();
		this.helper = helper;
		if(StringUtils.isNotEmpty(this.helper.alias)){//有别称则把别称加入到属性中。
			this.propertyName = this.helper.alias+"."+propertyName;
		}else{
			this.propertyName = propertyName;
		}
		this.dc = this.helper.getDc();
		this.junctions = this.helper.junctions;
		if(!junctions.empty()){
			this.junction = junctions.peek();
		}
	}

	String propertyName;
	
	DetachedCriteria dc;
	
	Junction junction ;
	
	/**
	 * 括号对象的栈，其他helper类可以设值
	 */
	Stack<Junction> junctions ;
	
	UserInfoHelper helper ;
	
	void add(Criterion c){
		if(junction != null){
			junction.add(c);
		}else{
			this.dc.add(c);
		}
	}
	/**
	 * 空
	 * @return
	 */
	public UserInfoHelper IsEmpty(){ 
		this.add(Restrictions.isEmpty(propertyName));
		return helper;
	}
	/**
	 * 非空
	 * @return
	 */
	public UserInfoHelper IsNotEmpty(){ 
		this.add(Restrictions.isNotEmpty(propertyName));
		return helper;
	}
	/**
	 * size等于
	 * @param size
	 * @return
	 */
	public UserInfoHelper SizeEq(int size){
		this.add(Restrictions.sizeEq(propertyName, size));
		return helper;
	}
	/**
	 * size不等于
	 * @param size
	 * @return
	 */
	public UserInfoHelper SizeNe(int size){
		this.add(Restrictions.sizeNe(propertyName, size));
		return helper;
	}
	/**
	 * size大于等于
	 * @param size
	 * @return
	 */
	public UserInfoHelper SizeGe(int size){
		this.add(Restrictions.sizeGe(propertyName, size));
		return helper;
	}
	/**
	 * size小于等于
	 * @param size
	 * @return
	 */
	public UserInfoHelper SizeLe(int size){
		this.add(Restrictions.sizeLe(propertyName, size));
		return helper;
	}
	/**
	 * size大于
	 * @param size
	 * @return
	 */
	public UserInfoHelper SizeGt(int size){
		this.add(Restrictions.sizeGt(propertyName, size));
		return helper;
	}
	/**
	 * size小于
	 * @param size
	 * @return
	 */
	public UserInfoHelper SizeLt(int size){
		this.add(Restrictions.sizeLt(propertyName, size));
		return helper;
	}
	/**
	 * 获得pdm定义的字段中文名
	 * @return
	 */
	public String getTitle(){
		return this.helper.titlesMap().get(propertyName);
	}
	
}
