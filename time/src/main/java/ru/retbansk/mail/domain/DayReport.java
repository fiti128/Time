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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((personId == null) ? 0 : personId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DayReport other = (DayReport) obj;
		if (personId == null) {
			if (other.personId != null)
				return false;
		} else if (!personId.equals(other.personId))
			return false;
		return true;
	}
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
