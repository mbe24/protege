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
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "units", propOrder = { "units" })
public class UnitSet {

	private String uniqueKeyId;
	private String totalLengthId;
	private List<Unit> units;

	@XmlAttribute(name = "unique-key-id")
	public String getUniqueKeyId() {
		return uniqueKeyId;
	}

	public void setUniqueKeyId(String uniqueKeyId) {
		this.uniqueKeyId = uniqueKeyId;
	}

	@XmlAttribute(name = "total-length-id")
	public String getTotalLengthId() {
		return totalLengthId;
	}

	public void setTotalLengthId(String totalLengthId) {
		this.totalLengthId = totalLengthId;
	}

	@XmlElement(name = "unit")
	public List<Unit> getUnits() {
		return units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}
}