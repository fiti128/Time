package ru.retbansk.utils.scheduled.impl;



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
		HashSet<DayReport> dayReportSet = reader.readEmail();
		Assert.assertNotNull(dayReportSet);
		Assert.assertEquals(2, dayReportSet.size());
		DayReport fromLegres;
		DayReport fromKirill;
		Iterator<DayReport> iterator = dayReportSet.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getPersonId() == "tr-legres@rambler.ru") fromLegres = iterator.next();
			if (iterator.next().getPersonId() == "kirill.iliashovitch@yandex.ru") fromKirill = iterator.next();
		}
		Assert.assertNotNull(fromLegres);
		Assert.assertNotNull(fromKirill);
		Assert.assertEquals(2, fromLegres.getReportList().size());
		Assert.assertEquals(1, fromKirill.getReportList().size());
		Assert.assertEquals("ел", fromLegres.getReportList().get(0).getWorkDescription());
		Assert.assertEquals(4, fromKirill.getReportList().get(0).getElapsedTime());
		
	}

}
