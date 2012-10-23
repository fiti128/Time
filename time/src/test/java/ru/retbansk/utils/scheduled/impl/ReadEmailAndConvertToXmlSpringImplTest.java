package ru.retbansk.utils.scheduled.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
		DayReport dayReport = reader.readEmail();
		Assert.assertNotNull(dayReport);
		Assert.assertEquals("tr-legres@rambler.ru", dayReport.getPersonId());
		Assert.assertEquals(4, dayReport.getReportList().size());
	}

}
