package com.sunnsoft.util;

import java.util.Date;

public class RibDateUtils {
	/**
	 * 获取两个日期的间隔时间（分钟）
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDiffMinute(Date date1, Date date2) {
		double minute = ((double) (date1.getTime() - date2.getTime())) / 1000 / 60;
		long m = (long) Math.ceil(minute);
		return m;
	}

	/**
	 * 获取两个日期的间隔时间（秒）
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDiffSecond(Date date1, Date date2) {
		return (date1.getTime() - date2.getTime()) / 1000;
	}

	/**
	 * 获取两个日期的间隔时间（秒）
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDiffSecond(long date1, long date2) {
		return (date1 - date2) / 1000;
	}

	/**
	 * 获取两个日期的间隔时间（小时）
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDiffHour(Date date1, Date date2) {
		double day = ((double) (date1.getTime() - date2.getTime())) / 1000 / (60 * 60);
		long m = (long) Math.ceil(day);

		return m;
	}

	/**
	 * 获取两个日期的间隔时间（天）
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDiffDay(Date date1, Date date2) {
		double day = ((double) (date1.getTime() - date2.getTime())) / 1000 / (60 * 60 * 24);
		long m = (long) Math.ceil(day);

		return m;
	}

	/*public static void main(String[] args) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try

		{

			Date d1 = df.parse("2018-02-6 10:05:00");

			Date d2 = df.parse("2018-02-6 10:04:51");

			long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
			System.out.println(diff+" 时间戳相减得到的差值.");
			double days = diff / 1000 / (60 * 60 * 24);
			
			//判断当前days参数的值是否小于0 
			if(days <= 0) {
				double hours =  diff / 1000 / (60 * 60);
				if(hours <= 0) {
					double minute = diff / 1000 / 60;
					if(minute <= 0) {
						long min = diff / 1000;
						if(min < 10) {
							System.out.println("刚刚");
						}else {
							System.out.println((int)min + "秒");
						}
					}else {
						
						System.out.println((int)minute + "分钟");
					}
				}else {
					
					System.out.println((int)hours + "小时");
				}
			}else {
				System.out.println((int)days + "天");
			}
			
			

			long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
			System.out.println(hours);
			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
			System.out.println((int)minutes);

			System.out.println("" + days + "天" + hours + "小时" + minutes + "分");

		}

		catch (Exception e)

		{

		}

	}*/
}
