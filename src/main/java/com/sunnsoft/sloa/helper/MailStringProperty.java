package com.sunnsoft.sloa.helper;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class MailStringProperty extends MailProperty<String>{
	
	public MailStringProperty(String propertyName, MailHelper helper) {
		super(propertyName, helper,String.class);
	}
	
	@Override
	protected boolean valueEmpty(String value) {
		return StringUtils.isBlank(value);
	}

	/**
	 * 类似 
	 * @param value 其中%为通配符，如果没有通配符，则自动加上前后通配符，等价于%value%
	 * @return
	 */
	public MailHelper Like(String value){//专门针对string类型
		if(helper.ignoreEmpty && valueEmpty( value )){
			return helper;
		}
		if(value != null && value.indexOf("%") == -1){
			add(Restrictions.like(this.propertyName, value, MatchMode.ANYWHERE));
		}else{
			add(Restrictions.like(this.propertyName, value));
		}
		return helper;
	}

}
