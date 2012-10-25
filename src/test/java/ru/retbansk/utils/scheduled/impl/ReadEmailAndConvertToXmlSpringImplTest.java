package ru.retbansk.utils.scheduled.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;




import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;






import ru.retbansk.mail.domain.DayReport;

import ru.retbansk.mail.domain.TaskReport;
import ru.retbansk.utils.marshaller.Jaxb2Marshaller;
import ru.retbansk.utils.marshaller.Marshaller;
import ru.retbansk.utils.scheduled.ReadEmailAndConvertToXml;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:applicationContext.xml"})
public class ReadEmailAndConvertToXmlSpringImplTest  {
	public static String HOST = "pop.yandex.ru";
	public static String USER = "kirill.iliashovitch@yandex.ru";
	public static String PASSWORD = "znich128";
	public static String PATH = "C:/XmlReports/";
	public static String CONTINUE = "yes";
	private static ReadEmailAndConvertToXml reader;
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
		Assert.assertEquals(2, daySet.size());
		DayReport fromLegres = null;
		DayReport fromKirill = null;
		Iterator<DayReport> iterator = daySet.iterator();
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
/*
 * Just testing the file creation.
 * Marshalling is tested in Jaxb2MarshallerTest
 */
	@Test
	public void createFileTest() throws Exception {
		Assert.assertNotNull(dayReportSet);
		reader.convertToXml(dayReportSet);
		Assert.assertTrue(new File("C:/XmlReports/tr-legres@rambler.ru/report.from.18.10.12.xml").isFile());
		
		
	}
}
