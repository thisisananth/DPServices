package com.jas.devipuram.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="LoginResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginResponse {

	@XmlElement(name="Status")
	private Status status;
	@XmlElement(name="Data")
	private LoginData data;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public LoginData getData() {
		return data;
	}
	public void setData(LoginData data) {
		this.data = data;
	}
}
