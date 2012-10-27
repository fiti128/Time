package ru.retbansk.utils;

import java.util.Properties;

import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import ru.retbansk.mail.domain.Reply;

public class ReplyManagerSimpleImpl implements ReplyManager {
	protected static Logger logger = Logger.getLogger("service");
	private JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	private String user;
	public ReplyManagerSimpleImpl() throws Exception {
		Properties props = UsefulMethods.loadProperties();
		mailSender.setHost("smtp.yandex.ru");
		mailSender.setUsername(props.getProperty("user"));
		mailSender.setPassword(props.getProperty("password"));
		// 25 587
		mailSender.setPort(587);
		user = props.getProperty("user");
		
	}
	@Override
	public void placeReply(Reply reply) throws Exception {

		SimpleMailMessage msg = new SimpleMailMessage();
		;
		msg.setTo(reply.getEmailAddress());
		msg.setFrom(user);
		msg.setSubject("RE: Day Report");
		msg.setText("This is an automated email. Do not reply.\n" +
				"Your day report is recieved and saved . You are allowed to make modifications till 23:59 GMT. Just send new report, the old one will be deleted.\nConverted xml look like:\n"+reply.getXml());
		
		
	
    try{
        this.mailSender.send(msg);
    }
    catch(MailException ex) {
        logger.error(ex.getMessage());
    }
    }
}   