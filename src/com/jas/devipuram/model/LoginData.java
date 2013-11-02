package com.jas.devipuram.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Data")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginData {
	@XmlElement(name="UserId")
	private long learnerId;

	public long getLearnerId() {
		return learnerId;
	}

	public void setLearnerId(long learnerId) {
		this.learnerId = learnerId;
	}

}
