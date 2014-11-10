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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.beyene.protege.core.Element;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.encoding.DoubleEncoding;
import org.beyene.protege.processor.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

public class DoubleElementProcessorTest {

    private static final DoubleElementProcessor ep = DoubleElementProcessor.INSTANCE;

    @Test
    public void testFromStream() throws Exception {
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
	    Double result = ep.fromStream(e, is);
	    Assert.assertEquals(entry.getKey().longValue(), result.longValue());
	}
    }

    @Test
    public void testToStream() throws Exception {
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
	    Double val = entry.getKey();
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    int written = ep.toStream(val, e, os);
	    Assert.assertEquals(DoubleEncoding.IEEE_754_DOUBLE.getWidth() / 8, written);
	    
	    InputStream is = new ByteArrayInputStream(os.toByteArray());
	    Double result = ep.fromStream(e, is);
	    Assert.assertEquals(val.longValue(), result.longValue());
	}
    }
}