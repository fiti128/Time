package ru.retbansk.mail.domain;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="DayReport")
public class DayReport {
	private Date date;
	
	private String personId;
	private List<TaskReport> reportList;
	public String getPersonId() {
		return personId;
	}
	@XmlAttribute
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public List<TaskReport> getReportList() {
		return reportList;
	}
	@XmlElement(name="Report")
	public void setReportList(List<TaskReport> reportList) {
		this.reportList = reportList;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
