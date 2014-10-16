package org.beyene.protege.core.mapping;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.beyene.protege.core.Type;

@XmlType(name = "mapping", propOrder = { "entries" })
public class Mapping {

	private Type type;
	private List<Entry> entries;

	@XmlAttribute(name = "type", required = true)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@XmlElement(name = "entry", required = true)
	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
}