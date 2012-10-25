package ru.retbansk.mail.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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
		String string = "nothing actually.done.3";
		System.out.println(string.matches(".+[.].+[.].+"));
		String[] array = string.split("\\.");
		for (String string2 : array) {
			System.out.println(string2);
		}
		TaskReport report = null;
		List<TaskReport> reportList = null;
		reportList = new ArrayList<TaskReport>();
		if (string.matches(".+,.+,.+")) {
			String split[] = string.split(",");
			report = new TaskReport();
			report.setWorkDescription(split[0].trim());
			report.setStatus(split[1].trim());
			report.setElapsedTime(Integer.valueOf(split[2].trim())
					.intValue());
			reportList.add(report);
		}
		if (string.matches(".+[/.].+[/.].+")) {
			String split[] = string.split("\\.");
			report = new TaskReport();
			report.setWorkDescription(split[0].trim());
			report.setStatus(split[1].trim());
			report.setElapsedTime(Integer.valueOf(split[2].trim())
					.intValue());
			reportList.add(report);
		}
		if (string.matches(".+[/].+[/].+")) {
			String split[] = string.split("/");
			report = new TaskReport();
			report.setWorkDescription(split[0].trim());
			report.setStatus(split[1].trim());
			report.setElapsedTime(Integer.valueOf(split[2].trim())
					.intValue());
			reportList.add(report);
		}
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+3"));
		System.out.println(calendar.get(Calendar.HOUR_OF_DAY));
		
		
		
		

	}

}
