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
package org.beyene.protege.processor.element;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;

import org.beyene.protege.core.Element;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.Value;
import org.beyene.protege.processor.exception.DataMismatchException;
import org.beyene.protege.processor.util.ByteUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ByteElementProcessorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private static final ByteElementProcessor ep = ByteElementProcessor.INSTANCE;
    
    @Test
        public void testRead() throws Exception {
    	Element e = new Element();
    	e.setType(Type.BYTE);
    	Value v = new Value();
    	v.setHex("DEADBEEF");
    	e.setValue(v);
    	
    	// read expected value
    	byte[] bytes = v.getBytes();
    	ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    	Byte[] result = ep.read(e, Channels.newChannel(is));
    	Assert.assertArrayEquals(bytes, ByteUtil.unbox(result));
    	
    	// read unexpected value
    	thrown.expect(DataMismatchException.class);
    	is = new ByteArrayInputStream(ByteUtil.toByteArray("BEEFDEAD"));
    	ep.read(e, Channels.newChannel(is));
    	
        }

    @Test
        public void testWrite() throws Exception {
    	Element e = new Element();
    	e.setType(Type.BYTE);
    	Value v = new Value();
    	v.setHex("DEADBEEF");
    	e.setValue(v);
    	
    	// write expected value
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	int length = ep.write(null, e, Channels.newChannel(os));
    	Assert.assertEquals(4, length);
    	
    	ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
    	byte[] result = ByteUtil.unbox(ep.read(e, Channels.newChannel(is)));
    	Assert.assertArrayEquals(v.getBytes(), result);
    	
    	// write unexpected value
    	thrown.expect(DataMismatchException.class);
    	os = new ByteArrayOutputStream();
    	ep.write(ByteUtil.box(ByteUtil.toByteArray("BEEFDEAD")), e, Channels.newChannel(os));
        }
}
