package com.sunnsoft.util;

/**
 * 把数字转换为大写
 * 
 * @author Administrator
 * 
 */
public class MoneyUtils {

	/** 大写数字 */
	private static final String[] NUMBERS = { "零", "壹", "贰", "叁", "肆", "伍",
			"陆", "柒", "捌", "玖" };
	/** 整数部分的单位 */
	private static final String[] IUNIT = { "元", "拾", "佰", "仟", "万", "拾", "佰",
			"仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟" };
	/** 小数部分的单位 */
	private static final String[] DUNIT = { "角", "分" };

	public static String toChinese(String money) {
		if (money == null) {
			money = "";
		}
		money = money.replaceAll(",", "");// 去掉","
		String integerStr;// 整数部分数字
		String decimalStr;// 小数部分数字
		// 初始化：分离整数部分和小数部分
		if (money.indexOf(".") > 0) {
			integerStr = money.substring(0, money.indexOf("."));
			decimalStr = money.substring(money.indexOf(".") + 1);
		} else if (money.indexOf(".") == 0) {
			integerStr = "";
			decimalStr = money.substring(1);
		} else {
			integerStr = money;
			decimalStr = "";
		}
		// integerStr去掉首0，不必去掉decimalStr的尾0(超出部分舍去)
		if (!integerStr.equals("")) {
			integerStr = Long.toString(Long.parseLong(integerStr));
			if (integerStr.equals("0")) {
				integerStr = "";
			}
		}
		// overflow超出处理能力，直接返回
		if (integerStr.length() > IUNIT.length) {
			System.out.println(money + ":超出处理能力");
			return money;
		}
		int[] integers = toArray(integerStr);// 整数部分数字
		boolean isMust5 = isMust5(integerStr);// 设置万单位
		int[] decimals = toArray(decimalStr);// 小数部分数字
		return getChineseInteger(integers, isMust5)
				+ getChineseDecimal(decimals);
	}

	/**
	 * 整数部分和小数部分转换为数组，从高位至低位
	 */
	private static int[] toArray(String number) {
		int[] array = new int[number.length()];
		for (int i = 0; i < number.length(); i++) {
			array[i] = Integer.parseInt(number.substring(i, i + 1));
		}
		return array;
	}

	/**
	 * 得到中文金额的整数部分。
	 */
	private static String getChineseInteger(int[] integers, boolean isMust5) {
		StringBuffer chineseInteger = new StringBuffer("");
		int length = integers.length;
		for (int i = 0; i < length; i++) {
			// 0出现在关键位置：1234(万)5678(亿)9012(万)3456(元)
			// 特殊情况：10(拾元、壹拾元、壹拾万元、拾万元)
			String key = "";
			if (integers[i] == 0) {
				if ((length - i) == 13)// 万(亿)(必填)
					key = IUNIT[4];
				else if ((length - i) == 9)// 亿(必填)
					key = IUNIT[8];
				else if ((length - i) == 5 && isMust5)// 万(不必填)
					key = IUNIT[4];
				else if ((length - i) == 1)// 元(必填)
					key = IUNIT[0];
				// 0遇非0时补零，不包含最后一位
				if ((length - i) > 1 && integers[i + 1] != 0)
					key += NUMBERS[0];
			}
			chineseInteger.append(integers[i] == 0 ? key
					: (NUMBERS[integers[i]] + IUNIT[length - i - 1]));
		}
		return chineseInteger.toString();
	}

	/**
	 * 得到中文金额的小数部分。
	 */
	private static String getChineseDecimal(int[] decimals) {
		StringBuffer chineseDecimal = new StringBuffer("");
		for (int i = 0; i < decimals.length; i++) {
			// 舍去2位小数之后的
			if (i == 2)
				break;
			chineseDecimal.append(decimals[i] == 0 ? ""
					: (NUMBERS[decimals[i]] + DUNIT[i]));
		}
		return chineseDecimal.toString();
	}

	/**
	 * 判断第5位数字的单位"万"是否应加。
	 */
	private static boolean isMust5(String integerStr) {
		int length = integerStr.length();
		if (length > 4) {
			String subInteger = "";
			if (length > 8) {
				// 取得从低位数，第5到第8位的字串
				subInteger = integerStr.substring(length - 8, length - 4);
			} else {
				subInteger = integerStr.substring(0, length - 4);
			}
			return Integer.parseInt(subInteger) > 0;
		} else {
			return false;
		}
	}

	public static void main(String[] ar) {
		// System.out.println(new MoneyFormat().format("10005022.123009"));
		System.out.println(MoneyUtils.toChinese("11.2311"));
		System.out.println(MoneyUtils.toChinese("10.01"));
		System.out.println(MoneyUtils.toChinese("0"));
		System.out.println(MoneyUtils.toChinese(""));
		System.out.println(MoneyUtils.toChinese(null));

		// getNum(345);
		Double value = 12345.01d * 100;
		int intValue = value.intValue();
		System.out.println(value.intValue());
		getNum(intValue);

	}

	/**
	 * 
	 * @param value
	 *            输入的值
	 * @param totalLength
	 *            最大位数
	 * @param index
	 *            从最高位开始算起的顺序，也即要获取的位的值
	 * @return 超出index范围的返回-1，其他返回正确数字
	 */
	public static Integer getNum(Integer value, Integer totalLength,
			Integer index) {

		String result = String.format("%0" + totalLength + "d", value);

		String[] integers = result.split("");

		if (index > integers.length - 1) {
			return -1;
		}

		return Integer.parseInt(integers[index]);
	}

	public static void getNum(int num) {
		int b1 = num / 10000;
		System.out.println(b1);
		int b2 = (num - b1 * 10000) / 1000;
		System.out.println(b2);
		int b3 = (num - b1 * 10000 - b2 * 1000) / 100;
		System.out.println(b3);
		int b4 = (num - b1 * 10000 - b2 * 1000 - b3 * 100) / 10;
		System.out.println(b4);
		int b5 = (num - b1 * 10000 - b2 * 1000 - b3 * 100 - b4 * 10) / 1;
		System.out.println(b5);
	}

	public static void getDoubleNum(double num) {
		double b1 = num / 10000;
		System.out.println(b1);
		double b2 = (num - b1 * 10000) / 1000;
		System.out.println(b2);
		double b3 = (num - b1 * 10000 - b2 * 1000) / 100;
		System.out.println(b3);
		double b4 = (num - b1 * 10000 - b2 * 1000 - b3 * 100) / 10;
		System.out.println(b4);
		double b5 = (num - b1 * 10000 - b2 * 1000 - b3 * 100 - b4 * 10) / 1;
		System.out.println(b5);
	}

}
