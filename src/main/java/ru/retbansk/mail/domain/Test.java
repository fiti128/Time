package ru.retbansk.mail.domain;




import ru.retbansk.utils.UsefulMethods;
/**
 * Run to populate
 * Sending to mr.server.serverovich@yandex.ru four reports.
 * <p> One is invalid. Two from one email address
 * @see ru.retbansk.utils.UsefulMethods#populate()
 * @throws InterruptedException I used Thread.sleep method in be ensure in the order of sending
 * @author Siarhei Yanusheuski
 * @since 25.10.2012
 */
public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		UsefulMethods.populate();
		
	}

}
