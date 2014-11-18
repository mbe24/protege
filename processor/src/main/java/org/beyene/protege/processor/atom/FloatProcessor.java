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
import org.beyene.protege.core.encoding.FloatEncoding;
import org.beyene.protege.core.data.Primitive;

class FloatProcessor implements AtomProcessor<Float> {

    private static final int MAX_BYTES = 4;

    @Override
    public Primitive<Float> getPrimitive() {
	return Primitive.FLOAT;
    }

    @Override
    public Float interpret(byte[] bytes, Encoding<Float> encoding) {
	ByteBuffer bb = ByteBuffer.allocate(MAX_BYTES);
	// bb.order(ByteOrder.LITTLE_ENDIAN);
	// bb.position(MAX_BYTES - bytes.length);

	if (encoding != FloatEncoding.IEEE_754_SINGLE
		&& bytes.length != MAX_BYTES)
	    throw new IllegalArgumentException(
		    "Only IEEE 754 single precision encoding supported for type float!");

	bb.put(bytes);
	bb.position(0);

	return Float.intBitsToFloat(bb.asIntBuffer().get());
    }

    @Override
    public byte[] toBytes(Float element, Encoding<Float> encoding, int bytes) {
	ByteBuffer bb = ByteBuffer.allocate(MAX_BYTES);
	// bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putInt(Float.floatToIntBits(element));

	if (encoding != FloatEncoding.IEEE_754_SINGLE)
	    throw new IllegalArgumentException(
		    "Only IEEE 754 single precision encoding supported for type float!");

	bb.position(bb.limit() - MAX_BYTES);
	byte[] encoded = new byte[MAX_BYTES];
	bb.get(encoded);

	assert interpret(encoded, encoding).equals(element) : "Encoded bytes are invalid!";

	return encoded;
    }
}