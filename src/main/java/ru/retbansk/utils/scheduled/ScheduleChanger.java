package ru.retbansk.utils.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ru.retbansk.utils.UsefulMethods;

@Component
public class ScheduleChanger {

	@Autowired
	private DynamicSchedule dynamicSchedule;

   @Scheduled(fixedDelay=3000)
   public void change() {
	  try {
		dynamicSchedule.setDelay(Integer.valueOf(UsefulMethods.loadProperties().getProperty("schedule")).intValue());
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     
   }

}