/*
 * Copyright 2012 the original author.
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
package ru.retbansk.utils.scheduled;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
/**
 * Name tells everything. Its dynamic schedule. It is used to provide runtime configuration of the schedule. 
 * Main method is <code>setDelay(int delay)</code>. It automatically restarts schedule if the new delay is less than the old one. Or waits the next schedule if the new delay is greater 
 * @author Siarhei Yanusheuski
 * @since 25.10.2012
 */
public class DynamicSchedule implements Trigger {
	protected static Logger logger = Logger.getLogger("service");
	   private TaskScheduler scheduler;
	   private Runnable task;
	   private int delay;
	   private ScheduledFuture<?> scheduledFuture;
/**
 * Immediately restarts schedule at initializing
 * @param scheduler
 * @param task
 * @param delay Milliseconds (1000 = 1 second)
 */
	   public DynamicSchedule(TaskScheduler scheduler, Runnable task, int delay) {
	      this.scheduler = scheduler;
	      this.task = task;
	      reset(delay);
	   }
	   /**
	    * Returns pretty output of milliseconds
	    * @param milisec Milliseconds (1000 = 1 second)
	    * @return pretty String with the view like 00:00:00
	    */
	   public String getElapsedTimeHoursMinutesSecondsString(int milisec) {
		   int time = milisec/1000;
		   String seconds = Integer.toString((int)(time % 60));  
		   String minutes = Integer.toString((int)((time % 3600) / 60));  
		   String hours = Integer.toString((int)(time / 3600));  
		   for (int i = 0; i < 2; i++) {  
		   if (seconds.length() < 2) {  
		   seconds = "0" + seconds;  
		   }  
		   if (minutes.length() < 2) {  
		   minutes = "0" + minutes;  
		   }  
		   if (hours.length() < 2) {  
		   hours = "0" + hours;  
		   }  
		   }
		   String timeString =  hours + ":" + minutes + ":" + seconds; 
		   return timeString;
	   }
	   /**
	    * It automatically restarts schedule if the new delay is less than the old one. Or waits the next schedule if the new delay is greater 
	    * @param delay Milliseconds (1000 = 1 second)
	    */
	   public void setDelay(int delay) {
		   if (delay != this.delay) logger.info("New delay is: "+getElapsedTimeHoursMinutesSecondsString(delay)); 
		   if (delay < this.delay) {
			   scheduledFuture.cancel(true);
			   reset(delay); 
		   }
		   this.delay = delay;
	   }
	   /**
	    * Immediately restarts schedule
	    * @param delay Milliseconds (1000 = 1 second)
	    */
	   public void reset(int delay) {
	      this.delay = delay;
          scheduledFuture = scheduler.schedule(task, this);
	   }
	   /**
	    * @return Date next execution of the schedule
	    */
	   @Override
	   public Date nextExecutionTime(TriggerContext triggerContext) {
	      Date lastTime = triggerContext.lastActualExecutionTime();
	      Date nextExecutionTime = (lastTime == null)
	         ? new Date()
	         : new Date(lastTime.getTime() + delay);
	         logger.info("End of schedule. Next execution: " + nextExecutionTime +". Delay is: " + getElapsedTimeHoursMinutesSecondsString(delay));
	      return nextExecutionTime;
	   }

	}
