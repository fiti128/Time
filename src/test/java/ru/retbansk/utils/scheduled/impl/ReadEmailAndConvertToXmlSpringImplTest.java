/*
 * Copyright 2012 the original author.
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
package ru.retbansk.utils.scheduled.impl;

import java.io.File;
import java.io.IOException;


import java.util.HashSet;
import java.util.Iterator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.retbansk.mail.domain.DayReport;
import ru.retbansk.mail.domain.TaskReport;
import ru.retbansk.utils.UsefulMethods;

/**
 * 
 * @author Siarhei Yanusheuski
 * @since 25.10.2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:applicationContext.xml"})
public class ReadEmailAndConvertToXmlSpringImplTest  {
	public static String HOST = "pop.yandex.ru";
	public static String USER = "another.tester@yandex.ru";
	public static String USER2 = "tester.testerovich@yandex.ru";
	public static String PASSWORD = "server";
	public static String PATH = "C:/XmlReports/";
	public static String FILE_PATH = PATH + USER2 + "/report.from.18.10.12.xml";
	public static String CONTINUE = "yes";
	public static String TEST_STRING = "helping Google with Android";
	public static String TEST_STRING2 = "nothing actually";
	public static String BODY = "helping Google with Android, in process, 7\r\nAbrakadabraaaaaaaaaaaaaa tata\r\nnothing actually. done. 3";
	public static ReadEmailAndConvertToXmlSpringImpl reader;
	public static HashSet<DayReport> dayReportSet;
	protected static Logger logger = Logger.getLogger("service");
	
	@BeforeClass
	public static void beforeClass() {
		 
		reader = new ReadEmailAndConvertToXmlSpringImpl();
		try {
			UsefulMethods.populate();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		DayReport dayReport = new DayReport();
		dayReport.setPersonId(USER2);
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+3"));
		calendar.set(2012, Calendar.OCTOBER, 18);
		dayReport.setCalendar(calendar);
		List<TaskReport> taskList = new ArrayList<TaskReport>();
		TaskReport taskReport = new TaskReport();
		taskReport.setDate(calendar.getTime());
		taskReport.setElapsedTime(1);
		taskReport.setStatus(TEST_STRING);
		taskReport.setWorkDescription(TEST_STRING2);
		taskList.add(taskReport);
		dayReport.setReportList(taskList);
		dayReportSet = new HashSet<DayReport>();
		dayReportSet.add(dayReport);
		
		
	}
	@Test
	public void loadPropertiesTest() throws Exception {
		Properties testProperties = reader.loadProperties();
		Assert.assertEquals(HOST, testProperties.getProperty("host"));
		Assert.assertEquals(PASSWORD, testProperties.getProperty("password"));
		Assert.assertEquals(PATH, testProperties.getProperty("path"));
		Assert.assertEquals(CONTINUE, testProperties.getProperty("continue"));
		
	}
	
	@Test
	public void readEmailTest() throws Exception {
		HashSet<DayReport> daySet = reader.readEmail();
		Assert.assertNotNull(daySet);
		Assert.assertEquals(2, daySet.size());
		DayReport fromTester = null;
		DayReport fromAnotherTester = null;
		SortedSet<DayReport> sortedDaySet = new TreeSet<DayReport>();
		sortedDaySet.addAll(daySet);
		Assert.assertEquals(2, sortedDaySet.size());
		Iterator<DayReport> iterator = sortedDaySet.iterator();
		while (iterator.hasNext()) {
			fromAnotherTester = iterator.next();
			fromTester = iterator.next();
								}
		
		Assert.assertNotNull(fromAnotherTester);
		Assert.assertNotNull(fromTester);
		Assert.assertEquals(USER2, fromTester.getPersonId());
		Assert.assertEquals(USER, fromAnotherTester.getPersonId());
		Assert.assertEquals(3, fromTester.getReportList().size());
		Assert.assertEquals(1, fromAnotherTester.getReportList().size());
		Assert.assertEquals(TEST_STRING, fromTester.getReportList().get(0).getWorkDescription());
		Assert.assertEquals(8, fromAnotherTester.getReportList().get(0).getElapsedTime());
		Assert.assertEquals(TEST_STRING2, fromTester.getReportList().get(2).getWorkDescription());
		
	}
/**
 * Just testing the file creation.
 * Marshalling is tested in Jaxb2MarshallerTest
 * 
 */
	@Test
	public void createFileTest() throws Exception {
		Assert.assertNotNull(dayReportSet);
		reader.convertToXml(dayReportSet);
		Assert.assertTrue(new File(FILE_PATH).isFile());

		
		
	}
	
	@Test
	public void giveValidReportsTest() {
		Date date = new Date();
		List<TaskReport> reportList = reader.giveValidReports(BODY, date);
		Assert.assertNotNull(reportList);
		Assert.assertEquals(2,reportList.size());
		Assert.assertEquals(TEST_STRING2, reportList.get(1).getWorkDescription());
	}
	@AfterClass
	public  static void afterClass() {
		try {
			FileUtils.deleteDirectory(new File(PATH));
		} catch (IOException e) {
			logger.info("Plz close all windows that are related with "+PATH);
		}
	}
}

