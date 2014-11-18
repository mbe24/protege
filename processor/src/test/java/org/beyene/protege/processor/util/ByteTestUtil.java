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
package org.beyene.protege.processor.util;

public final class ByteTestUtil {

    private ByteTestUtil() {
	// private constructor to prevent instantiation
    }

    // writes number of bytes into length field
    public static byte[] toPrefixedByteArray(String hex, int bytesPrefix) {
	String lengthPrefix = lengthPrefix(hex, bytesPrefix);
	if (lengthPrefix.length() != (2 * bytesPrefix))
	    throw new IllegalArgumentException(String.format("Preceding length field has width of %d bytes!", lengthPrefix.length() / 2));
	return ByteUtil.toByteArray(lengthPrefix + hex);
    }

    // computes necessary width of length field
    public static int widthOfLengthField(String hex) {
	int bits = hex.length() / 2;
	return (int) Math.ceil(Integer.toBinaryString(bits).length() / 8d);
    }

    // creates length field with width 'bytePrefix'
    public static String lengthPrefix(String hex, int bytesPrefix) {
	return String.format("%" + (bytesPrefix * 2) + "s",
		Integer.toHexString(hex.length() / 2)).replace(' ', '0');
    }
}