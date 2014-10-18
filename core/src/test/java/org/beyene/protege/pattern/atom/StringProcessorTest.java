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
package org.beyene.protege.pattern.atom;

import java.util.Arrays;

import org.beyene.protege.core.encoding.StringEncoding;
import org.junit.Assert;
import org.junit.Test;

public class StringProcessorTest {

	AtomProcessor<String> ap = new StringProcessor();

	@Test
	public void testInterpret() throws Exception {
		String element = "oneness";
		byte[] bytes = element.getBytes("UTF-8");
		String copy = ap.interpret(bytes, StringEncoding.UTF_8);
		
		Assert.assertEquals(element, copy);
	}

	@Test
	public void testToBytes() throws Exception {
		String element = "loremipsum";
		byte[] bytes = ap.toBytes(element, StringEncoding.UTF_8, element.length() * StringEncoding.UTF_8.getWidth());
		
		Assert.assertEquals((StringEncoding.UTF_8.getWidth() / 8) * element.length(), bytes.length);
		
		boolean equals = Arrays.equals(element.getBytes("UTF-8"), bytes);
		Assert.assertTrue(equals);
	}
}