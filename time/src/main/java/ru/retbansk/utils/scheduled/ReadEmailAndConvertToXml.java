package ru.retbansk.utils.scheduled;

import java.util.Properties;

import ru.retbansk.mail.domain.DayReport;

public interface ReadEmailAndConvertToXml {
	public void execute() throws Exception;
	public DayReport readEmail() throws Exception;
	public void convertToXml(DayReport dayReport) throws Exception;
	public Properties loadProperties() throws Exception;
}
