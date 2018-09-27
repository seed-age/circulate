package com.sunnsoft.sloa.helper;

import org.apache.commons.lang3.StringUtils;
import org.gteam.db.helper.hibernate.HelperException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;
import java.util.Stack;

public class UpdateRecordEntityProperty<T> {
	
	protected Class<T> type;
	/**
	 * 
	 * @param propName 如果有别名 propName必须包含别名。
	 * @param helper
	 */
	public UpdateRecordEntityProperty(String propertyName, UpdateRecordHelper helper,Class<T> type) {
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
		this.type = type;
	}
	
	String propertyName;
	
	DetachedCriteria dc;
	
	Junction junction ;
	
	/**
	 * 括号对象的栈，其他helper类可以设值
	 */
	Stack<Junction> junctions ;
	
	UpdateRecordHelper helper ;
	
	void add(Criterion c){
		if(junction != null){
			junction.add(c);
		}else{
			this.dc.add(c);
		}
	}
	/**
	 * 等于
	 * @param value 如果为Null则自动使用isNull
	 * @return
	 */
	public UpdateRecordHelper Eq(T value){
		if(helper.ignoreEmpty && value == null){
			return helper;
		}
		if(value == null){
			return this.IsNull();
		}
		this.add(Restrictions.eq(propertyName, value));
		return helper;
	}
	/**
	 * 不等于
	 * @param value 如果为Null则自动使用isNotNull
	 * @return
	 */
	public UpdateRecordHelper Ne(T value){
		if(helper.ignoreEmpty && value == null){
			return helper;
		}
		if(value == null){
			return this.IsNotNull();
		}
		this.add(Restrictions.ne(propertyName, value));
		return helper;
	}
	/**
	 * 等于values之一
	 * @param values
	 * @return
	 */
	public UpdateRecordHelper In(T... values){
		if(helper.ignoreEmpty && (values == null || values.length == 0 )){
			return helper;
		}
		this.add(Restrictions.in(propertyName, values));
		return helper;
	}
	/**
	 * 等于values之一
	 * @param values
	 * @return
	 */
	public UpdateRecordHelper In(Collection<T> values){
		if(helper.ignoreEmpty && (values == null || values.isEmpty() )){
			return helper;
		}
		this.add(Restrictions.in(propertyName, values));
		return helper;
	}
	/**
	 * Null
	 * @return
	 */
	public UpdateRecordHelper IsNull(){
		this.add(Restrictions.isNull(propertyName));
		return helper;
	}
	/**
	 * Not Null
	 * @return
	 */
	public UpdateRecordHelper IsNotNull(){
		this.add(Restrictions.isNotNull(propertyName));
		return helper;
	}
	/**
	 * 等于value 或者 为Null 相当于( A is null or A = value)
	 * @param value 不能为null
	 * @return
	 */
	public UpdateRecordHelper EqOrNull(T value){
		if(value == null){
			throw new HelperException("value不能为null");
		}
		this.add(Restrictions.or(Restrictions.eq(propertyName, value), Restrictions.isNull(propertyName)));
		return helper;
	}
	/**
	 * 不等于 value 或者 为Null 相当于( A is null or A != value)
	 * @param value 不能为null
	 * @return
	 */
	public UpdateRecordHelper NeOrNull(T value){
		if(value == null){
			throw new HelperException("value不能为null");
		}
		this.add(Restrictions.or(Restrictions.ne(propertyName, value), Restrictions.isNull(propertyName)));
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
