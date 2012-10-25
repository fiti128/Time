package ru.retbansk.utils.scheduled;

import java.util.HashSet;
import java.util.Properties;

import ru.retbansk.mail.domain.DayReport;

public interface ReadEmailAndConvertToXml {
	public void execute() throws Exception;
	public HashSet<DayReport> readEmail() throws Exception;
	public Properties loadProperties() throws Exception;
	public void convertToXml(HashSet<DayReport> dayReportSet) throws Exception;
}
