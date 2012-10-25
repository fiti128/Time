package ru.retbansk.mail.domain;

import java.util.Calendar;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DayReport dayReport1 = new DayReport();
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(2012, 10, 25);
		dayReport1.setCalendar(calendar1);
		dayReport1.setPersonId("me");
		DayReport dayReport2 = new DayReport();
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2012, 10, 25);
		dayReport2.setCalendar(calendar2);
		dayReport2.setPersonId("me");
		System.out.println(calendar1);
		System.out.println(calendar2);
		System.out.println(dayReport1.getCalendar().get(Calendar.DAY_OF_MONTH));
		System.out.println(dayReport2.getCalendar().get(Calendar.DAY_OF_MONTH));
		System.out.println(dayReport1.equals(dayReport2));

	}

}
