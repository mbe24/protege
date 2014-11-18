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

import org.beyene.protege.core.encoding.IntegerEncoding;
import org.beyene.protege.processor.atom.AtomProcessor;
import org.beyene.protege.processor.atom.IntegerProcessor;
import org.beyene.protege.processor.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

public class IntegerProcessorTest {

	private final AtomProcessor<Long> ap = new IntegerProcessor();
	private final String regExNegative = "(:?[F][F]){0,7}C0";
	private final String regExPositive = "(:?[0][0]){0,7}40";

	@Test
	public void testToBytesNegative() throws Exception {
		long n = -64;
		int bytes;
		String hex;

		bytes = 8;
		byte[] b8 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b8);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExNegative));

		bytes = 7;
		byte[] b7 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b7);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExNegative));

		bytes = 6;
		byte[] b6 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b6);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExNegative));

		bytes = 5;
		byte[] b5 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b5);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExNegative));

		bytes = 4;
		byte[] b4 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b4);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExNegative));

		bytes = 3;
		byte[] b3 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b3);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExNegative));

		bytes = 2;
		byte[] b2 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b2);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExNegative));

		bytes = 1;
		byte[] b1 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b1);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExNegative));
	}

	@Test
	public void testToBytesPositive() throws Exception {
		long n = 64;
		int bytes;
		String hex;

		bytes = 8;
		byte[] b8 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b8);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExPositive));

		bytes = 7;
		byte[] b7 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b7);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExPositive));

		bytes = 6;
		byte[] b6 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b6);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExPositive));

		bytes = 5;
		byte[] b5 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b5);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExPositive));

		bytes = 4;
		byte[] b4 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b4);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExPositive));

		bytes = 3;
		byte[] b3 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b3);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExPositive));

		bytes = 2;
		byte[] b2 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b2);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExPositive));

		bytes = 1;
		byte[] b1 = ap.toBytes(n, IntegerEncoding.TWOS_COMPLEMENT, bytes);
		hex = ByteUtil.toHexString(b1);
		System.out.println(hex);
		Assert.assertEquals(2 * bytes, hex.length());
		Assert.assertTrue(hex.matches(regExPositive));
	}

	@Test
	public void testInterpretNegative() throws Exception {
		long n = -64;
		int bits;
		String hex;
		byte[] bytes;
		long result;

		bits = 64;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "FF") + "C0";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 56;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "FF") + "C0";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 48;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "FF") + "C0";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 40;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "FF") + "C0";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 32;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "FF") + "C0";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 24;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "FF") + "C0";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 16;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "FF") + "C0";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 8;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "FF") + "C0";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);
	}

	@Test
	public void testInterpretPositive() throws Exception {
		long n = 64;
		int bits;
		String hex;
		byte[] bytes;
		long result;

		bits = 64;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "00") + "40";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 56;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "00") + "40";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 48;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "00") + "40";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 40;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "00") + "40";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 32;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "00") + "40";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 24;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "00") + "40";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 16;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "00") + "40";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);

		bits = 8;
		hex = new String(new char[(bits / 8) - 1]).replaceAll("\0", "00") + "40";
		System.out.println(hex);
		bytes = ByteUtil.toByteArray(hex);
		result = ap.interpret(bytes, IntegerEncoding.TWOS_COMPLEMENT);
		Assert.assertEquals(n, result);
	}
}