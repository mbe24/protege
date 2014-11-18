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

import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.core.encoding.DoubleEncoding;
import org.beyene.protege.core.encoding.Encoding;

class DoubleProcessor implements AtomProcessor<Double> {

    private static final int MAX_BYTES = 8;

    @Override
    public Primitive<Double> getPrimitive() {
	return Primitive.DOUBLE;
    }

    @Override
    public Double interpret(byte[] bytes, Encoding<Double> encoding) {
	ByteBuffer bb = ByteBuffer.allocate(MAX_BYTES);
	// bb.order(ByteOrder.LITTLE_ENDIAN);
	// bb.position(MAX_BYTES - bytes.length);

	if (encoding != DoubleEncoding.IEEE_754_DOUBLE
		&& bytes.length != MAX_BYTES)
	    throw new IllegalArgumentException(
		    "Only IEEE 754 double precision encoding supported for type double!");

	bb.put(bytes);
	bb.position(0);
	return Double.longBitsToDouble(bb.asLongBuffer().get());
    }

    @Override
    public byte[] toBytes(Double element, Encoding<Double> encoding, int bytes) {
	ByteBuffer bb = ByteBuffer.allocate(MAX_BYTES);
	// bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putLong(Double.doubleToLongBits(element));

	if (encoding != DoubleEncoding.IEEE_754_DOUBLE)
	    throw new IllegalArgumentException(
		    "Only IEEE 754 double precision encoding supported for type double!");

	bb.position(bb.limit() - MAX_BYTES);
	byte[] encoded = new byte[MAX_BYTES];
	bb.get(encoded);

	assert interpret(encoded, encoding).equals(element) : "Encoded bytes are invalid!";

	return encoded;
    }
}