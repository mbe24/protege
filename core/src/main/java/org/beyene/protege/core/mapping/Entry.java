package org.beyene.protege.core.mapping;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "entry")
public class Entry {

	private String key;
	private String value;

	public Entry() {
		// needed for jaxb
	}

	public Entry(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@XmlAttribute(name = "key")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@XmlAttribute(name = "value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}