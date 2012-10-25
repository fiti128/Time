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
