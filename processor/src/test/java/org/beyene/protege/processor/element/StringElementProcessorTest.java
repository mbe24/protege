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
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.Arrays;

import org.beyene.protege.core.Element;
import org.beyene.protege.core.Length;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.core.encoding.StringEncoding;
import org.beyene.protege.processor.atom.AtomProcessor;
import org.beyene.protege.processor.atom.AtomProcessorFactory;
import org.beyene.protege.processor.util.ByteTestUtil;
import org.beyene.protege.processor.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

public class StringElementProcessorTest {

    private static final StringElementProcessor ep = StringElementProcessor.INSTANCE;

    @Test
    public void testRead() throws Exception {
	String resultingString = "BadenWuerttemberg";
	AtomProcessor<String> processor = AtomProcessorFactory.getProcessor(Primitive.STRING);

	System.out.printf("%nDecoding string: %s%n", resultingString);
	System.out.printf("Length: %d%n", resultingString.length());

	for (StringEncoding encoding : Arrays.asList(StringEncoding.UTF_8,
		StringEncoding.UTF_16BE, StringEncoding.UTF_16LE)) {
	    System.out.printf("%nEncoding: %s%n", encoding.name());

	    int length = resultingString.length() * encoding.getWidth() / 8;
	    System.out.printf("Computed width: %d bits (%d bytes)%n", length * 8, length);

	    byte[] raw = processor.toBytes(resultingString, encoding, -1);
	    System.out.printf("Actual width: %d bytes%n", raw.length);

	    String word = ByteUtil.toHexString(raw);
	    System.out.printf("Hex: %s, length: %d%n", word, word.length());

	    int widthOfLengthField = ByteTestUtil.widthOfLengthField(word);
	    System.out.printf("Preceding length field width: %d bytes%n", widthOfLengthField);
	    System.out.printf(
		    "Preceding length field value: %s (hex), %s (bit)%n",
		    ByteTestUtil.lengthPrefix(word, widthOfLengthField),
		    Integer.toBinaryString(length));

	    byte[] bytes = ByteTestUtil.toPrefixedByteArray(word, widthOfLengthField);
	    System.out.printf("Length + hex: %s%n", ByteUtil.toHexString(bytes));
	    Assert.assertEquals("Length mismatch after adding preceding length field!",
		    raw.length + widthOfLengthField, bytes.length);

	    Element e = new Element();
	    e.setType(Type.STRING);
	    e.setClassification(encoding.getKey());
	    Length l = new Length();
	    l.setPrecedingLengthFieldSize(widthOfLengthField);
	    e.setLength(l);

	    // BadenWuerttemberg
	    InputStream is = new ByteArrayInputStream(bytes);
	    String result = ep.read(e, Channels.newChannel(is));
	    String message = String.format("Error with encoding %s!", encoding.name());
	    Assert.assertEquals(message, resultingString, result);
	}
    }

    @Test
    public void testWrite() throws Exception {
	String source = "BadenWuerttemberg";
	AtomProcessor<String> processor = AtomProcessorFactory.getProcessor(Primitive.STRING);

	System.out.printf("%nEncoding string: %s%n", source);
	System.out.printf("Length: %d%n", source.length());
	for (StringEncoding encoding : Arrays.asList(StringEncoding.UTF_8,
		StringEncoding.UTF_16BE, StringEncoding.UTF_16LE)) {

	    System.out.printf("%nEncoding: %s%n", encoding.name());

	    int length = source.length() * encoding.getWidth() / 8;
	    System.out.printf("Computed width: %d bits (%d bytes)%n", length * 8, length);

	    byte[] raw = processor.toBytes(source, encoding, -1);
	    System.out.printf("Actual width: %d bytes%n", raw.length);

	    String word = ByteUtil.toHexString(raw);
	    System.out.printf("String Hex: %s, length: %d%n", word, word.length());

	    int widthOfLengthField = ByteTestUtil.widthOfLengthField(word);
	    System.out.printf("Preceding length field width: %d bytes%n", widthOfLengthField);

	    Element e = new Element();
	    e.setType(Type.STRING);
	    e.setClassification(encoding.getKey());
	    Length l = new Length();
	    l.setPrecedingLengthFieldSize(widthOfLengthField);
	    e.setLength(l);

	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    ep.write(source, e, Channels.newChannel(os));

	    byte[] bytes = os.toByteArray();
	    word = ByteUtil.toHexString(bytes);
	    System.out.printf("Length + hex: %s, length: %d%n", word, word.length());

	    InputStream is = new ByteArrayInputStream(bytes);
	    String result = ep.read(e, Channels.newChannel(is));

	    String message = String.format("Error with encoding %s!", encoding.name());
	    Assert.assertEquals(message, source, result);
	}
    }
}