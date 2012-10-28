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
package ru.retbansk.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Properties;

import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


import org.apache.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
/**
 * 
 * @author Siarhei Yanusheuski
 * @since 25.10.2012
 */
public class UsefulMethods {
	protected static Logger logger = Logger.getLogger("service");
	public static Properties loadProperties() throws Exception {
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
		// Implementing user exit
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
	public static String prettyFormat(String input, int indent) {
        try
        {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // This statement works with JDK 6
            transformerFactory.setAttribute("indent-number", indent);
             
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        }
        catch (Throwable e)
        {
            // You'll come here if you are using JDK 1.5
            // you are getting an the following exeption
            // java.lang.IllegalArgumentException: Not supported: indent-number
            // Use this code (Set the output property in transformer.
            try
            {
                Source xmlInput = new StreamSource(new StringReader(input));
                StringWriter stringWriter = new StringWriter();
                StreamResult xmlOutput = new StreamResult(stringWriter);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
                transformer.transform(xmlInput, xmlOutput);
                return xmlOutput.getWriter().toString();
            }
            catch(Throwable t)
            {
                return input;
            }
        }
    }
 
    public static String prettyFormat(String input) {
        return prettyFormat(input, 2);
    }
    
    public static void populate() throws InterruptedException {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("another.tester@yandex.ru");
		msg.setSubject("Test report 4");
		msg.setTo("mr.server.serverovich@yandex.ru");
		msg.setText("Работаю над офигенным проектом, в процессе, 8");
		mailSender.setHost("smtp.yandex.ru");
		mailSender.setUsername("another.tester@yandex.ru");
		mailSender.setPassword("tester");
		mailSender.send(msg);
		SimpleMailMessage[] msgs = new SimpleMailMessage[3];
		for (int i = 0; i < 3; i++) {
			msgs[i] = new SimpleMailMessage();
			msgs[i].setFrom("tester.testerovich@yandex.ru");
			msgs[i].setSubject("Test report "+(i+1));
			msgs[i].setTo("mr.server.serverovich@yandex.ru");
		}
		msgs[2].setText("helping Google with Android, in process, 7\nmain job/ in process/1\nnothing actually. done. 3");
		msgs[1].setText("Testing error report");
		msgs[0].setText("ел, закончил, 2\nработаю, в процессе, 6");
		mailSender.setUsername("tester.testerovich@yandex.ru");
		mailSender.setPassword("tester");
		Thread.sleep(1000);
		mailSender.send(msgs[0]);
		Thread.sleep(1000);
		mailSender.send(msgs[1]);
		Thread.sleep(1000);
		mailSender.send(msgs[2]);
		
    }
}
