package ru.retbansk.utils.marshaller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

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
