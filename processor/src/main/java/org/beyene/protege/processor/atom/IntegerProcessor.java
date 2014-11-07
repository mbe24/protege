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

import java.nio.ByteBuffer;

import org.beyene.protege.core.encoding.Encoding;
import org.beyene.protege.core.data.Primitive;

class IntegerProcessor implements AtomProcessor<Long> {

	private static final int MAX_BYTES = 8;

	@Override
	public Primitive<Long> getPrimitive() {
		return Primitive.INTEGER;
	}

	@Override
	public Long interpret(byte[] bytes, Encoding<Long> encoding) {
		ByteBuffer bb = ByteBuffer.allocate(MAX_BYTES);
		// bb.order(ByteOrder.LITTLE_ENDIAN);
		// bb.position(MAX_BYTES - bytes.length);

		// if negative value is represented, 0xFF is appended to fill 64 bits
		if (bytes[0] < (byte) 0x00)
			for (int i = 0; i < MAX_BYTES - bytes.length; i++)
				bb.put((byte) 0xFF);
		else
			for (int i = 0; i < MAX_BYTES - bytes.length; i++)
				bb.put((byte) 0x00);

		bb.put(bytes);
		bb.position(0);
		return bb.asLongBuffer().get();
	}

	@Override
	public byte[] toBytes(Long element, Encoding<Long> encoding, int bits) {
		ByteBuffer bb = ByteBuffer.allocate(MAX_BYTES);
		// bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putLong(element);

		int bytes = bits / Byte.SIZE;
		if (bits % Byte.SIZE != 0)
			bytes++;

		bb.position(bb.limit() - bytes);
		byte[] encoded = new byte[bytes];
		bb.get(encoded);
		return encoded;
	}
}