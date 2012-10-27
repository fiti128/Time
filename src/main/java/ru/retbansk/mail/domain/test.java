package ru.retbansk.mail.domain;



import ru.retbansk.utils.ReplyManager;
import ru.retbansk.utils.ReplyManagerSimpleImpl;

public class test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Reply reply = new Reply();
		reply.setEmailAddress("tr-legres@rambler.ru");
		ReplyManager man = new ReplyManagerSimpleImpl();
		
			man.placeReply(reply);
		

	}

}
