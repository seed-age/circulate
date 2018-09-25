package com.sunnsoft.sloa.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.sunnsoft.util.DateUtils;

public class HrmdepartmentDateProperty extends HrmdepartmentProperty<Date>{
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public HrmdepartmentDateProperty(String propertyName, HrmdepartmentHelper helper) {
		super(propertyName, helper,Date.class);
	}
	/**
	 * 设置日期处理格式(默认值"yyyy-MM-dd HH:mm:ss")
	 * @param format
	 * @return
	 */
	public HrmdepartmentDateProperty setDateFormat(String format){
		sdf = new SimpleDateFormat(format);
		return this;
	}
	/**
	 * 直接设置字符串作为日期值
	 * @param value
	 */
	public HrmdepartmentHelper setValue(String value) throws ParseException{
		this.setValue(sdf.parse(value));
		return helper;
	}
	/**
	 * 在某天
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	public HrmdepartmentHelper InDay(String value) throws ParseException{
		if(helper.ignoreEmpty && StringUtils.isBlank(value)){
			return helper;
		}
		this.InDay(sdf.parse(value));
		return helper;
	}
	/**
	 * 在某个日期所在那天
	 * @param value
	 * @return
	 */
	public HrmdepartmentHelper InDay(Date value){
		if(helper.ignoreEmpty && value == null){
			return helper;
		}
		Date begin = DateUtils.getDayBegin(value);
		Date end = DateUtils.getDayEnd(value);
		this.Ge(begin);
		this.Lt(end);
		return helper;
	}
	/**
	 * 在某月
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	public HrmdepartmentHelper InMonth(String value) throws ParseException{
		if(helper.ignoreEmpty && StringUtils.isBlank(value)){
			return helper;
		}
		this.InMonth(sdf.parse(value));
		return helper;
	}
	/**
	 * 在某个日期所在月份
	 * @param value
	 * @return
	 */
	public HrmdepartmentHelper InMonth(Date value){
		if(helper.ignoreEmpty && value == null){
			return helper;
		}
		Date begin = DateUtils.getMonthBegin(value);
		Date end = DateUtils.getMonthEnd(value);
		this.Ge(begin);
		this.Lt(end);
		return helper;
	}
	/**
	 * 小于等于（字符串格式）
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	public HrmdepartmentHelper Le(String value) throws ParseException{
		this.Le(sdf.parse(value));
		return helper;
	}
	/**
	 * 小于（字符串格式）
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	public HrmdepartmentHelper Lt(String value) throws ParseException{
		this.Lt(sdf.parse(value));
		return helper;
	}
	/**
	 * 大于等于（字符串格式）
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	public HrmdepartmentHelper Ge(String value) throws ParseException{
		this.Ge(sdf.parse(value));
		return helper;
	}
	/**
	 * 大于（字符串格式）
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	public HrmdepartmentHelper Gt(String value) throws ParseException{
		this.Gt(sdf.parse(value));
		return helper;
	}

}
