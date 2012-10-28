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

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Implementation of the Jaxb2Marshaller with spring. Have only one method <code>marshal</code>
 * that marshals a valid Day Report into the File and returns pretty formatted xml String
 * @author Siarhei Yanusheuski
 * @since 25.10.2012
 * @see #marshal(Object, File)
 */
@Component(value="marshaller")
public class Jaxb2Marshaller implements Marshaller {

	@Autowired
	@Qualifier(value="jaxb2Marshaller")
	private org.springframework.oxm.jaxb.Jaxb2Marshaller marshaller;
	/**
	 * Marshals our valid Day Report into the File and returns pretty formatted xml String
	 * @param file Name of the xml file to be created or rewritten
	 * @param object Valid Day Report
	 * @return Formatted xml String
	 */
	public String marshal(Object object,File file) {
		final StringWriter out = new StringWriter();
			//Making our xml readable
		HashMap<String,Boolean> map = new HashMap<String,Boolean>();
		map.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setMarshallerProperties(map);
		marshaller.marshal(object, new StreamResult(out));
		
		if (file != null) {

			//Writing to file
		marshaller.marshal(object, new StreamResult(file));}
		return out.toString();
	}


}
