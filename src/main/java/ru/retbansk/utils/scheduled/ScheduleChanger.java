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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ru.retbansk.utils.UsefulMethods;
/**
 * Additional thread to provide runtime configuration
 * 
 * @author Siarhei Yanusheuski
 * @since 25.10.2012
 * @see ru.retbansk.utils.scheduled.DynamicSchedule
 */
@Component
public class ScheduleChanger {
	protected static Logger logger = Logger.getLogger("service");
	@Autowired
	private DynamicSchedule dynamicSchedule;
/**
 * Scheduled task with fixed delay - 3 seconds
 * @see ru.retbansk.utils.scheduled.DynamicSchedule#setDelay(int)
 */
   @Scheduled(fixedDelay=3000)
   public void change() {
	  try {
		dynamicSchedule.setDelay(Integer.valueOf(UsefulMethods.loadProperties().getProperty("schedule")).intValue());
	} catch (NumberFormatException e) {
		logger.error(e.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage());
	}
     
   }

}