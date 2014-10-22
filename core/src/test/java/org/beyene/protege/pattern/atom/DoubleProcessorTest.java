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

import org.beyene.protege.core.encoding.DoubleEncoding;
import org.beyene.protege.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

public class DoubleProcessorTest {

	private final AtomProcessor<Double> ap = new DoubleProcessor();

	private final double d1 = 64.5;
	private final String s1 = "4050200000000000";
	private final String s1n = "C050200000000000";

	private final double d2 = 32.125;
	private final String s2 = "4040100000000000";
	private final String s2n = "C040100000000000";

	@Test
	public void testInterpretPositive() throws Exception {
		String hex;
		byte[] bytes;
		double result;

		DoubleEncoding enc = DoubleEncoding.IEEE_754_DOUBLE;
		
		hex = s1;
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, enc);
		Assert.assertEquals(d1, result, 1e-6);
		System.out.printf("%s = %f%n", hex, result);

		hex = s2;
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, enc);
		Assert.assertEquals(d2, result, 1e-6);
		System.out.printf("%s = %f%n", hex, result);
	}

	@Test
	public void testToBytesPositive() throws Exception {
		int bits;
		String hex;

		DoubleEncoding enc = DoubleEncoding.IEEE_754_DOUBLE;
		bits = enc.getWidth();

		byte[] b8 = ap.toBytes(d1, enc, bits);
		hex = ByteUtil.toHexString(b8);
		System.out.println(hex);
		Assert.assertEquals(2 * bits / 8, hex.length());
		Assert.assertTrue(s1.equals(hex));

		byte[] b7 = ap.toBytes(d2, enc, bits);
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

		DoubleEncoding enc = DoubleEncoding.IEEE_754_DOUBLE;
		
		hex = s1n;
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, enc);
		Assert.assertEquals(-d1, result, 1e-6);
		System.out.printf("%s = %f%n", hex, result);

		hex = s2n;
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, enc);
		Assert.assertEquals(-d2, result, 1e-6);
		System.out.printf("%s = %f%n", hex, result);
	}

	@Test
	public void testToBytesNegative() throws Exception {
		int bits;
		String hex;

		DoubleEncoding enc = DoubleEncoding.IEEE_754_DOUBLE;
		bits = enc.getWidth();

		byte[] b8 = ap.toBytes(-d1, enc, bits);
		hex = ByteUtil.toHexString(b8);
		System.out.println(hex);
		Assert.assertEquals(2 * bits / 8, hex.length());
		Assert.assertTrue(s1n.equals(hex));

		byte[] b7 = ap.toBytes(-d2, enc, bits);
		hex = ByteUtil.toHexString(b7);
		System.out.println(hex);
		Assert.assertEquals(2 * bits / 8, hex.length());
		Assert.assertTrue(s2n.equals(hex));
	}
}