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


import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.retbansk.utils.Process;
import ru.retbansk.utils.scheduled.impl.ReadEmailAndConvertToXmlSpringImpl;
/**
 * This class is used to provide scheduling for the program.
 * Automatically runs when scheduledConfig.xml is loaded
 *  
 * <p>It has one method process(). 
 * 
 *
 * @author Siarhei Yanusheuski
 * @since 25.10.2012
 * @see ru.retbansk.utils.scheduled.impl.ReadEmailAndConvertToXmlSpringImpl
 */
@Service
public class SheduledProcess implements Process {
	protected static Logger logger = Logger.getLogger("service");
	private ReadEmailAndConvertToXml reader = new ReadEmailAndConvertToXmlSpringImpl();


	/**
	 * Process() method would be invoked every minute with a fixed delay, meaning that the period will be measured from the completion time of each preceding invocation.
	 * <p> Configurable at runtime. 
	 * @see ru.retbansk.utils.Process#process()
	 * @see ru.retbansk.utils.scheduled.impl.ReadEmailAndConvertToXmlSpringImpl#execute()
	 */
	public void process() {
		logger.info("Start schedule. Reading Email...");
		try {
			reader.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
