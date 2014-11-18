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
package org.beyene.protege.example;

import java.util.Arrays;

import org.beyene.protege.core.ComplexType;
import org.beyene.protege.core.Configuration;
import org.beyene.protege.core.Element;
import org.beyene.protege.core.Length;
import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.Unit;
import org.beyene.protege.core.UnitSet;
import org.beyene.protege.core.Value;
import org.beyene.protege.core.encoding.DoubleEncoding;
import org.beyene.protege.core.header.UniqueKeyValue;

public class Units {

	private Units() {
		// private constructor to prevent instantiation
	}

	public static Unit getExample() {
		// create type 'person'
		Length l = new Length();
		l.setPrecedingLengthFieldSize(4);

		Element firstName = new Element();
		firstName.setId("first-name");
		firstName.setType(Type.STRING);
		firstName.setLength(l);

		Element lastName = new Element();
		lastName.setId("last-name");
		lastName.setType(Type.STRING);
		lastName.setLength(l);
		
		Length genderLength = new Length();
		genderLength.setQuantity(8);
		
		Element gender = new Element();
		gender.setId("gender");
		gender.setType(Type.STRING);
		gender.setLength(genderLength);		

		ComplexType personType = new ComplexType();
		personType.setElements(Arrays.asList(firstName, lastName, gender));
		personType.setName("person");

		Unit unit = new Unit();
		unit.setName("get-group");
		unit.setComplexTypes(Arrays.asList(personType));

		// create body
		Length m = new Length();
		m.setPrecedingLengthFieldSize(8);

		Element personList = new Element();
		personList.setClassification("person");
		personList.setLength(m);

		ComplexType body = new ComplexType();
		body.setElements(Arrays.asList(personList));
		unit.setBody(body);

		return unit;
	}

	public static Protocol getProtocol() {
		// create header
		Value value = new Value();
		value.setHex("DEADBEEF");

		Element packetHeader = new Element();
		packetHeader.setType(Type.BYTE);
		packetHeader.setValue(value);

		Element version = new Element();
		version.setType(Type.INTEGER);
		Length versionLength = new Length();
		versionLength.setQuantity(8);
		version.setLength(versionLength);
		version.setId("version");
		
		Element totalLength = new Element();
		totalLength.setType(Type.INTEGER);
		totalLength.setLength(versionLength);
		totalLength.setId("total-length");
		
		Element unitId = new Element();
		unitId.setLength(versionLength);
		unitId.setType(Type.BYTE);
		unitId.setId("unit-id");
		
		ComplexType header = new ComplexType();
		header.setElements(Arrays.asList(packetHeader, version, totalLength, unitId));
		Configuration c = new Configuration();
		c.setTotalLengthId(totalLength.getId());
		c.setVersionId(version.getId());
		header.setConfiguration(c);
		
		Protocol p = new Protocol();
		p.setName("hello");
		p.setHeader(header);
		
		// create type 'person'
		Length l = new Length();
		l.setPrecedingLengthFieldSize(8);

		Element firstName = new Element();
		firstName.setId("first-name");
		firstName.setType(Type.STRING);
		firstName.setLength(l);

		Element lastName = new Element();
		lastName.setId("last-name");
		lastName.setType(Type.STRING);
		lastName.setLength(l);
		
		Length genderLength = new Length();
		genderLength.setQuantity(8);
		
		Element gender = new Element();
		gender.setId("gender");
		gender.setType(Type.STRING);
		gender.setLength(genderLength);		

		ComplexType personType = new ComplexType();
		personType.setElements(Arrays.asList(firstName, lastName, gender));
		personType.setName("person");

		// create unit request
		
		Unit request = new Unit();
		request.setName("hello-request");
		UniqueKeyValue ukv1 = new UniqueKeyValue();
		Value v1 = new Value();
		v1.setHex("00");
		ukv1.setValue(v1);
		request.setKeyValue(ukv1);
		
		ComplexType requestBody = new ComplexType();
		Element requestElement = new Element();
		requestElement.setClassification(personType.getName());
		Length requestLength = new Length();
		requestElement.setLength(requestLength);
		requestBody.setElements(Arrays.asList(requestElement));
		request.setBody(requestBody);
		
		// create unit response
		
		Unit response = new Unit();
		response.setName("hello-response");
		UniqueKeyValue ukv2 = new UniqueKeyValue();
		Value v2 = new Value();
		v2.setHex("01");
		ukv2.setValue(v2);
		response.setKeyValue(ukv2);

		// create body
		Length m = new Length();
		m.setPrecedingLengthFieldSize(8);

		Element personList = new Element();
		personList.setClassification(personType.getName());
		personList.setLength(m);

		Element averageAge = new Element();
		averageAge.setType(Type.DOUBLE);
		averageAge.setClassification(DoubleEncoding.IEEE_754_DOUBLE.getKey());
		Length age = new Length();
		averageAge.setLength(age);
		averageAge.setId("average-age");
		
		ComplexType body = new ComplexType();
		body.setElements(Arrays.asList(personList, averageAge));
		response.setBody(body);

		p.setComplexTypes(Arrays.asList(personType));
		
		UnitSet units = new UnitSet();
		units.setUnits(Arrays.asList(request, response));
		units.setUniqueKeyId(unitId.getId());
		units.setTotalLengthId(totalLength.getId());
		
		p.setUnits(units);
		return p;
	}
}