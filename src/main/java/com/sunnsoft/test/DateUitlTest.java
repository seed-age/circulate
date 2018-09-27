package com.sunnsoft.test;

import java.text.ParseException;
import java.util.Calendar;

public class DateUitlTest {

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {

		Calendar calendarStart = Calendar.getInstance();
		Calendar calendarEnd = Calendar.getInstance();

		calendarStart.set(2010, 11, 16);
		calendarEnd.set(2011, 1, 16);

		double rate = 1d;
		double season = 90d;

		if (calendarStart.get(Calendar.DATE) != calendarEnd.get(Calendar.DATE)) {
			if (calendarStart.get(Calendar.YEAR) == calendarEnd
					.get(Calendar.YEAR)) {
				double startDay = calendarStart.get(Calendar.DAY_OF_YEAR);
				System.out.println("startDay=" + startDay);
				double endDay = calendarEnd.get(Calendar.DAY_OF_YEAR);
				System.out.println("endDay=" + endDay);
				rate = (endDay - startDay) / season;
				System.out.println("rate1============" + rate);
			} else {
				double startDayFirst = calendarStart.get(Calendar.DAY_OF_YEAR);
				System.out.println("startDayFirst=" + startDayFirst);
				double endDayFirst = calendarStart
						.getActualMaximum(Calendar.DAY_OF_YEAR);
				System.out.println("endDayFirst=" + endDayFirst);
				double rateFirst = (endDayFirst - startDayFirst);
				System.out.println("rateFirst===" + rateFirst);

				double startDaySec = 1d;
				System.out.println("startDaySec=" + startDaySec);

				double endDaySec = calendarEnd.get(Calendar.DAY_OF_YEAR);
				System.out.println("endDaySec=" + endDaySec);

				double rateSec = (endDaySec - startDaySec);
				System.out.println("rateSec===" + rateSec);

				rate = (rateFirst + rateSec) / season;
				System.out.println("rate2==================" + rate);
			}
		}

		System.out.println("所占比率为======" + rate);

	}

}
