package ru.retbansk.mail.domain;



import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import ru.retbansk.utils.ReplyManager;
import ru.retbansk.utils.ReplyManagerSimpleImpl;
import ru.retbansk.utils.UsefulMethods;

public class test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Properties props = UsefulMethods.loadProperties();
		JavaMailSenderImpl mailSender = new ReplyManagerSimpleImpl().getMailSender();
		SimpleMailMessage msg = new SimpleMailMessage();
		SimpleMailMessage[] msgs = new SimpleMailMessage[4];
		for (int i = 0; i < 4; i++) {
			msgs[i] = new SimpleMailMessage();
			msgs[i].setFrom("tester.testerovich@yandex.ru");
			msgs[i].setSubject("Test report "+(i+1));
			msgs[i].setTo(props.getProperty("user"));
		}
		System.out.println(props.getProperty("user"));
		msgs[0].setFrom("tester.testerovich@yandex.ru");
		msgs[0].setText("helping Google with Android, in process, 7\nmain job/ in process/1\nnothing actually. done. 3");
		mailSender.setUsername("tester.testerovich@yandex.ru");
		mailSender.setPassword("tester");
		mailSender.send(msgs[0]);
		
		
		

	}

}
