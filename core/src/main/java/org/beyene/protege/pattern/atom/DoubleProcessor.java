package org.beyene.protege.pattern.atom;

import java.nio.ByteBuffer;

import org.beyene.protege.core.encoding.DoubleEncoding;
import org.beyene.protege.core.encoding.Encoding;
import org.beyene.protege.data.Primitive;

public class DoubleProcessor implements AtomProcessor<Double> {

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

		if (encoding != DoubleEncoding.IEEE_754_DOUBLE && bytes.length != MAX_BYTES)
			throw new IllegalArgumentException("Only IEEE 754 double precision encoding supported for type double!");
		
		bb.put(bytes);
		bb.position(0);
		return Double.longBitsToDouble(bb.asLongBuffer().get());
	}

	@Override
	public byte[] toBytes(Double element, Encoding<Double> encoding, int bits) {
		ByteBuffer bb = ByteBuffer.allocate(MAX_BYTES);
		// bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putLong(Double.doubleToLongBits(element));
		
		if (encoding != DoubleEncoding.IEEE_754_DOUBLE)
			throw new IllegalArgumentException("Only IEEE 754 double precision encoding supported for type double!");
		
		int bytes = 8;

		bb.position(bb.limit() - bytes);
		byte[] encoded = new byte[bytes];
		bb.get(encoded);
		return encoded;
	}

}
