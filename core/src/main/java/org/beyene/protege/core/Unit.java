package org.beyene.protege.core;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "unit", propOrder = { "complexTypes", "body" })
public class Unit {

	private String name;
	private List<ComplexType> complexTypes;
	private ComplexType body;

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElementWrapper(name = "complex-types")
	@XmlElement(name = "type")
	public List<ComplexType> getComplexTypes() {
		return complexTypes;
	}

	public void setComplexTypes(List<ComplexType> types) {
		this.complexTypes = types;
	}

	@XmlElement(name = "body")
	public ComplexType getBody() {
		return body;
	}

	public void setBody(ComplexType body) {
		this.body = body;
	}
}