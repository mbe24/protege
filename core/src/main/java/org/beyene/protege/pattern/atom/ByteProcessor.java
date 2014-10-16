package org.beyene.protege.pattern.atom;

import org.beyene.protege.data.Primitive;

public class ByteProcessor extends AbstractAtomProcessor<Byte[]> {

	public ByteProcessor(int length) {
		super(length, Primitive.BYTES);
	}

	@Override
	public Byte[] interpret(byte[] bytes) {
		return box(bytes);
	}

	@Override
	public byte[] toBytes(Byte[] element) {
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