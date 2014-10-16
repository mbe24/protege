package org.beyene.protege.core;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "protocol", propOrder = { "header", "units" })
public class Protocol {

	private String name;
	private ComplexType header;
	private List<Unit> units;

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "header")
	public ComplexType getHeader() {
		return header;
	}

	public void setHeader(ComplexType header) {
		this.header = header;
	}

	@XmlElementWrapper(name = "units")
	@XmlElement(name = "unit")
	public List<Unit> getUnits() {
		return units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}
}