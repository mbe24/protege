package org.beyene.protege.core;

import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "type")
public enum Type {

	@XmlEnumValue("boolean")
	BOOLEAN,
	
	@XmlEnumValue("byte")
	BYTE,

	@XmlEnumValue("string")
	STRING,

	@XmlEnumValue("integer")
	INTEGER,
	
	//IEEE 754 - single (32bit)
	@XmlEnumValue("float")
	FLOAT,
	
	//IEEE 754 - double (64bit)
	@XmlEnumValue("double")
	DOUBLE;
}