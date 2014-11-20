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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.Unit;
import org.beyene.protege.core.data.Composition;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.processor.util.ByteUtil;

public class Units {

    private Units() {
	// private constructor to prevent instantiation
    }

    private static final Protocol p;

    static {
	Source source = new StreamSource(
		Units.class.getResourceAsStream("/message-protocol.xml"));
	Protocol tmp = null;
	try {
	    tmp = readXml(source, Protocol.class);
	} catch (JAXBException e) {
	    System.err.println("Error while reading protocol from xml: "
		    + e.getMessage());
	    System.exit(1);
	}
	p = tmp;
    }

    private static final Map<String, Unit> units;
    static {
	Map<String, Unit> tmp = new HashMap<>();
	for (Unit unit : p.getUnits())
	    tmp.put(unit.getName(), unit);
	units = Collections.unmodifiableMap(tmp);
    }

    public static Protocol getProtocol() {
	return p;
    }

    public static MetaData extractMetaData(DataUnit du) throws IOException, ClassNotFoundException {
	Composition md = du.getComplexCollection("metadata").get(0);
	String user = md.getPrimitiveValue("user", Primitive.STRING);
	
	Composition c = md.getComplexCollection("date").get(0);
	byte[] bytes = ByteUtil.unbox(c.getPrimitiveValue("java-object", Primitive.BYTES));
	ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
	Date date = Date.class.cast(ois.readObject());
	
	return new MetaData(date, user);
    }
    
    public static DataUnit getEmptyMessageFor(String unit, String user) throws IOException {
	Unit u = units.get(unit);
	if (u == null)
	    throw new IllegalArgumentException(String.format("There is no unit by name: %s.", unit));
	DataUnit message = new DataUnit();
	message.setUnit(u);
	message.addPrimitiveValue("header-mark", Type.BYTE, ByteUtil.box(ByteUtil.toByteArray("0420")));
	message.addPrimitiveValue("version", Type.INTEGER, Long.valueOf(1));
	message.addPrimitiveValue("unit", Type.BYTE, ByteUtil.box(u.getKeyValue().getValue().getBytes()));
	
	Composition c = new Composition();
	c.setId("metadata");
	c.addPrimitiveValue("user", Type.STRING, user);
	
	Date current = Calendar.getInstance().getTime();
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	ObjectOutputStream oos = new ObjectOutputStream(baos);
	oos.writeObject(current);
	
	Composition date = new Composition();
	date.setId("date");
	date.addPrimitiveValue("java-object", Type.BYTE, ByteUtil.box(baos.toByteArray()));
	c.addComplexObject("date", date);
	
	message.addComplexObject("metadata", c);
	return message;
    }

    private static <T> T readXml(Source source, Class<T> declaredType)
	    throws JAXBException {
	JAXBContext context = JAXBContext.newInstance(Protocol.class);
	Unmarshaller unmarshaller = context.createUnmarshaller();
	JAXBElement<T> result = unmarshaller.unmarshal(source, declaredType);
	return result.getValue();
    }
}