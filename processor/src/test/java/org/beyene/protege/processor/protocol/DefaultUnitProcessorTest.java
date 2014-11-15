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
package org.beyene.protege.processor.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Unit;
import org.beyene.protege.core.data.Composition;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.processor.HelloProtocol;
import org.beyene.protege.processor.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

public class DefaultUnitProcessorTest {

    private final DefaultUnitProcessor up = DefaultUnitProcessor.INSTANCE;
    
    @Test
    public void testFromStream() throws Exception {
	Protocol p = HelloProtocol.get();

	DataUnit du = new DataUnit();
	
	Unit reponse = null;
	for(Unit u : p.getUnits().getUnits())
	   if (u.getName().equals("hello-response"))
	       reponse = u;
	Assert.assertNotNull(reponse);
	
	du.setUnit(reponse);

	byte[] bytes = createHelloResponseBody();
	ByteArrayInputStream is = new ByteArrayInputStream(bytes);
	du = up.fromStream(du, p, is);
	
	Double averageAge = du.getPrimitiveValue("average-age", Primitive.DOUBLE);
	Assert.assertNotNull(averageAge);
	Assert.assertEquals(32.125d, averageAge, 0.00001);
	
	Iterator<Composition> personIt = du.getComplexCollection("persons").iterator();
	Composition personMax = personIt.next();
	Composition personJohn = personIt.next();
	Assert.assertEquals(2, du.getComplexCollection("persons").size());
	
	Assert.assertEquals("Max", personMax.getPrimitiveValue("first-name", Primitive.STRING));
	Assert.assertEquals("Mustermann", personMax.getPrimitiveValue("last-name", Primitive.STRING));
	Assert.assertEquals("M", personMax.getPrimitiveValue("gender", Primitive.STRING));
	
	Assert.assertEquals("John", personJohn.getPrimitiveValue("first-name", Primitive.STRING));
	Assert.assertEquals("Doe", personJohn.getPrimitiveValue("last-name", Primitive.STRING));
	Assert.assertEquals("M", personJohn.getPrimitiveValue("gender", Primitive.STRING));
    }

    @Test
    public void testToStream() throws Exception {
	Protocol p = HelloProtocol.get();
	DataUnit du = new DataUnit();
	Unit reponse = null;
	for(Unit u : p.getUnits().getUnits())
	   if (u.getName().equals("hello-response"))
	       reponse = u;
	Assert.assertNotNull(reponse);
	du.setUnit(reponse);
	DataUnit header = new DataUnit();
	header.setUnit(reponse);

	byte[] bytes = createHelloResponseBody();
	ByteArrayInputStream is = new ByteArrayInputStream(bytes);
	du = up.fromStream(du, p, is);
	
	// create data unit
	ByteArrayOutputStream os = new ByteArrayOutputStream();
	int bytesWritten = up.toStream(du, p, os);
	Assert.assertEquals(bytes.length, bytesWritten);
	
	is = new ByteArrayInputStream(os.toByteArray());
	DataUnit result = up.fromStream(header, p, is);
	
	Double averageAge = result.getPrimitiveValue("average-age", Primitive.DOUBLE);
	Assert.assertNotNull(averageAge);
	Assert.assertEquals(32.125d, averageAge, 0.00001);
	
	Iterator<Composition> personIt = result.getComplexCollection("persons").iterator();
	Composition personMax = personIt.next();
	Composition personJohn = personIt.next();
	Assert.assertEquals(2, result.getComplexCollection("persons").size());
	
	Assert.assertEquals("Max", personMax.getPrimitiveValue("first-name", Primitive.STRING));
	Assert.assertEquals("Mustermann", personMax.getPrimitiveValue("last-name", Primitive.STRING));
	Assert.assertEquals("M", personMax.getPrimitiveValue("gender", Primitive.STRING));
	
	Assert.assertEquals("John", personJohn.getPrimitiveValue("first-name", Primitive.STRING));
	Assert.assertEquals("Doe", personJohn.getPrimitiveValue("last-name", Primitive.STRING));
	Assert.assertEquals("M", personJohn.getPrimitiveValue("gender", Primitive.STRING));
    }
    
    static byte[] createHelloResponseBody() {
	StringBuilder sb = new StringBuilder();
	// occurrences of person, integer
	sb.append("02");
	
	// ----------------------
	
	// person 1, max mustermann
	
	// first name length
	sb.append("18");
	
	// first name
	sb.append("4D6178");
	
	// last name length
	sb.append("50");
	
	// last name
	sb.append("4D75737465726D616E6E");
	
	// gender
	sb.append("4D");
	
	// ----------------------
	
	// person 2, john doe
	
	// first name length
	sb.append("20");
	
	// first name
	sb.append("4A6F686E");
	
	// last name length
	sb.append("18");
	
	// last name
	sb.append("446F65");
	
	// gender
	sb.append("4D");
	
	// ----------------------
	
	// average-age, double 32.125
	sb.append("4040100000000000");
	return ByteUtil.toByteArray(sb.toString());
    }
}