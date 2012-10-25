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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.retbansk.mail.domain.DayReport;
import ru.retbansk.mail.domain.TaskReport;
import ru.retbansk.utils.marshaller.Jaxb2Marshaller;
import ru.retbansk.utils.scheduled.ReadEmailAndConvertToXml;

public class ReadEmailAndConvertToXmlImpl implements ReadEmailAndConvertToXml {
	protected static Logger logger = Logger.getLogger("service");
	private String path;


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
		convertToXml(readEmail());

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

		Message[] message = folder.getMessages();
		Collections.reverse(Arrays.asList(message));
		HashSet<DayReport> dayReportSet = new HashSet<DayReport>();

		// Display message.
		String body;
		for (int i = 0; i < message.length; i++) {
			DayReport dayReport = null;
			dayReport = new DayReport();
			dayReport.setPersonId(((InternetAddress) message[i].getFrom()[0])
					.getAddress());
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			calendar.setTime(message[i].getSentDate());
			dayReport.setCalendar(calendar);
			TaskReport report = null;
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

			String lines[] = body.split("[\\r\\n]+");

			for (String string : lines) {
				if (string.matches(".+,.+,.+")) {
					String split[] = string.split(",");
					report = new TaskReport();
					report.setDate(message[i].getSentDate());
					report.setWorkDescription(split[0].trim());
					report.setStatus(split[1].trim());
					report.setElapsedTime(Integer.valueOf(split[2].trim())
							.intValue());
					reportList.add(report);
				}
			}
			dayReport.setReportList(reportList);
			dayReportSet.add(dayReport);
		}
		
		return dayReportSet;
	}

	@Override
	public void convertToXml(HashSet<DayReport> dayReportSet) throws Exception {
		Properties prop = loadProperties();
		for (DayReport dayReport : dayReportSet) {
			path = prop.getProperty("path");
			File dir = null;
			File file = null;
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
	
		JAXBContext context = JAXBContext.newInstance(DayReport.class);
		Marshaller m = context.createMarshaller();	
		
		/*		Uncomment this to see xml in console
		 * 		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
				m.marshal(dayReport, out);
				
		*/	
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		m.marshal(dayReport, file);

	}
	}
}
