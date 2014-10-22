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

import java.nio.ByteBuffer;

import org.beyene.protege.core.encoding.Encoding;
import org.beyene.protege.core.encoding.FloatEncoding;
import org.beyene.protege.data.Primitive;

public class FloatProcessor extends AbstractAtomProcessor<Float> {

	private static final int MAX_BYTES = 4;

	public FloatProcessor() {
		super(Primitive.FLOAT);
	}

	@Override
	public Float interpret(byte[] bytes, Encoding<Float> encoding) {
		ByteBuffer bb = ByteBuffer.allocate(MAX_BYTES);
		// bb.order(ByteOrder.LITTLE_ENDIAN);
		// bb.position(MAX_BYTES - bytes.length);

		if (encoding != FloatEncoding.IEEE_754_SINGLE && bytes.length != MAX_BYTES)
			throw new IllegalArgumentException("Only IEEE 754 single precision encoding supported for type float!");
		
		bb.put(bytes);
		bb.position(0);
		
		return Float.intBitsToFloat(bb.asIntBuffer().get());
	}

	@Override
	public byte[] toBytes(Float element, Encoding<Float> encoding, int bits) {
		ByteBuffer bb = ByteBuffer.allocate(MAX_BYTES);
		// bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putInt(Float.floatToIntBits(element));

		if (encoding != FloatEncoding.IEEE_754_SINGLE)
			throw new IllegalArgumentException("Only IEEE 754 single precision encoding supported for type float!");
		
		int bytes = 4;

		bb.position(bb.limit() - bytes);
		byte[] encoded = new byte[bytes];
		bb.get(encoded);
		return encoded;
	}
}