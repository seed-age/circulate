package com.sunnsoft.util;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

	/** 日期 */
	public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	/** 日期时间 */
	public final static String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/** 时间 */
	public final static String DEFAULT_TIME_PATTERN = "HH:mm:ss";
	/**
	 * 每天的毫秒数
	 */
	public final static long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

	/** 注意格里历和儒略历交接时的日期差别 */
	private transient int gregorianCutoverYear = 1582;

	/** 闰年中每月天数 */
	private final int[] DAYS_P_MONTH_LY = { 31, 29, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };
	/** 平年中每月天数 */
	private final int[] DAYS_P_MONTH_CY = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };
	/** 代表数组里的年、月、日 */
	private final int Y = 0, M = 1, D = 2;

	/** 参与运算用 */
	private int[] ymd = null;

	/**
	 * 获得当前年月日时分秒的数字表达形式， 例如当前2011年3月2日，则返回20110302.
	 * 
	 * @param isHour
	 *            是否生成时分秒
	 * @return 生成时间字符串
	 */
	public static String getCurrentDateStr(boolean isHour) {
		StringBuffer dateStr = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		if (month.length() == 1) {
			month = "0" + month;
		}
		String day = String.valueOf(cal.get(Calendar.DATE));
		if (day.length() == 1) {
			day = "0" + day;
		}
		String hour = String.valueOf(cal.get(Calendar.HOUR));
		if (hour.length() == 1) {
			hour = "0" + hour;
		}
		String minute = String.valueOf(cal.get(Calendar.MINUTE));
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		String second = String.valueOf(cal.get(Calendar.SECOND));
		if (second.length() == 1) {
			second = "0" + second;
		}
		dateStr.append(year + month + day);
		if (isHour) {
			dateStr.append(hour + minute + second);
		}
		System.out.println(dateStr);
		return dateStr.toString();
	}

	/**
	 * 获得当前年份的数字表达形式，例如当前2011年，则返回2011.
	 * 
	 * @return
	 */
	public static Integer getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.get(Calendar.YEAR);

	}

	/**
	 * 获取当前月份的季度
	 * 
	 * @param month
	 * @return
	 */
	public static String getThisSeasonTime(int year, int month) {
		String quarter = "";
		int thisModthDays = DateUtils.getDaysNumByMonth(year, month);
		if (month >= 1 && month <= 3) {
			quarter = year + "-01-01" + "至" + year + "-03-" + thisModthDays;
		}
		if (month >= 4 && month <= 6) {
			quarter = year + "-04-01" + "至" + year + "-06-" + thisModthDays;
		}
		if (month >= 7 && month <= 9) {
			quarter = year + "-07-01" + "至" + year + "-09-" + thisModthDays;
		}
		if (month >= 10 && month <= 12) {
			quarter = year + "-10-01" + "至" + year + "-12-" + thisModthDays;
		}
		return quarter;
	}

	/**
	 * 获得当前月份的数字表达形式，例如当前2011年5月，则返回5.
	 * 
	 * @return
	 */
	public static Integer getCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.get(Calendar.MONTH) + 1;

	}

	/**
	 * 获取给定日期所在月份的开始，精确到毫秒，也就是说给定日期是2011年3月21日，那么获得的日期是2011年3月1日，0时0分0秒0毫秒。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthBegin(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);
		return getDayBegin(cal.getTime());
	}

	/**
	 * 获取给定日期所在月份的结尾，实际获取的是下个月开始的第一毫秒，也就是说给定日期是2011年3月21日，那么获得的日期是2011年4月1日，0
	 * 时0分0秒0毫秒。 此日期使用的时候应该使用“字符串substring”原则，即结束日期应该排除在外。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);
		// cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) - 1);
		cal.add(Calendar.MONTH, 1);
		return getDayBegin(cal.getTime());
	}

	/**
	 * 获取给定日期所在星期的开始，精确到毫秒，也就是说给定日期是2011年3月21日是星期四，那么获得的日期是2011年3月18日（星期天），0
	 * 时0分0秒0毫秒。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getWeekBegin(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		// Date mm=nDaysAgo(cal.get(Calendar.DAY_OF_WEEK)-2,date);
		return getDayBegin(cal.getTime());
	}

	/**
	 * 获取给定日期所在星期的结尾，实际获取的是下个星期开始的第一毫秒，也就是说给定日期是2011年3月21日（星期四），
	 * 那么获得的日期是2011年3月24日（星期天），0时0分0秒0毫秒。
	 * 此日期使用的时候应该使用“字符串substring”原则，即结束日期应该排除在外。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getWeekEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		// Date mm=nDaysAfter(cal.get(8-Calendar.DAY_OF_WEEK),date);
		return getDayEnd(cal.getTime());

	}

	/**
	 * 获取给定时间所在日期的开始，精确到毫秒，也就是说给定时间是2011年3月21日12时30分30秒230毫秒，
	 * 那么获得的日期是2011年3月21日0时0分0秒0毫秒。
	 * 
	 * @param date
	 * @return
	 */

	public static Date getDayBegin(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
//		Calendar cal = Calendar.getInstance();
//		cal.clear();
//		cal.setTime(date);
//		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
//				.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//		return cal.getTime();
	}

	/**
	 *获取给定时间所在日期的结尾，实际获取的是给定时间的第二天开始的第一毫秒，也就是说给定时间是2011年3月21日12时30分30秒230毫秒，
	 * 那么获得的日期是2011年3月21日0时0分0秒0毫秒。 此日期使用的时候应该使用“字符串substring”原则，即结束日期应该排除在外。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return getDayBegin(cal.getTime());
	}

	/**
	 * 转换日期字符串得到指定格式的日期类型
	 * 
	 * @param formatString
	 *            需要转换的格式字符串
	 * @param targetDate
	 *            需要转换的时间
	 * @return
	 * @throws ParseException
	 */
	public static final Date convertString2Date(String formatString,
			String targetDate) throws ParseException {
		if (StringUtils.isBlank(targetDate) || StringUtils.isEmpty(targetDate))
			return null;
		SimpleDateFormat format = null;
		Date result = null;
		format = new SimpleDateFormat(formatString);
		try {
			result = format.parse(targetDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return result;
	}

	/**
	 * 转换字符串得到默认格式的日期类型
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertString2Date(String strDate) throws ParseException {
		Date result = null;
		try {
			result = convertString2Date(DEFAULT_DATE_PATTERN, strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return result;
	}

	/**
	 * 转换日期得到指定格式的日期字符串
	 * 
	 * @param formatString
	 *            需要把目标日期格式化什么样子的格式。例如,yyyy-MM-dd HH:mm:ss
	 * @param targetDate
	 *            目标日期
	 * @return
	 */
	public static String convertDate2String(String formatString, Date targetDate) {
		SimpleDateFormat format = null;
		String result = null;
		if (targetDate != null) {
			format = new SimpleDateFormat(formatString);
			result = format.format(targetDate);
		} else {
			return null;
		}
		return result;
	}

	/**
	 * 转换日期,得到默认日期格式字符串
	 * 
	 * @param targetDate
	 * @return
	 */
	public static String convertDate2String(Date targetDate) {
		return convertDate2String(DEFAULT_DATE_PATTERN, targetDate);
	}

	/**
	 * 从月份得到当月天数
	 * 
	 * @param
	 */
	public static int getDaysNumByMonth(int year, int month) {
		switch (month) {
		case 1:
			return 31;
		case 2: {
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			return gregorianCalendar.isLeapYear(year) ? 29 : 28;
		}
		case 3:
			return 31;
		case 4:
			return 30;
		case 5:
			return 31;
		case 6:
			return 30;
		case 7:
			return 31;
		case 8:
			return 30;
		case 9:
			return 31;
		case 10:
			return 30;
		case 11:
			return 31;
		case 12:
			return 30;

		}
		return 30;
	}

	/**
	 * @deprecated 从开始时间与结束时间得到除去星期天的天数
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getDays(Date start, Date end) {
		int workDay = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(end);
		while (cal.before(calEnd)) {
			if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {// 星期天不计算在内
				workDay++;
			}
			cal.add(Calendar.DATE, 1);
		}

		return workDay;
	}

	/**
	 * 格式化时间字符串
	 * 
	 * @param date
	 *            要格式化的时间,默认为当前时间.
	 * @param format
	 *            时间格式字符串,默认格式为'yyyy-MM-dd HH:mm:ss'.
	 * @return 格式化后的时间字符串
	 */
	public static String dateToString(Date date, String format) {
		if (date == null) {
			date = new Date();
		}
		if (format == null || format.equals("")) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * 将指定格式的时间字符串解析成时间对象
	 * 
	 * @param strDate
	 *            时间字符串
	 * @param format
	 *            时间格式,默认格式为"yyyy-MM-dd HH:mm:ss"
	 * @return 时间对象
	 */
	public Date parse(String strDate, String format) {
		if (format == null || format.equals("")) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		try {
			date = dateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 比较两个日期的大小
	 * <p>
	 * date1大于date2返回1,date1等于date2返回0,date1小于date2返回-1.
	 * 
	 * @param date1
	 *            日期1
	 * @param date2
	 *            日期2
	 * @return 日期的大小
	 */
	public int compareTo(Date date1, Date date2) {
		String strDate1 = this.getStandardDate(date1);
		String strDate2 = this.getStandardDate(date2);
		date1 = this.parse(strDate1, "yyyy-MM-dd");
		date2 = this.parse(strDate2, "yyyy-MM-dd");
		return date1.compareTo(date2);
	}

	/**
	 * 获取当前系统完整日期时间（年-月-日 小时:分钟:秒）
	 * 
	 * @return 完整日期时间字符串
	 */
	public String getCurrentFullDateTime() {
		SimpleDateFormat smdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return smdf.format(new Date());
	}

	/**
	 * 获取标准日期（年-月-日）
	 * 
	 * @param date
	 *            日期对象
	 * @return 标准日期字符串
	 */
	public String getStandardDate(Date date) {
		if (date == null)
			date = new Date();
		SimpleDateFormat smdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = new String(smdf.format(date));
		return strDate;
	}

	/**
	 * 检查传入的参数是否合法的日期
	 * 
	 * @param date
	 * @throws IllegalArgumentException
	 */
	public void validate(String date) throws IllegalArgumentException {

		int[] ymd = splitYMD(date);

		if (ymd[M] == 0 || ymd[M] > 12) {
			throw new IllegalArgumentException("月份数值错误");
		}

		if (true == isLeapYear(ymd[0])) {
			if (ymd[D] == 0 || ymd[D] > DAYS_P_MONTH_LY[ymd[M] - 1]) {
				throw new IllegalArgumentException("日期数值错误");
			}
		} else {
			if (ymd[D] == 0 || ymd[D] > DAYS_P_MONTH_CY[ymd[M] - 1]) {
				throw new IllegalArgumentException("日期数值错误");
			}
		}
	}

	/**
	 * 检查传入的参数代表的年份是否为闰年
	 * 
	 * @param year
	 * @return
	 */
	public boolean isLeapYear(int year) {
		return year >= gregorianCutoverYear ? ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)))
				: // Gregorian
				(year % 4 == 0); // Julian
	}

	/**
	 * 日期加1天，注意这里没有考虑儒略历和格里历交接时相差的10天
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	private int[] addOneDay(int year, int month, int day) {
		if (isLeapYear(year)) {
			day++;
			if (day > DAYS_P_MONTH_LY[month - 1]) {
				month++;
				if (month > 12) {
					year++;
					month = 1;
				}
				day = 1;
			}
		} else {
			day++;
			if (day > DAYS_P_MONTH_CY[month - 1]) {
				month++;
				if (month > 12) {
					year++;
					month = 1;
				}
				day = 1;
			}
		}
		int[] ymd = { year, month, day };
		return ymd;
	}

	/**
	 * 以循环的方式计算日期加法
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public String addDaysByLoop(String date, int days) {
		validate(date);
		int[] ymd = splitYMD(date);
		for (int i = 0; i < days; i++) {
			ymd = addOneDay(ymd[Y], ymd[M], ymd[D]);
		}
		return formatYear(ymd[Y]) + formatMonthDay(ymd[M])
				+ formatMonthDay(ymd[D]);
	}

	/**
	 * 日期减1天，注意这里没有考虑儒略历和格里历交接时相差的10天
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	private int[] reduceOneDay(int year, int month, int day) {
		if (isLeapYear(year)) {
			day--;
			if (day <= 0) {
				month--;
				if (month < 1) {
					year--;
					month = 12;
				}
				day = DAYS_P_MONTH_LY[month - 1];
			}
		} else {
			day--;
			if (day <= 0) {
				month--;
				if (month < 1) {
					year--;
					month = 12;
				}
				day = DAYS_P_MONTH_CY[month - 1];
			}
		}
		int[] ymd = { year, month, day };
		return ymd;
	}

	/**
	 * 以循环的方式计算日期减法
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public String reduceDaysByLoop(String date, int days) {
		validate(date);
		int[] ymd = splitYMD(date);
		for (int i = 0; i < days; i++) {
			ymd = reduceOneDay(ymd[Y], ymd[M], ymd[D]);
		}
		return formatYear(ymd[Y]) + formatMonthDay(ymd[M])
				+ formatMonthDay(ymd[D]);
	}

	/**
	 * 指定日期加上指定的天数的操作
	 * 
	 * @param date
	 * @param days
	 * @return
	 * @throws IllegalArgumentException
	 */
	public String addDays(Date date, int days) throws IllegalArgumentException {
		return addDays(formatDate(date), days);
	}

	/**
	 * 指定日期加上指定的天数的操作
	 * 
	 * @param date
	 * @param days
	 * @return
	 * @throws IllegalArgumentException
	 */
	public String addDays(String date, int days)
			throws IllegalArgumentException {

		validate(date);
		ymd = splitYMD(date);

		if (isLeapYear(ymd[Y])) {
			ymd[D] += days;
			if (ymd[D] > DAYS_P_MONTH_LY[ymd[M] - 1]) {
				ymd[M]++;
				ymd[D] = ymd[D] - DAYS_P_MONTH_LY[ymd[M] - 1 - 1];
				if (ymd[M] > 12) {
					ymd[M] -= 12;
					ymd[Y]++;
				}
				if (ymd[D] > DAYS_P_MONTH_LY[ymd[M] - 1]) {
					addDays(formatYear(ymd[Y]) + formatMonthDay(ymd[M])
							+ formatMonthDay(DAYS_P_MONTH_LY[ymd[M] - 1]),
							ymd[D] - DAYS_P_MONTH_LY[ymd[M] - 1]);
				}
			}
		} else {
			ymd[D] += days;
			if (ymd[D] > DAYS_P_MONTH_CY[ymd[M] - 1]) {
				ymd[M]++;
				ymd[D] = ymd[D] - DAYS_P_MONTH_CY[ymd[M] - 1 - 1];
				if (ymd[M] > 12) {
					ymd[M] -= 12;
					ymd[Y]++;
				}
				if (ymd[D] > DAYS_P_MONTH_CY[ymd[M] - 1]) {
					addDays(formatYear(ymd[Y]) + formatMonthDay(ymd[M])
							+ formatMonthDay(DAYS_P_MONTH_CY[ymd[M] - 1]),
							ymd[D] - DAYS_P_MONTH_CY[ymd[M] - 1]);
				}
			}
		}
		return formatYear(ymd[Y]) + "-" + formatMonthDay(ymd[M]) + "-"
				+ formatMonthDay(ymd[D]);
	}

	/**
	 * 指定日期减去指定的天数的操作
	 * 
	 * @param date
	 * @param days
	 * @return
	 * @throws IllegalArgumentException
	 */
	public String reduceDays(Date date, int days)
			throws IllegalArgumentException {
		return reduceDays(formatDate(date), days);
	}

	/**
	 * 指定日期减去指定的天数的操作
	 * 
	 * @param date
	 * @param days
	 * @return
	 * @throws IllegalArgumentException
	 */
	public String reduceDays(String date, int days)
			throws IllegalArgumentException {

		validate(date);
		ymd = splitYMD(date);

		if (isLeapYear(ymd[Y])) {
			ymd[D] -= days;
			if (ymd[D] <= 0) {
				ymd[M]--;
				if (ymd[M] < 1) {
					ymd[M] += 12;
					ymd[Y]--;
				}
				ymd[D] = ymd[D] + DAYS_P_MONTH_LY[ymd[M] - 1];
				if (ymd[D] <= 0) {
					reduceDays(formatYear(ymd[Y]) + formatMonthDay(ymd[M])
							+ formatMonthDay(1), abs(ymd[D] - 1));
				}
			}
		} else {
			ymd[D] -= days;
			if (ymd[D] <= 0) {
				ymd[M]--;
				if (ymd[M] < 1) {
					ymd[M] += 12;
					ymd[Y]--;
				}
				ymd[D] = ymd[D] + DAYS_P_MONTH_CY[ymd[M] - 1];
				if (ymd[D] <= 0) {
					reduceDays(formatYear(ymd[Y]) + formatMonthDay(ymd[M])
							+ formatMonthDay(1), abs(ymd[D] - 1));
				}
			}
		}
		System.out.println(formatYear(ymd[Y]));
		System.out.println(formatMonthDay(ymd[M]));
		System.out.println(formatMonthDay(ymd[D]));
		return formatYear(ymd[Y]) + "-" + formatMonthDay(ymd[M]) + "-"
				+ formatMonthDay(ymd[D]);
	}

	/**
	 * 格式化一个日期字符串
	 * 
	 * @param date
	 * @return
	 */
	public String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/**
	 * 将代表日期的字符串分割为代表年月日的整形数组
	 * 
	 * @param date
	 * @return
	 */
	public int[] splitYMD(String date) {
		int[] ymd = { 0, 0, 0 };
		ymd[Y] = Integer.parseInt(date.substring(0, 4));
		ymd[M] = Integer.parseInt(date.substring(5, 7));
		ymd[D] = Integer.parseInt(date.substring(8, 10));
		return ymd;
	}

	/**
	 * 将不足两位的月份或日期补足为两位
	 * 
	 * @param decimal
	 * @return
	 */
	public static String formatMonthDay(int decimal) {
		DecimalFormat df = new DecimalFormat("00");
		return df.format(decimal);
	}

	/**
	 * 将不足四位的年份补足为四位
	 * 
	 * @param decimal
	 * @return
	 */
	public String formatYear(int decimal) {
		DecimalFormat df = new DecimalFormat("0000");
		return df.format(decimal);
	}

	/**
	 * 取绝对值操作
	 * 
	 * @param num
	 * @return
	 */
	public int abs(int num) {
		return (num > 0) ? num : -num;
	}

	/**
	 * 是否为相同的日期.
	 * 
	 * @param date1
	 *            日期1
	 * @param date2
	 *            日期2
	 * @return true:日期相同
	 */
	public boolean isSameDate(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("yyy-MM-dd");
		try {
			Date d1 = df.parse(date1);
			Date d2 = df.parse(date2);
			if (d1.compareTo(d2) == 0)
				return true;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}
	/**
	 * 在某个开始时间（包括）和结束时间（不包括）的时间段之内，按calendarField表示的步长以及outputDateFormat规定的格式，获取符合条件的时间字串列表。
	 * 输入的开始时间字符串和结束时间字符串由inputDateFormat固定的精度进行格式化。
	 * 例如获取 2013-07-01 00:00:00.0 2013-07-07 00:00:00.0时间段
	 * @param startTimeString
	 * @param endTimeString
	 * @param inputDateFormat
	 * @param outputDateFormat
	 * @param calendarField
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getDateStringList(String startTimeString,String endTimeString,String inputDateFormat,String outputDateFormat,int calendarField) throws ParseException{
		List<String> result = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdfIn = new SimpleDateFormat(inputDateFormat);
		SimpleDateFormat sdfOut = new SimpleDateFormat(outputDateFormat);
		Date compareDate = sdfIn.parse(startTimeString);
		Date endDate = sdfIn.parse(endTimeString);
		while(compareDate.before(endDate)){
			result.add(sdfOut.format(compareDate));
			cal.setTime(compareDate);
			cal.add(calendarField, 1);
			compareDate = cal.getTime();
		}
		return result;
	}
	
	/**
	 * 判断日期是否已经成为过去式，也就是判断日期是否昨天或者更早的时间。
	 * @param catchDateString 日期字符串，格式yyyy-MM-dd
	 * @return
	 */
	public static boolean isDatePassed(String catchDateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(catchDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		Date todayBegin = DateUtils.getDayBegin(new Date());
		return date.before(todayBegin);
	}
	
	
	
	/** 
	 * 得到几天前/几天后的时间 
	 * @param d 
	 * @param day 
	 * @return 
    */  
    public static Date dateToDay(Date date,int day){  
    	Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
	    return c.getTime();  
    }
    
    /** 
	 * 得到几个月前/几个月后的时间 
	 * @param d 
	 * @param day 
	 * @return 
    */  
    public static Date dateToMonth(Date date,int month){  
    	Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, month);
	    return c.getTime();  
    } 
    
}
