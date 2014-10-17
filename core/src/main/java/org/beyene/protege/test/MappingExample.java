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
package org.beyene.protege.test;

import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.beyene.protege.core.Element;
import org.beyene.protege.core.Length;
import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.mapping.Entry;
import org.beyene.protege.core.mapping.Mapping;

public class MappingExample {

	public static void main(String[] args) throws JAXBException {
		Length l = new Length();
		l.setBit(8);

		Mapping m = new Mapping();
		m.setType(Type.STRING);
		m.setEntries(Arrays
				.asList(new Entry("0xA1",
						"Error #1: Whatch out, we have an error here!"),
						new Entry("0xB1",
								"Error #2: Whatch out, we have an error here!"),
						new Entry("0xC1",
								"Error #3: Whatch out, we have an error here!"),
						new Entry("0xD1",
								"Error #4: Whatch out, we have an error here!")));

		Element e = new Element();
		e.setType(Type.BYTE);
		e.setId("error");
		e.setLength(l);
		e.setMapping(m);

		JAXBContext context = JAXBContext.newInstance(Protocol.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(e, System.out);
	}
}