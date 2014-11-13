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
package org.beyene.protege.processor.atom;

import org.beyene.protege.processor.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

public class ByteProcessorTest {

    private final AtomProcessor<Byte[]> ap = new ByteProcessor();
    
    @Test
    public void testInterpret() throws Exception {
	byte[] bytes = ByteUtil.toByteArray("DEADBEEF");
	Byte[] interpreted = ap.interpret(bytes, null);
	Assert.assertArrayEquals(ByteUtil.box(bytes), interpreted);
    }

    @Test
    public void testToBytes() throws Exception {
	Byte[] data = ByteUtil.box(ByteUtil.toByteArray("DEADBEEF"));
	byte[] bytes = ap.toBytes(data, null, -1);
	Assert.assertArrayEquals(data, ByteUtil.box(bytes));
    }
}