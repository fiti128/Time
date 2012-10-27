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
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;






import ru.retbansk.mail.domain.DayReport;
import ru.retbansk.mail.domain.Reply;

import ru.retbansk.mail.domain.TaskReport;
import ru.retbansk.utils.ReplyManager;
import ru.retbansk.utils.ReplyManagerSimpleImpl;
import ru.retbansk.utils.marshaller.Jaxb2Marshaller;
import ru.retbansk.utils.marshaller.Marshaller;
import ru.retbansk.utils.scheduled.DynamicSchedule;
import ru.retbansk.utils.scheduled.ReadEmailAndConvertToXml;


public class ReadEmailAndConvertToXmlSpringImpl implements ReadEmailAndConvertToXml {
	protected static Logger logger = Logger.getLogger("service");
	private String path;
	private ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	private Marshaller marshaller = (Jaxb2Marshaller) context.getBean("marshaller");
	private List<Reply> replyList = new ArrayList<Reply>();

	
	public Properties loadProperties() throws Exception {
		Properties prop = new Properties();
		InputStream inputStream = null;
		// At first, program will try to read external properties
		try {
			inputStream = new FileInputStream("email.properties");
			prop.load(inputStream);
		} catch (FileNotFoundException e) {
		}
		// If nothing there - internal
		if (inputStream == null)
			inputStream = prop.getClass().getResourceAsStream(
					"/email.properties");

		prop.load(inputStream);
		if (inputStream != null)
			inputStream.close();
		try {
			if (prop.getProperty("continue").equals("no")){
				logger.info("Program was stopped by the User");
				System.exit(0);
			}
		} catch (Exception e) {
			logger.info("Continue parameter is not defined");
		}
		return prop;
	}

	@Override
	public void execute() throws Exception {
		HashSet<DayReport> dayReportSet = readEmail();
		convertToXml(dayReportSet);
		ReplyManager man = new ReplyManagerSimpleImpl();
		for (Reply reply1 : replyList) {
			
			man.placeReply(reply1);
		}
		replyList.clear();

	}

	@Override
	public HashSet<DayReport> readEmail() throws Exception {

		Properties prop = loadProperties();
		String host = prop.getProperty("host");
		String user = prop.getProperty("user");
		String password = prop.getProperty("password");
		path = prop.getProperty("path");

		// Get system properties
		Properties properties = System.getProperties();

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		// Get a Store object that implements the specified protocol.
		Store store = session.getStore("pop3");

		// Connect to the current host using the specified username and
		// password.
		store.connect(host, user, password);

		// Create a Folder object corresponding to the given name.
		Folder folder = store.getFolder("inbox");

		// Open the Folder.
		folder.open(Folder.READ_ONLY);
		HashSet<DayReport> dayReportSet = new HashSet<DayReport>();
		try {
			
		
		Message[] message = folder.getMessages();
		Collections.reverse(Arrays.asList(message));


		// Display message.
		String body;
		for (int i = 0; i < message.length; i++) {
			DayReport dayReport = null;
			dayReport = new DayReport();
			dayReport.setPersonId(((InternetAddress) message[i].getFrom()[0])
					.getAddress());
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+3"));
			calendar.setTime(message[i].getSentDate());
			dayReport.setCalendar(calendar);
			List<TaskReport> reportList = null;
			reportList = new ArrayList<TaskReport>();
			
			body = "";
			Object content = message[i].getContent();
			if (content instanceof java.lang.String) {
				body = (String) content;
			} else if (content instanceof Multipart) {
				Multipart mp = (Multipart) content;

				for (int j = 0; j < mp.getCount(); j++) {
					Part part = mp.getBodyPart(j);

					String disposition = part.getDisposition();

					if (disposition == null) {
						// Check if plain
						MimeBodyPart mbp = (MimeBodyPart) part;
						if (mbp.isMimeType("text/plain")) {
							// Log.Debug("Mime type is plain");
							body += (String) mbp.getContent();
						}
					} else if ((disposition != null)
							&& (disposition.equals(Part.ATTACHMENT) || disposition
									.equals(Part.INLINE))) {
						// Check if plain
						MimeBodyPart mbp = (MimeBodyPart) part;
						if (mbp.isMimeType("text/plain")) {
							// Log.Debug("Mime type is plain");
							body += (String) mbp.getContent();
						}
					}
				}
			}
			//Reads the body of the message and return list of valid reports
			reportList = giveValidReports(body,message[i].getSentDate());
			if (reportList.size() > 0) {
			dayReport.setReportList(reportList);
			dayReportSet.add(dayReport);
			}
		}
		}
		finally {
    		folder.close(false); // true tells the mail server to expunge deleted messages.
    		store.close();
		}
		
		return dayReportSet;
	}

	@Override
	public void convertToXml(HashSet<DayReport> dayReportSet) throws Exception {
		Properties prop = loadProperties();
		File dir;
		File file;
		String xml;
		
		// Writing file for every DayReport in Set
		for (DayReport dayReport : dayReportSet) {
			path = prop.getProperty("path");
			path += dayReport.getPersonId();
			dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String date = new SimpleDateFormat("dd.MM.YY").format(dayReport
					.getCalendar().getTime());
			file = new File(path + "/report.from." + date + ".xml");

			logger.info("Report created - " + path + "/report.from." + date
					+ ".xml");
			
			xml = marshaller.marshal(dayReport, file);
			Reply reply = new Reply();
			reply.setXml(xml);
			reply.setEmailAddress(dayReport.getPersonId());
			reply.setValidNumber(dayReport.getReportList().size());
			replyList.add(reply);
			
		}
	}
	
	public List<TaskReport> giveValidReports(String body, Date reportDate) {
		List<TaskReport> reportList = new ArrayList<TaskReport>();
		TaskReport report;
		String lines[] = body.split("[\\r\\n]+");

		for (String string : lines) {
			if (string.matches(".+,.+,[\\d|\\s]{1,3}")) {
				String split[] = string.split(",");
				report = new TaskReport();
				report.setDate(reportDate);
				report.setWorkDescription(split[0].trim());
				report.setStatus(split[1].trim());
				report.setElapsedTime(Integer.valueOf(split[2].trim())
						.intValue());
				reportList.add(report);
			}
			if (string.matches(".+[.].+[.][\\d|\\s]{1,3}")) {
				String split[] = string.split("\\.");
				report = new TaskReport();
				report.setDate(reportDate);
				report.setWorkDescription(split[0].trim());
				report.setStatus(split[1].trim());
				report.setElapsedTime(Integer.valueOf(split[2].trim())
						.intValue());
				reportList.add(report);
			}
			if (string.matches(".+[/].+[/][\\d|\\s]{1,3}")) {
				String split[] = string.split("/");
				report = new TaskReport();
				report.setDate(reportDate);
				report.setWorkDescription(split[0].trim());
				report.setStatus(split[1].trim());
				report.setElapsedTime(Integer.valueOf(split[2].trim())
						.intValue());
				reportList.add(report);
			}
		}
		return reportList;
	}


}
