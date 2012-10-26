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
			String body = "helping Google with Android, in process, 7\r\nAbrakadabraaaaaaaaaaaaaa tata\r\nnothing actually. done. 3";
			String[] strings = body.split("[\\r\\n]+");
			for (String string : strings) {
				System.out.println(string);
							}

	}

}
