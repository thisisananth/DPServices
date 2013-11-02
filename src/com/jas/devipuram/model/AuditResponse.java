package com.jas.devipuram.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="AuditResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuditResponse {
	@XmlElement(name="Status")
	private Status status;
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	

}
