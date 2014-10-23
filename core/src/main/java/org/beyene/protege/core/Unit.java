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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.beyene.protege.core.header.UniqueKeyValue;

@XmlRootElement
@XmlType(name = "unit", propOrder = { "keyValue", "complexTypes", "body" })
public class Unit {

	private String name;
	private UniqueKeyValue keyValue;
	private List<ComplexType> complexTypes;
	private ComplexType body;

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name = "unique-key-value")
	public UniqueKeyValue getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(UniqueKeyValue keyValue) {
		this.keyValue = keyValue;
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
	
	// TODO
	public Collection<Element> getUnitImage() {
		return Collections.emptyList();
	}
}