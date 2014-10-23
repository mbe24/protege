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
package org.beyene.protege.example.test;

import java.nio.ByteBuffer;

import javax.xml.bind.DatatypeConverter;

public class IntegerEncodings {

	public static void main(String[] args) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(255);
		
		System.out.println(toHexString(bb.array()));
		
		System.out.printf("remaining = '%d', limit = '%d', capacity = '%d'%n", bb.remaining(), bb.limit(), bb.capacity());
		int size = 1;
		bb.position(bb.limit() - size);
		byte[] one = new byte[size];
		bb.get(one);
		System.out.println(toHexString(one));
		
		bb = ByteBuffer.allocate(4);
		bb.position(4 - one.length);
		bb.put(one);
		System.out.println(toHexString(bb.array()));
		bb.position(0);
		System.out.println(bb.asIntBuffer().get());
	}
	
	private static String toHexString(byte[] array) {
		return DatatypeConverter.printHexBinary(array);
	}

	@SuppressWarnings("unused")
	private static byte[] toByteArray(String s) {
		return DatatypeConverter.parseHexBinary(s);
	}
}