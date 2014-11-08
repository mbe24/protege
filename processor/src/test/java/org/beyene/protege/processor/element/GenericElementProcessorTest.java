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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.beyene.protege.core.Element;
import org.beyene.protege.core.Length;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.core.encoding.DoubleEncoding;
import org.beyene.protege.core.encoding.Encoding;
import org.beyene.protege.core.encoding.FloatEncoding;
import org.beyene.protege.core.encoding.IntegerEncoding;
import org.beyene.protege.core.encoding.StringEncoding;
import org.beyene.protege.processor.atom.AtomProcessor;
import org.beyene.protege.processor.atom.AtomProcessorFactory;
import org.beyene.protege.processor.util.ByteTestUtil;
import org.beyene.protege.processor.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

public class GenericElementProcessorTest {

    private final GenericElementProcessor ep = new GenericElementProcessor();

    @Test
    public void testFromStreamInteger() throws Exception {
	Map<IntegerEncoding, Long> results = new HashMap<>();
	results.put(IntegerEncoding.TWOS_COMPLEMENT, -1L);
	results.put(IntegerEncoding.UNSIGNED, 65535L);

	for (Encoding<Long> encoding : Arrays.asList(
		IntegerEncoding.TWOS_COMPLEMENT, IntegerEncoding.UNSIGNED)) {
	    Element e = new Element();
	    e.setType(Type.INTEGER);
	    e.setClassification(encoding.getKey());
	    Length l = new Length();
	    l.setBit(16);
	    e.setLength(l);

	    // 65535 unsigned or -1 signed
	    InputStream is = new ByteArrayInputStream(
		    ByteUtil.toByteArray("FFFF"));

	    Long result = ep.fromStream(e, is, Primitive.INTEGER);
	    Assert.assertEquals(results.get(encoding).longValue(),
		    result.longValue());
	}
    }

    @Test
    public void testFromStreamDouble() throws Exception {
	double d1 = 64.5;
	String s1 = "4050200000000000";
	String s1n = "C050200000000000";

	Map<Double, String> results = new HashMap<>();
	results.put(d1, s1);
	results.put(-d1, s1n);

	Element e = new Element();
	e.setType(Type.DOUBLE);
	e.setClassification(DoubleEncoding.IEEE_754_DOUBLE.getKey());

	for (Entry<Double, String> entry : results.entrySet()) {
	    String hex = entry.getValue();
	    InputStream is = new ByteArrayInputStream(ByteUtil.toByteArray(hex));
	    Double result = ep.fromStream(e, is, Primitive.DOUBLE);
	    Assert.assertEquals(entry.getKey().longValue(), result.longValue());
	}
    }

    @Test
    public void testFromStreamFloat() throws Exception {
	float f2 = 32.125f;
	String s2 = "42008000";
	String s2n = "C2008000";

	Map<Float, String> results = new HashMap<>();
	results.put(f2, s2);
	results.put(-f2, s2n);

	Element e = new Element();
	e.setType(Type.FLOAT);
	e.setClassification(FloatEncoding.IEEE_754_SINGLE.getKey());

	for (Entry<Float, String> entry : results.entrySet()) {
	    String hex = entry.getValue();
	    InputStream is = new ByteArrayInputStream(ByteUtil.toByteArray(hex));
	    Float result = ep.fromStream(e, is, Primitive.FLOAT);
	    Assert.assertEquals(entry.getKey().intValue(), result.intValue());
	}
    }

    @Test
    public void testFromStreamString() throws Exception {
	String resultingString = "BadenWuerttemberg";
	AtomProcessor<String> processor = AtomProcessorFactory.getProcessor(Primitive.STRING);

	System.out.printf("String: %s%n", resultingString);
	System.out.printf("Length: %d%n", resultingString.length());

	for (StringEncoding encoding : Arrays.asList(StringEncoding.UTF_8,
		StringEncoding.UTF_16BE, StringEncoding.UTF_16LE)) {
	    System.out.printf("%nEncoding: %s%n", encoding.name());

	    int bits = resultingString.length() * encoding.getWidth();
	    System.out.printf("Computed width: %d bits (%d bytes)%n", bits,
		    bits / 8);

	    byte[] raw = processor.toBytes(resultingString, encoding, bits);
	    System.out.printf("Actual width: %d bytes%n", raw.length);

	    String word = ByteUtil.toHexString(raw);
	    System.out.printf("Hex: %s, length: %d%n", word, word.length());

	    int widthOfLengthField = ByteTestUtil.widthOfLengthField(word);
	    System.out.printf("Preceding length field width: %d bytes%n", widthOfLengthField);
	    System.out.printf("Preceding length field value: %s (hex), %s (bit)%n",
		    ByteTestUtil.lengthPrefix(word, widthOfLengthField),
		    Integer.toBinaryString(bits));

	    byte[] bytes = ByteTestUtil.toPrefixedByteArray(word, widthOfLengthField);
	    System.out.printf("Length + hex: %s%n", ByteUtil.toHexString(bytes));
	    Assert.assertEquals(
		    "Length mismatch after adding preceding length field!",
		    raw.length + widthOfLengthField, bytes.length);

	    Element e = new Element();
	    e.setType(Type.STRING);
	    e.setClassification(encoding.getKey());
	    Length l = new Length();
	    l.setPrecedingLengthFieldSize(widthOfLengthField * 8);
	    e.setLength(l);

	    // BadenWuerttemberg
	    InputStream is = new ByteArrayInputStream(bytes);
	    String result = ep.fromStream(e, is, encoding.getPrimitive());
	    String message = String.format("Error with encoding %s!",
		    encoding.name());
	    Assert.assertEquals(message, resultingString, result);
	}
    }
}