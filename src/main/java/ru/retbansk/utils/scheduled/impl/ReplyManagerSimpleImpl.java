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

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import ru.retbansk.mail.domain.DayReport;
import ru.retbansk.mail.domain.Reply;
import ru.retbansk.utils.UsefulMethods;
import ru.retbansk.utils.scheduled.ReplyManager;
/**
 * 
 * @author Siarhei Yanusheuski
 * @since 25.10.2012
 */
public class ReplyManagerSimpleImpl implements ReplyManager {
	protected static Logger logger = Logger.getLogger("service");
	private JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	private String user;
	public ReplyManagerSimpleImpl() throws Exception {
		Properties props = UsefulMethods.loadProperties();
		mailSender.setHost(props.getProperty("host.send"));
		mailSender.setUsername(props.getProperty("user"));
		mailSender.setPassword(props.getProperty("password"));
		// 25 587 465ssl
		mailSender.setPort(587);
		user = props.getProperty("user");
		
	}
	@Override
	public void placeReply(Reply reply) throws Exception {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(reply.getEmailAddress());
		msg.setReplyTo(reply.getEmailAddress());
		msg.setFrom(user);
		msg.setSubject("RE: "+ (reply.getSubject() == null ? "" : reply.getSubject()));
		msg.setText("This is an automated email. Do not reply.\n" +
				"Your day report is recieved and saved ." +
				" You are allowed to make modifications till 23:59 GMT+3." +
				" Just send new report, the old one will be deleted.\n" +
				"Converted report look like:\n"+reply.getXml());
		
		
	
    try{
        this.mailSender.send(msg);
    }
    catch(MailException ex) {
        logger.error(ex.getMessage());
    }
    }
	
	public void sendError(DayReport dayReport) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(dayReport.getPersonId());
		msg.setReplyTo(dayReport.getPersonId());
		msg.setFrom(user);
		msg.setSubject("RE: " + (dayReport.getSubject() == null ? "" : dayReport.getSubject()));
		msg.setText("This is an automated email. Do not reply.\n" +
				"No valid reports were detected. Check your report.\nExample of valid reports:\n" +
				"helping Google with Android, in process, 7\nmain job/ in process/1\nnothing actually. done. 3\n");
		
	    try{
	        this.mailSender.send(msg);
	    }
	    catch(MailException ex) {
	        logger.error(ex.getMessage());
	    }
	}
	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}
	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}
	
}   
