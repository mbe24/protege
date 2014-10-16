package org.beyene.protege.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "value")
public class Value {

	private String hex;

	@XmlAttribute(name = "hex")
	public String getHex() {
		return hex;
	}

	public void setHex(String hex) {
		this.hex = hex;
	}
}