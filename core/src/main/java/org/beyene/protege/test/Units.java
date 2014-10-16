package org.beyene.protege.test;

import java.util.Arrays;

import org.beyene.protege.core.ComplexType;
import org.beyene.protege.core.Element;
import org.beyene.protege.core.Length;
import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.Unit;
import org.beyene.protege.core.Value;

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

		ComplexType personType = new ComplexType();
		personType.setElements(Arrays.asList(firstName, lastName));
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

		ComplexType header = new ComplexType();
		header.setElements(Arrays.asList(packetHeader));

		Protocol p = new Protocol();
		p.setName("simple");
		p.setHeader(header);
		p.setUnits(Arrays.asList(getExample()));

		return p;
	}
}