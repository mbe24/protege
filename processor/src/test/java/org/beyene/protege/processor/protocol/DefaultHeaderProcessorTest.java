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

import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.Unit;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.processor.HelloProtocol;
import org.beyene.protege.processor.util.ByteUtil;
import org.beyene.protege.processor.util.ProtocolUtil;
import org.junit.Assert;
import org.junit.Test;

public class DefaultHeaderProcessorTest {

    private DefaultHeaderProcessor hp;

    @Test
    public void testFromStream() throws Exception {
	Protocol p = HelloProtocol.get();
	hp = new DefaultHeaderProcessor(p);
	
	StringBuilder sb = new StringBuilder();
	// header, byte
	sb.append("DEADBEEF");

	// version, integer
	sb.append("01");

	// total length = 50, integer
	sb.append("32");

	// unit id, byte
	sb.append("01");

	byte[] bytes = ByteUtil.toByteArray(sb.toString());
	ByteArrayInputStream is = new ByteArrayInputStream(bytes);
	DataUnit du = hp.fromStream(p, is);
	
	int totalLength = ProtocolUtil.getTotalLength(du, p);
	Assert.assertEquals(50, totalLength);
	
	byte[] mark = ByteUtil.unbox(du.getPrimitiveValue("header-mark", Primitive.BYTES));
	Assert.assertArrayEquals(ByteUtil.toByteArray("DEADBEEF"), mark);
	
	Unit unit = du.getUnit();
	Assert.assertNotNull(unit);
	Assert.assertEquals("hello-response", unit.getName());
    }

    @Test
    public void testToStream() throws Exception {
	Protocol p = HelloProtocol.get();
	hp = new DefaultHeaderProcessor(p);
	
	StringBuilder sb = new StringBuilder();
	// header, byte
	sb.append("DEADBEEF");

	// version, integer
	sb.append("01");

	// total length = 50, integer
	sb.append("32");

	// unit id, byte
	sb.append("01");
	
	byte[] bytes = ByteUtil.toByteArray(sb.toString());
	
	DataUnit du = new DataUnit();
	du.addPrimitiveValue("version", Type.INTEGER, Long.valueOf(1));
	du.addPrimitiveValue("total-length", Type.INTEGER, Long.valueOf(50));
	du.addPrimitiveValue("unit-id", Type.BYTE, new Byte[] {(byte) 0x01});
	
	ByteArrayOutputStream os = new ByteArrayOutputStream();
	int written = hp.toStream(du, p, os);
	Assert.assertEquals(bytes.length, written);
	
	byte[] result = os.toByteArray();
	Assert.assertArrayEquals(bytes, result);
    }
}