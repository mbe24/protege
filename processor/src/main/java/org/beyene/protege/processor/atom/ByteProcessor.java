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

import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.core.encoding.Encoding;

class ByteProcessor implements AtomProcessor<Byte[]> {

    @Override
    public Primitive<Byte[]> getPrimitive() {
	return Primitive.BYTES;
    }

    @Override
    public Byte[] interpret(byte[] bytes, Encoding<Byte[]> encoding) {
	return box(bytes);
    }

    @Override
    public byte[] toBytes(Byte[] element, Encoding<Byte[]> encoding, int bits) {
	return unbox(element);
    }

    private static Byte[] box(byte[] bytes) {
	Byte[] boxed = new Byte[bytes.length];
	for (int i = 0; i < boxed.length; i++) {
	    boxed[i] = bytes[i];
	}
	return boxed;
    }

    private static byte[] unbox(Byte[] bytes) {
	byte[] boxed = new byte[bytes.length];
	for (int i = 0; i < boxed.length; i++) {
	    boxed[i] = bytes[i];
	}
	return boxed;
    }
}