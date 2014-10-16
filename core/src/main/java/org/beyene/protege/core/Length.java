package org.beyene.protege.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "length", propOrder = { "bit" })
public class Length {

	private Integer precedingLengthField;
	private Integer paddingLeft;
	private Integer paddingRight;
	private Integer bit;

	@XmlAttribute(name = "preceding-length-field")
	public Integer getPrecedingLengthFieldSize() {
		return precedingLengthField;
	}

	public void setPrecedingLengthFieldSize(Integer precedingLengthFieldSize) {
		this.precedingLengthField = precedingLengthFieldSize;
	}

	@XmlAttribute(name = "padding-left")
	public Integer getPaddingLeft() {
		return paddingLeft;
	}

	public void setPaddingLeft(Integer paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	@XmlAttribute(name = "padding-right")
	public Integer getPaddingRight() {
		return paddingRight;
	}

	public void setPaddingRight(Integer paddingRight) {
		this.paddingRight = paddingRight;
	}

	@XmlElement(name = "bit")
	public Integer getBit() {
		return bit;
	}

	public void setBit(Integer bit) {
		this.bit = bit;
	}
}