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
package org.beyene.protege.test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.DatatypeConverter;

public class StringEncodings {

	public static void main(String[] args) throws UnsupportedEncodingException {

		String s = "0123456789"; // 10 signs
		Map<String, Byte[]> map = new LinkedHashMap<String, Byte[]>();
		map.put("standard", box(s.getBytes()));
		map.put("UTF-8", box(s.getBytes("UTF-8")));
		map.put("UTF-16", box(s.getBytes("UTF-16")));
		map.put("UTF-16BE", box(s.getBytes("UTF-16BE")));
		map.put("UTF-16LE", box(s.getBytes("UTF-16LE")));

		System.out.printf("String s = '%s', #characters = '%d'.%n", s,
				s.length());
		for (Entry<String, Byte[]> entry : map.entrySet())
			System.out.printf("Encoding = '%s', bytes = '%d', data ='%s'%n",
					entry.getKey(), entry.getValue().length,
					toHexString(unbox(entry.getValue())));

		Charset charset = Charset.forName("UTF-8");
		String decoded = charset.decode(ByteBuffer.wrap(toByteArray("30313233343536373839"))).toString();
		System.out.printf("decoded = '%s'%n", decoded);
		
		ByteBuffer bb = charset.encode(s);
		
		
		byte[] bytes = new byte[bb.remaining()];
		bb.get(bytes);
		System.out.println(toHexString(bytes));
		
		System.out.println(toHexString(s.getBytes(charset)));
	}

	private static byte[] unbox(Byte[] bytes) {
		byte[] boxed = new byte[bytes.length];
		for (int i = 0; i < boxed.length; i++) {
			boxed[i] = bytes[i];
		}
		return boxed;
	}

	private static Byte[] box(byte[] bytes) {
		Byte[] boxed = new Byte[bytes.length];
		for (int i = 0; i < boxed.length; i++) {
			boxed[i] = bytes[i];
		}
		return boxed;
	}

	private static String toHexString(byte[] array) {
		return DatatypeConverter.printHexBinary(array);
	}

	private static byte[] toByteArray(String s) {
		return DatatypeConverter.parseHexBinary(s);
	}
}