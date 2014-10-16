package org.beyene.protege.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.beyene.protege.core.mapping.Mapping;

@XmlRootElement
@XmlType(name = "element", propOrder = { "length", "value", "mapping" })
public class Element {

	private String id;
	
	private Type type;
	private String classification;
	
	private Length length;
	private Value value;
	
	private Mapping mapping;

	@XmlAttribute(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute(name = "type", required = true)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@XmlAttribute(name = "classification")
	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	@XmlElement(name = "length", required = true)
	public Length getLength() {
		return length;
	}

	public void setLength(Length length) {
		this.length = length;
	}

	@XmlElement(name = "value")
	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	@XmlElement(name = "mapping")
	public Mapping getMapping() {
		return mapping;
	}

	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}
}