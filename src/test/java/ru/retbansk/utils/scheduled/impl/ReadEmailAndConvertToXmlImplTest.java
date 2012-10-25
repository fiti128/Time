package ru.retbansk.utils.scheduled.impl;



import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.retbansk.mail.domain.DayReport;



public class ReadEmailAndConvertToXmlImplTest {

	public static String HOST = "pop.yandex.ru";
	public static String USER = "kirill.iliashovitch@yandex.ru";
	public static String PASSWORD = "znich128";
	public static String PATH = "C:/XmlReports/";
	public static String CONTINUE = "yes";
	private static ReadEmailAndConvertToXmlSpringImpl reader;
	public static HashSet<DayReport> dayReportSet;
	
	@BeforeClass
	public static void beforeClass() {
		reader = new ReadEmailAndConvertToXmlSpringImpl();
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
		dayReportSet = reader.readEmail();
		Assert.assertNotNull(dayReportSet);
		Assert.assertEquals(2, dayReportSet.size());
		DayReport fromLegres = null;
		DayReport fromKirill = null;
		Iterator<DayReport> iterator = dayReportSet.iterator();
		while (iterator.hasNext()) {
			fromLegres = iterator.next();
			if (fromLegres.getPersonId().equals("tr-legres@rambler.ru") ) {
				fromKirill = iterator.next();
			}
			else {
				fromKirill = fromLegres;
				fromLegres = iterator.next();
			}
					}
		Assert.assertNotNull(fromKirill);
		Assert.assertNotNull(fromLegres);
		Assert.assertEquals("tr-legres@rambler.ru", fromLegres.getPersonId());
		Assert.assertEquals(2, fromLegres.getReportList().size());
		Assert.assertEquals(1, fromKirill.getReportList().size());
		Assert.assertEquals("ел", fromLegres.getReportList().get(0).getWorkDescription());
		Assert.assertEquals(4, fromKirill.getReportList().get(0).getElapsedTime());
		
	}
	
	@Test
	public void createFileTest() throws Exception {
		reader.convertToXml(dayReportSet);
		Assert.assertTrue(new File("C:/XmlReports/tr-legres@rambler.ru/report.from.18.10.12.xml").isFile());
		
		
	}
}
