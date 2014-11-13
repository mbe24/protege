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
package org.beyene.protege.processor.util;

import java.util.Arrays;

import org.beyene.protege.core.ComplexType;
import org.beyene.protege.core.Element;
import org.beyene.protege.core.Length;
import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.Value;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.processor.HelloProtocol;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ProtocolUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetHeaderBytes() throws Exception {
	// create header
	Value value = new Value();
	value.setHex("DEADBEEF");

	Element packetHeader = new Element();
	packetHeader.setType(Type.BYTE);
	packetHeader.setValue(value);

	Element version = new Element();
	version.setType(Type.INTEGER);
	Length versionLength = new Length();
	versionLength.setBit(8);
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
	header.setElements(Arrays.asList(packetHeader, version, totalLength,
		unitId));

	Protocol p = new Protocol();
	p.setName("hello");
	p.setHeader(header);

	int bytes = ProtocolUtil.getHeaderBytes(p);
	Assert.assertEquals(7, bytes);

	// create malformed element
	versionLength = new Length();
	versionLength.setPrecedingLengthFieldSize(8);
	unitId = new Element();
	unitId.setLength(versionLength);
	unitId.setType(Type.BYTE);
	unitId.setId("unit-id");

	header = new ComplexType();
	header.setElements(Arrays.asList(packetHeader, version, totalLength,
		unitId));
	p.setHeader(header);

	thrown.expect(IllegalStateException.class);
	ProtocolUtil.getHeaderBytes(p);
    }

    @Test
    public void testGetTotalLength() throws Exception {
	Protocol p = HelloProtocol.get();
	
	DataUnit du = new DataUnit();
	Long tl = Long.valueOf(50);
	du.addPrimitiveValue("total-length", Type.INTEGER, tl);
	
	int totalLength = ProtocolUtil.getTotalLength(du, p);
	Assert.assertEquals(tl.intValue(), totalLength);
	
	du = new DataUnit();
	totalLength = ProtocolUtil.getTotalLength(du, p);
	Assert.assertEquals(-1, totalLength);
    }
}