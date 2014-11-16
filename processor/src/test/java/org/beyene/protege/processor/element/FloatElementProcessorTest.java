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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.beyene.protege.core.Element;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.encoding.FloatEncoding;
import org.beyene.protege.processor.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

public class FloatElementProcessorTest {

    private static final FloatElementProcessor ep = FloatElementProcessor.INSTANCE;

    @Test
        public void testRead() throws Exception {
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
    	    Float result = ep.read(e, Channels.newChannel(is));
    	    Assert.assertEquals(entry.getKey().intValue(), result.intValue());
    	}
        }

    @Test
        public void testWrite() throws Exception {
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
    	    Float val = entry.getKey();
    	    ByteArrayOutputStream os = new ByteArrayOutputStream();
    	    int written = ep.write(val, e, Channels.newChannel(os));
    	    Assert.assertEquals(FloatEncoding.IEEE_754_SINGLE.getWidth() / 8, written);
    	    
    	    InputStream is = new ByteArrayInputStream(os.toByteArray());
    	    Float result = ep.read(e, Channels.newChannel(is));
    	    Assert.assertEquals(val.intValue(), result.intValue());
    	}
        }
}