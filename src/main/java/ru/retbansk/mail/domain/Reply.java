package ru.retbansk.mail.domain;

public class Reply {

	private String emailAddress;
	private int validNumber;
	private String xml;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public int getValidNumber() {
		return validNumber;
	}

	public void setValidNumber(int validNumber) {
		this.validNumber = validNumber;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

}