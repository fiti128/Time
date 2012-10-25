package ru.retbansk.mail;




import org.springframework.context.support.ClassPathXmlApplicationContext;



public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		new Thread(new Runnable() {
			public void run() {
				new ClassPathXmlApplicationContext("scheduledConfig.xml", Main.class);
			}
		}).start();
//		new ClassPathXmlApplicationContext("scheduledConfig.xml", Main.class);
//		new ReadEmailAndConvertToXmlSpringAnnotatedImpl().execute();
//		new Thread(new Runnable() {
//										public void run() {
//											Scanner sc = new Scanner(System.in);
//											if (sc.next().equals("5")) System.exit(0);
//											sc.close();
//										}
//		}).start();

	}

}
