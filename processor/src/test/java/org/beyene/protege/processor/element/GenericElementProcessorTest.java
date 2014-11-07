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
import java.io.InputStream;

import org.beyene.protege.core.Element;
import org.beyene.protege.core.Length;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.encoding.IntegerEncoding;
import org.beyene.protege.core.encoding.StringEncoding;
import org.beyene.protege.processor.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

public class GenericElementProcessorTest {

    private final GenericElementProcessor ep = new GenericElementProcessor();
    
    @Test
    public void testFromStreamInteger() throws Exception {
	/*
	 * initialize element to 16 bit two's complement integer 
	 */
	Element e = new Element();
	e.setType(Type.INTEGER);
	e.setClassification(IntegerEncoding.TWOS_COMPLEMENT.getKey());
	Length l = new Length();
	l.setBit(16);
	e.setLength(l);
	
	// 255
	InputStream is = new ByteArrayInputStream(ByteUtil.toByteArray("00FF"));
	
	Long result = (Long) ep.fromStream(e, is);
	Assert.assertEquals(255L, result.longValue());
    }
    
    @Test
    public void testFromStreamString() throws Exception {
	String resultingString = "BadenWuerttemberg";
	String word = "0042006100640065006E0057007500650072007400740065006D0062006500720067";
	// zero padded 4 digit hex string representing 2 byte preceding length field
	String lengthPrefix = String.format("%4s", Integer.toHexString(8 * word.length() / 2)).replace(' ', '0');
	byte[] bytes = ByteUtil.toByteArray(lengthPrefix + word);
	
	Element e = new Element();
	e.setType(Type.STRING);
	e.setClassification(StringEncoding.UTF_16BE.getKey());
	Length l = new Length();
	l.setPrecedingLengthFieldSize(16);
	e.setLength(l);
	
	// BadenWuerttemberg
	InputStream is = new ByteArrayInputStream(bytes);
	Object result = ep.fromStream(e, is);
	Assert.assertEquals(resultingString, result);
    }
}