package ru.retbansk.utils.scheduled;

import ru.retbansk.mail.domain.DayReport;

public interface ReadEmailAndConvertToXml {
	public void execute() throws Exception;
	public DayReport readEmail() throws Exception;
	public void convertToXml(DayReport dayReport) throws Exception;
}
