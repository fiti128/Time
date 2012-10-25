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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:applicationContext.xml"})
public class Jaxb2MarshallerTest {

	private static final String MARSHALLED_DAYREPORT =
		"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><DayReport personId=\"tr-legres@yandex.ru\"><Report><date>1981-01-01T03:00:00+03:00</date><elapsedTime>4</elapsedTime><status>� ��������</status><workDescription>������� �����</workDescription></Report></DayReport>";
		
	private static String personId;
	
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
		System.out.println(calendar.YEAR);
		taskReport.setDate(date);
		taskReport.setElapsedTime(4);
		taskReport.setStatus("� ��������");
		taskReport.setWorkDescription("������� �����");
		list = new ArrayList<TaskReport>();
		list.add(taskReport);
			
		personId = "tr-legres@yandex.ru";

	}
	
	@Test
	public void marshallPerson() {
		DayReport dayReport = new DayReport();
		dayReport.setPersonId(personId);
		dayReport.setReportList(list);
		
		String xml = marshaller.marshal(dayReport,null);
		System.out.println(xml);
		Assert.assertNotNull(xml);
		Assert.assertEquals(MARSHALLED_DAYREPORT, xml);

	}


}
