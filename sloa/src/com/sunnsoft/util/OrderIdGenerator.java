package com.sunnsoft.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderIdGenerator implements InitializingBean {

	/** 年月日时分秒(无下划线) yyyyMMddHHmmss */
	public static final String dtLong = "yyyyMMddHHmmss";

	public static String getOrderNum() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat(dtLong);
		return df.format(date);
	}

	public synchronized String getId(String type) {
		return getOrderNum() + type + this.initValue++;
	}

	public static boolean isType(String type, String orderNo) {
		if (StringUtils.isEmpty(orderNo) || StringUtils.isEmpty(type)) {
			return false;
		}
		return orderNo.indexOf(type) != -1;
	}

	private int initValue;

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		// List l =
		// this.orderGuoqingService.findByDetachedCriteria(this.orderGuoqingService.createCriteria().setProjection(Projections.max("taoNo")));
		// Long id = (Long)l.get(0);
		// String value = null;
		// if(id != null){
		// value = this.orderGuoqingService.findById(id).getTaoNo();
		// }
		// System.out.println(value);
		// if(value == null){
		// initValue = 1001;
		// }else{
		// initValue = Integer.parseInt(value.substring(1))+1;
		// }
		// System.out.println(initValue);
	}

}
