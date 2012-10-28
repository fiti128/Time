/*
 * Copyright 2002-2012 the original author.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.retbansk.utils.marshaller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.retbansk.mail.domain.DayReport;
import ru.retbansk.mail.domain.TaskReport;
import ru.retbansk.utils.UsefulMethods;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:applicationContext.xml"})
public class Jaxb2MarshallerTest {

	private static final String MARSHALLED_DAYREPORT =
		"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><DayReport personId=\"tr-legres@yandex.ru\"><Report><date>01-01-1981</date><elapsedTime>4</elapsedTime><status>in process</status><workDescription>helping Apple</workDescription></Report></DayReport>";
		
	private static String PERSON_ID = "tr-legres@yandex.ru";
	private static String STATUS = "in process";
	private static String WORK_DESCRIPTION = "helping Apple";
	
	private static List<TaskReport> list;
	

	@Autowired
	@Qualifier(value="marshaller")
	private Jaxb2Marshaller marshaller;

	@BeforeClass
	public static void beforeClass() {
		TaskReport taskReport = new TaskReport();
		Date date = new Date();
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.clear();
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.YEAR, 1981);
		date = calendar.getTime();
		taskReport.setDate(date);
		taskReport.setElapsedTime(4);
		taskReport.setStatus(STATUS);
		taskReport.setWorkDescription(WORK_DESCRIPTION);
		list = new ArrayList<TaskReport>();
		list.add(taskReport);
		
		

	}
	
	@Test
	public void marshallPerson() {
		DayReport dayReport = new DayReport();
		dayReport.setPersonId(PERSON_ID);
		dayReport.setReportList(list);
		
		String xml = marshaller.marshal(dayReport,null);
		Assert.assertNotNull(xml);
		String prettyExpected = UsefulMethods.prettyFormat(MARSHALLED_DAYREPORT,4);
		String prettyActual = UsefulMethods.prettyFormat(xml,4);
		Assert.assertEquals(prettyExpected, prettyActual);

	}


}
