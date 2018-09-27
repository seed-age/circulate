package com.sunnsoft.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class FormatUtils {

	public static final DecimalFormat DECIMAL_TWO = new DecimalFormat("#0.00");
	public static final DecimalFormat DECIMAL_THREE = new DecimalFormat(
			"#0.000");

	/**
	 * 把长整型数字补齐指定字符串,形成定长的字符串
	 * 
	 * @param source
	 *            源长整型数字
	 * @param iLength
	 *            目标字符串的长度
	 * @param ch
	 *            填充字符串
	 * @return 异常返回 null或 " " ,成功返回定长的字符串
	 */
	public static String toDefiniteLengthString(long source, int iLength,
			char ch) {
		StringBuffer sb = new StringBuffer();
		String strValue = String.valueOf(source);
		if (iLength < strValue.length()) {
			return null;
		}
		for (int i = 0; i < iLength - strValue.length(); i++) {
			sb.append(ch);
		}
		sb.append(source);
		return new String(sb);
	}

	/**
	 * 把double变成2位小数
	 * 
	 * @param total
	 * @return
	 */
	public static Double formatTwoDouble(Double total) {

		return Double.parseDouble(formatDouble(total, 2));
	}

	public static Double formatThreeDouble(Double total) {

		return Double.parseDouble(formatDouble(total, 3));
	}

	public static String formatDouble(double value, int num) {
		BigDecimal bd = new BigDecimal(value);
		BigDecimal bd2 = bd.setScale(num, BigDecimal.ROUND_HALF_UP);
		return bd2.toString();
	}

	public static void main(String[] args) {
		// System.out.println(formatTwoDouble(2000d));
		// System.out.println(formatTwoDouble(2000.123d));
		// System.out.println(formatTwoDouble(2000.125d));
		// System.out.println(formatTwoDouble(2000.124999d));

		double value = 134.1556235d;

		System.out.println(formatTwoDouble(value));
		System.out.println(formatThreeDouble(value));

		// 1. 先乘后四舍五入, 再除;
		double d = 123.14499;

		double d2 = Math.round(d * 100) / 100.0;
		System.out.println("通过Math取整后做除法: " + d2);

		// 2. 通过BigDecimal的setScale()实现四舍五入与小数点位数确定, 将转换为一个BigDecimal对象.
		BigDecimal bd = new BigDecimal(d);
		BigDecimal bd2 = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		System.out.println("通过BigDecimal.setScale获得: " + bd2);

		// 3. 通过DecimalFormat.format返回String的
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("通过DecimalFormat.format获得: " + df.format(d));

		// 4. 通过String.format
		System.out.println("通过StringFormat: " + String.format("%.2f", d));

		double formatString = 0;
		System.out.println("通过DecimalFormat: "
				+ DECIMAL_TWO.format(formatString));
		System.out.println("0.10变成double: "
				+ Double.parseDouble(DECIMAL_TWO.format(0.10)));
		System.out.println("0.1变成double: "
				+ Double.parseDouble(DECIMAL_TWO.format(0.1)));
		System.out.println("0.12变成double: "
				+ Double.parseDouble(DECIMAL_TWO.format(0.12)));
		System.out.println("0.123变成double: "
				+ Double.parseDouble(DECIMAL_TWO.format(0.123)));
		System.out.println("0.128变成double: "
				+ Double.parseDouble(DECIMAL_TWO.format(0.128)));
		System.out.println("0变成double: "
				+ Double.parseDouble(DECIMAL_TWO.format(0)));
		System.out.println("1.0变成double: "
				+ Double.parseDouble(DECIMAL_TWO.format(1.0)));

	}

}
