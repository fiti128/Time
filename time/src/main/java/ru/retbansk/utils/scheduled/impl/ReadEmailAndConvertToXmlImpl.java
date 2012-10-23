package ru.retbansk.utils.scheduled.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
		
		try {
			inputStream = new FileInputStream("email.properties");
			prop.load(inputStream);
		} catch (FileNotFoundException e) {
		}

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
	public DayReport readEmail() throws Exception {

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
		DayReport dayReport = new DayReport();
		dayReport.setPersonId(((InternetAddress) message[0].getFrom()[0])
				.getAddress());
		dayReport.setDate(message[0].getSentDate());
		TaskReport report;
		List<TaskReport> reportList = new ArrayList<TaskReport>();
		// Display message.
		String body;
		for (int i = 0; i < message.length; i++) {
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

		}
		dayReport.setReportList(reportList);
		return dayReport;
	}

	@Override
	public void convertToXml(DayReport dayReport) throws Exception {
		JAXBContext context = JAXBContext.newInstance(DayReport.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

/*		Uncomment this to see xml in console
 * 		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		m.marshal(dayReport, out);
*/

		path += dayReport.getPersonId();
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		String date = new SimpleDateFormat("dd.MM.YY").format(dayReport
				.getDate());
		File file = new File(path + "/report.from." + date + ".xml");

		logger.info("Report created - "+path + "/report.from." + date + ".xml");
		m.marshal(dayReport, file);

	}

}
