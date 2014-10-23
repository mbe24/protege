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

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "protocol", propOrder = { "complexTypes", "header", "units" })
public class Protocol {

	private String name;
	private List<ComplexType> complexTypes;
	private ComplexType header;
	private UnitSet units;

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

	@XmlElement(name = "header")
	public ComplexType getHeader() {
		return header;
	}

	public void setHeader(ComplexType header) {
		this.header = header;
	}

	@XmlElement(name = "units")
	public UnitSet getUnits() {
		return units;
	}

	public void setUnits(UnitSet units) {
		this.units = units;
	}
}