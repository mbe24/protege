package org.beyene.protege.pattern.atom;

import org.beyene.protege.data.Primitive;

public class DoubleProcessor extends AbstractAtomProcessor<Double> {

	public DoubleProcessor(int length) {
		super(length, Primitive.DOUBLE);
	}

	@Override
	public Double interpret(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] toBytes(Double element) {
		// TODO Auto-generated method stub
		return null;
	}
}