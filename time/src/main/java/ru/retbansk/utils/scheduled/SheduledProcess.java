package ru.retbansk.utils.scheduled;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.retbansk.utils.Process;
import ru.retbansk.utils.scheduled.impl.ReadEmailAndConvertToXmlSpringImpl;

@Service
public class SheduledProcess implements Process {
	protected static Logger logger = Logger.getLogger("service");
	private ReadEmailAndConvertToXml reader = new ReadEmailAndConvertToXmlSpringImpl();

	@Scheduled(fixedDelay = 60000)
	public void process() {
		logger.info("Start schedule. Reading Email...");
		try {
			reader.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("End schedule. Next schedule in 1 minute.");
	}

}
