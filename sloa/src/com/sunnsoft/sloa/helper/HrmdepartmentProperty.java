package com.sunnsoft.sloa.helper;

import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import org.gteam.db.helper.hibernate.Each;
import org.gteam.db.helper.hibernate.HelperException;
import com.sunnsoft.sloa.db.vo.Hrmdepartment;

@SuppressWarnings("unchecked")
public class HrmdepartmentProperty<T> {
	
	protected Class<T> type;
	/**
	 * 
	 * @param propName
	 * @param helper
	 */
	public HrmdepartmentProperty(String propertyName, HrmdepartmentHelper helper,Class<T> type) {
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
	
	HrmdepartmentHelper helper ;
	
	void add(Criterion c){
		if(junction != null){
			junction.add(c);
		}else{
			this.dc.add(c);
		}
	}
		
	protected boolean valueEmpty(T value){
		return value == null;
	}
	
	/**
	 * 升序
	 */
	public HrmdepartmentHelper Asc(){
		this.dc.addOrder(Order.asc(propertyName));
		return helper;
	}
	/**
	 * 降序
	 * @return
	 */
	public HrmdepartmentHelper Desc(){
		this.dc.addOrder(Order.desc(propertyName));
		return helper;
	}
	/**
	 * 等于
	 * @param value 如果为Null则自动使用isNull
	 * @return
	 */
	public HrmdepartmentHelper Eq(T value){
		if(helper.ignoreEmpty && valueEmpty( value )){
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
	public HrmdepartmentHelper Ne(T value){
		if(helper.ignoreEmpty && valueEmpty( value )){
			return helper;
		}
		if(value == null){
			return this.IsNotNull();
		}
		this.add(Restrictions.ne(propertyName, value));
		return helper;
	}
	/**
	 * 大于
	 * @param value
	 * @return
	 */
	public HrmdepartmentHelper Gt(T value){
		if(helper.ignoreEmpty && valueEmpty( value )){
			return helper;
		}
		this.add(Restrictions.gt(propertyName, value));
		return helper;
	}
	/**
	 * 大于等于
	 * @param value
	 * @return
	 */
	public HrmdepartmentHelper Ge(T value){
		if(helper.ignoreEmpty && valueEmpty( value )){
			return helper;
		}
		this.add(Restrictions.ge(propertyName, value));
		return helper;
	}
	/**
	 * 小于
	 * @param value
	 * @return
	 */
	public HrmdepartmentHelper Lt(T value){
		if(helper.ignoreEmpty && valueEmpty( value )){
			return helper;
		}
		this.add(Restrictions.lt(propertyName, value));
		return helper;
	}
	/**
	 * 小于等于
	 * @param value
	 * @return
	 */
	public HrmdepartmentHelper Le(T value){
		if(helper.ignoreEmpty && valueEmpty( value )){
			return helper;
		}
		this.add(Restrictions.le(propertyName, value));
		return helper;
	}
	/**
	 * 等于values之一
	 * @param values
	 * @return
	 */
	public HrmdepartmentHelper In(T... values){
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
	public HrmdepartmentHelper In(Collection<T> values){
		if(helper.ignoreEmpty && (values == null || values.isEmpty() )){
			return helper;
		}
		this.add(Restrictions.in(propertyName, values));
		return helper;
	}
	/**
	 * 不等于values中任何一个
	 * @param values
	 * @return
	 */
	public HrmdepartmentHelper NotIn(T... values){
		if(helper.ignoreEmpty && (values == null || values.length == 0 )){
			return helper;
		}
		this.add(Restrictions.not(Restrictions.in(propertyName, values)));
		return helper;
	}
	/**
	 * 不等于values中任何一个
	 * @param values
	 * @return
	 */
	public HrmdepartmentHelper NotIn(Collection<T> values){
		if(helper.ignoreEmpty && (values == null || values.isEmpty() )){
			return helper;
		}
		this.add(Restrictions.not(Restrictions.in(propertyName, values)));
		return helper;
	}
	/**
	 * Null
	 * @return
	 */
	public HrmdepartmentHelper IsNull(){
		this.add(Restrictions.isNull(propertyName));
		return helper;
	}
	/**
	 * Not Null
	 * @return
	 */
	public HrmdepartmentHelper IsNotNull(){
		this.add(Restrictions.isNotNull(propertyName));
		return helper;
	}
	/**
	 * 等于value 或者 为Null 相当于( A is null or A = value)
	 * @param value 不能为null
	 * @return
	 */
	public HrmdepartmentHelper EqOrNull(T value){
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
	public HrmdepartmentHelper NeOrNull(T value){
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
	/**
	 * 获取满足查询条件的字段值集合列表
	 * @param distinct 是否不重复
	 * @return
	 */
	public List<T> listProperty(boolean distinct){
		if(distinct){
			this.dc.setProjection(Projections.distinct(Projections.property(propertyName)));
		}else{
			this.dc.setProjection(Projections.property(propertyName));
		}
		List<T> result = (List<T>) this.helper.service.findRawListResultByDetachedCriteria(dc);
		if(this.helper.setLimit){
			if(this.helper.setStart){
				result = (List<T>) this.helper.service.findRawListResultByDetachedCriteria(dc, this.helper.start, this.helper.limit);
			}else{
				result = (List<T>) this.helper.service.findRawListResultByDetachedCriteria(dc, 0, this.helper.limit);
			}
		}else{
			result = (List<T>) this.helper.service.findRawListResultByDetachedCriteria(dc);
		}
		return result;
	}
	/**
	 * 获取满足查询条件的字段值集合列表，并转化为json序列化输出。
	 * @param distinct 是否不重复
	 * @return
	 */
	public String listPropertyJson(boolean distinct) {
		List<T> list = this.listProperty(distinct);
		return this.helper.json().fastJsonSerializer(list, false);
	}
	/**
	 * 直接设置属性值
	 * @param value
	 */
	public void setValue(final T value){
		final String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
		try {
			final Method m = Hrmdepartment.class.getMethod(setMethodName, this.type);
			this.helper.each(new Each<Hrmdepartment>(){

				@Override
				public void each(Hrmdepartment bean, List<Hrmdepartment> list) {
					try {
						m.invoke(bean, value);
					} catch (Exception e) {
						throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
					}					
				}
				
			});
		} catch (Exception e) {
			throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
		}
	}
	/**
	 * 求和
	 * @return
	 */
	public Object sum(){
		this.dc.setProjection(Projections.sum(propertyName));
		return this.helper.service.findRawUniqueResultByDetachedCriteria(dc);
	}
	
	/**
	 * 最大值
	 * @return
	 */
	public Object max(){
		this.dc.setProjection(Projections.max(propertyName));
		return this.helper.service.findRawUniqueResultByDetachedCriteria(dc);
	}
	/**
	 * 最小值
	 * @return
	 */
	public Object min(){
		this.dc.setProjection(Projections.min(propertyName));
		return this.helper.service.findRawUniqueResultByDetachedCriteria(dc);
	}
	/**
	 * 平均值
	 * @return
	 */
	public Object avg(){
		this.dc.setProjection(Projections.avg(propertyName));
		return this.helper.service.findRawUniqueResultByDetachedCriteria(dc);
	}
}
