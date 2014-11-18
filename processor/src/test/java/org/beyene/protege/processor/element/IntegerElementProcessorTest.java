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
import java.util.HashMap;
import java.util.Map;

import org.beyene.protege.core.Element;
import org.beyene.protege.core.Length;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.encoding.Encoding;
import org.beyene.protege.core.encoding.IntegerEncoding;
import org.beyene.protege.processor.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

public class IntegerElementProcessorTest {

    private static final IntegerElementProcessor ep = IntegerElementProcessor.INSTANCE;

    @Test
    public void testRead() throws Exception {
	Map<IntegerEncoding, Long> results = new HashMap<>();
	results.put(IntegerEncoding.TWOS_COMPLEMENT, -1L);
	results.put(IntegerEncoding.UNSIGNED, 65535L);

	for (Encoding<Long> encoding : Arrays.asList(
		IntegerEncoding.TWOS_COMPLEMENT, IntegerEncoding.UNSIGNED)) {
	    Element e = new Element();
	    e.setType(Type.INTEGER);
	    e.setClassification(encoding.getKey());
	    Length l = new Length();
	    l.setQuantity(2);
	    e.setLength(l);

	    // 65535 unsigned or -1 signed
	    InputStream is = new ByteArrayInputStream(ByteUtil.toByteArray("FFFF"));

	    Long result = ep.read(e, Channels.newChannel(is));
	    Assert.assertEquals(results.get(encoding).longValue(), result.longValue());
	}
    }

    @Test
    public void testWrite() throws Exception {
	Map<IntegerEncoding, Long> results = new HashMap<>();
	results.put(IntegerEncoding.TWOS_COMPLEMENT, -1L);
	results.put(IntegerEncoding.UNSIGNED, 65535L);

	for (Encoding<Long> encoding : Arrays.asList(
		IntegerEncoding.TWOS_COMPLEMENT, IntegerEncoding.UNSIGNED)) {
	    Element e = new Element();
	    e.setType(Type.INTEGER);
	    e.setClassification(encoding.getKey());
	    Length l = new Length();
	    l.setQuantity(2);
	    e.setLength(l);

	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    int written = ep.write(results.get(encoding), e, Channels.newChannel(os));
	    Assert.assertEquals(2, written);
	    
	    // 65535 unsigned or -1 signed
	    InputStream is = new ByteArrayInputStream(os.toByteArray());
	    Long result = ep.read(e, Channels.newChannel(is));
	    Assert.assertEquals(results.get(encoding).longValue(), result.longValue());
	}
    }
}