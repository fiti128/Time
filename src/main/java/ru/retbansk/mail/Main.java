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

package ru.retbansk.mail;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Main class that starts everything. 
 * <p>First point of the program. Run ScheduledProcess
 * 
 *
 * @author Siarhei Yanusheuski
 * @since 25.10.2012
 * @see ru.retbansk.utils.scheduled.SheduledProcess
 */
public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

				new ClassPathXmlApplicationContext("scheduledConfig.xml", Main.class);


	}

}
