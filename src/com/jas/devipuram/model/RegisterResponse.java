package com.jas.devipuram.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="RegisterResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegisterResponse {
	
	@XmlElement(name="Status")
	private Status status;
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public RegisterData getData() {
		return data;
	}

	public void setData(RegisterData data) {
		this.data = data;
	}
	@XmlElement(name="Data")
	private RegisterData data;

}
