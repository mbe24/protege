/*
 * Copyright 2014 Mikael Beyene
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
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