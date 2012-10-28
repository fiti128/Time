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
package ru.retbansk.utils.scheduled;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

public class DynamicSchedule implements Trigger {
	protected static Logger logger = Logger.getLogger("service");
	   private TaskScheduler scheduler;
	   private Runnable task;
	   private int delay;
	   private ScheduledFuture<?> scheduledFuture;

	   public DynamicSchedule(TaskScheduler scheduler, Runnable task, int delay) {
	      this.scheduler = scheduler;
	      this.task = task;
	      reset(delay);
	   }
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
	   public void setDelay(int delay) {
		   if (delay != this.delay) logger.info("New delay is: "+getElapsedTimeHoursMinutesSecondsString(delay)); 
		   if (delay < this.delay) {
			   scheduledFuture.cancel(true);
			   reset(delay); 
		   }
		   this.delay = delay;
	   }
	   public void reset(int delay) {
	      this.delay = delay;
          scheduledFuture = scheduler.schedule(task, this);
	   }

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
