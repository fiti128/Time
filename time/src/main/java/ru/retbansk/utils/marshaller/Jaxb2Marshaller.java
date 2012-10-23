package ru.retbansk.utils.marshaller;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component(value="marshaller")
public class Jaxb2Marshaller implements Marshaller {

	@Autowired
	@Qualifier(value="jaxb2Marshaller")
	private org.springframework.oxm.jaxb.Jaxb2Marshaller marshaller;
	
	public String marshal(Object object,File file) {
		final StringWriter out = new StringWriter();
		marshaller.marshal(object, new StreamResult(out));
		
		if (file != null) {
			//Making our xml readable
		HashMap<String,Boolean> map = new HashMap<String,Boolean>();
		map.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setMarshallerProperties(map);
			//Writing to file
		marshaller.marshal(object, new StreamResult(file));}
		return out.toString();
	}


}
