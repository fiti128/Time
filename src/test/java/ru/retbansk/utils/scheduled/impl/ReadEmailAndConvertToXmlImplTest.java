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
package ru.retbansk.utils.scheduled.impl;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.retbansk.mail.domain.DayReport;
import ru.retbansk.utils.scheduled.ReadEmailAndConvertToXml;



public class ReadEmailAndConvertToXmlImplTest {
	public static String HOST = "pop.yandex.ru";
	public static String USER = "kirill.iliashovitch@yandex.ru";
	public static String USER2 = "tr-legres@rambler.ru";
	public static String PASSWORD = "znich128";
	public static String PATH = "C:/XmlReports/";
	public static String FILE_PATH = "C:/XmlReports/tr-legres@rambler.ru/report.from.18.10.12.xml";
	public static String CONTINUE = "yes";
	public static String TEST_STRING = "ел";
	public static String TEST_STRING2 = "nothing actually";
	public static ReadEmailAndConvertToXml reader;
	public static HashSet<DayReport> dayReportSet;
	
	@BeforeClass
	public static void beforeClass() {
		reader = new ReadEmailAndConvertToXmlSpringImpl();
		//Need to rewrite this
		try {
			dayReportSet = reader.readEmail();
		} catch (Exception e) {
		}
		
	}
	@Test
	public void loadPropertiesTest() throws Exception {
		Properties testProperties = reader.loadProperties();
		Assert.assertEquals(HOST, testProperties.getProperty("host"));
		Assert.assertEquals(USER, testProperties.getProperty("user"));
		Assert.assertEquals(PASSWORD, testProperties.getProperty("password"));
		Assert.assertEquals(PATH, testProperties.getProperty("path"));
		Assert.assertEquals(CONTINUE, testProperties.getProperty("continue"));
		
	}
	
	@Test
	public void readEmailTest() throws Exception {
		HashSet<DayReport> daySet = reader.readEmail();
		Assert.assertNotNull(daySet);
		Assert.assertEquals(3, daySet.size());
		DayReport fromLegres1 = null;
		DayReport fromLegres2 = null;
		DayReport fromKirill = null;
		SortedSet<DayReport> sortedDaySet = new TreeSet<DayReport>();
		sortedDaySet.addAll(daySet);
		Iterator<DayReport> iterator = sortedDaySet.iterator();
		while (iterator.hasNext()) {
			fromLegres1 = iterator.next();
			fromKirill = iterator.next();
			fromLegres2 = iterator.next();
					}
		
		Assert.assertNotNull(fromKirill);
		Assert.assertNotNull(fromLegres1);
		Assert.assertNotNull(fromLegres2);
		Assert.assertEquals(USER2, fromLegres1.getPersonId());
		Assert.assertEquals(USER2, fromLegres2.getPersonId());
		Assert.assertEquals(USER, fromKirill.getPersonId());
		Assert.assertEquals(2, fromLegres1.getReportList().size());
		Assert.assertEquals(1, fromKirill.getReportList().size());
		Assert.assertEquals(TEST_STRING, fromLegres1.getReportList().get(0).getWorkDescription());
		Assert.assertEquals(4, fromKirill.getReportList().get(0).getElapsedTime());
		Assert.assertEquals(TEST_STRING2, fromLegres2.getReportList().get(2).getWorkDescription());
		
	}
/*
 * Just testing the file creation.
 * Marshalling is tested in Jaxb2MarshallerTest
 */
	@Test
	public void createFileTest() throws Exception {
		Assert.assertNotNull(dayReportSet);
		reader.convertToXml(dayReportSet);
		Assert.assertTrue(new File(FILE_PATH).isFile());
		FileUtils.deleteDirectory(new File(PATH));
		
		
	}
}
