package org.beyene.protege.pattern.atom;

import java.nio.ByteBuffer;

import org.beyene.protege.data.Primitive;

public class IntegerProcessor extends AbstractAtomProcessor<Integer> {

	private final int numBytes;
	
	public IntegerProcessor(int length) {
		super(length, Primitive.INTEGER);
		
		// currently only full bytes are supported
		int tmp = length / Byte.SIZE;
		if (length % Byte.SIZE != 0)
			tmp++;
		numBytes = tmp;
	}

	@Override
	public Integer interpret(byte[] bytes) {
		ByteBuffer bb = ByteBuffer.allocate(4);
//	    bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.position(4 - bytes.length);
		bb.put(bytes);
		bb.position(0);
		return bb.asIntBuffer().get();
	}

	@Override
	public byte[] toBytes(Integer element) {
		ByteBuffer bb = ByteBuffer.allocate(4);
//	    bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putInt(element);
		
		bb.position(bb.limit() - numBytes);
		byte[] one = new byte[numBytes];
		bb.get(one);
		return one;
	}
}