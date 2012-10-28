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
package ru.retbansk.utils.marshaller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;
/**
 * 
 * @author Siarhei Yanusheuski
 * @since 25.10.2012
 */
public class NiceDateAdapter extends XmlAdapter<String, Date> {
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public Date unmarshal(String dateString) throws Exception {
		return simpleDateFormat.parse(dateString);
	}

	@Override
	public String marshal(Date date) throws Exception {
		return simpleDateFormat.format(date);
	}

}
