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

import org.beyene.protege.core.encoding.FloatEncoding;
import org.beyene.protege.processor.atom.AtomProcessor;
import org.beyene.protege.processor.atom.FloatProcessor;
import org.beyene.protege.processor.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

public class FloatProcessorTest {

	private final AtomProcessor<Float> ap = new FloatProcessor();

	private final float f1 = 64.5f;
	private final String s1 = "42810000";
	private final String s1n = "C2810000";

	private final float f2 = 32.125f;
	private final String s2 = "42008000";
	private final String s2n = "C2008000";

	@Test
	public void testInterpretPositive() throws Exception {
		String hex;
		byte[] bytes;
		double result;

		FloatEncoding enc = FloatEncoding.IEEE_754_SINGLE;

		hex = s1;
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, enc);
		Assert.assertEquals(f1, result, 1e-6);
		System.out.printf("%s = %f%n", hex, result);

		hex = s2;
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, enc);
		Assert.assertEquals(f2, result, 1e-6);
		System.out.printf("%s = %f%n", hex, result);
	}

	@Test
	public void testToBytesPositive() throws Exception {
		int bits;
		String hex;

		FloatEncoding enc = FloatEncoding.IEEE_754_SINGLE;
		bits = enc.getWidth();

		byte[] b8 = ap.toBytes(f1, enc, bits);
		hex = ByteUtil.toHexString(b8);
		System.out.println(hex);
		Assert.assertEquals(2 * bits / 8, hex.length());
		Assert.assertTrue(s1.equals(hex));

		byte[] b7 = ap.toBytes(f2, enc, bits);
		hex = ByteUtil.toHexString(b7);
		System.out.println(hex);
		Assert.assertEquals(2 * bits / 8, hex.length());
		Assert.assertTrue(s2.equals(hex));
	}

	@Test
	public void testInterpretNegative() throws Exception {
		String hex;
		byte[] bytes;
		double result;

		FloatEncoding enc = FloatEncoding.IEEE_754_SINGLE;

		hex = s1n;
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, enc);
		Assert.assertEquals(-f1, result, 1e-6);
		System.out.printf("%s = %f%n", hex, result);

		hex = s2n;
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, enc);
		Assert.assertEquals(-f2, result, 1e-6);
		System.out.printf("%s = %f%n", hex, result);
	}

	@Test
	public void testToBytesNegative() throws Exception {
		int bits;
		String hex;

		FloatEncoding enc = FloatEncoding.IEEE_754_SINGLE;
		bits = enc.getWidth();

		byte[] b8 = ap.toBytes(-f1, enc, bits);
		hex = ByteUtil.toHexString(b8);
		System.out.println(hex);
		Assert.assertEquals(2 * bits / 8, hex.length());
		Assert.assertTrue(s1n.equals(hex));

		byte[] b7 = ap.toBytes(-f2, enc, bits);
		hex = ByteUtil.toHexString(b7);
		System.out.println(hex);
		Assert.assertEquals(2 * bits / 8, hex.length());
		Assert.assertTrue(s2n.equals(hex));
	}
}