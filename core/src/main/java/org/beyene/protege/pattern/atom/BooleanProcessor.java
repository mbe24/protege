package org.beyene.protege.pattern.atom;

import org.beyene.protege.data.Primitive;

public class BooleanProcessor extends AbstractAtomProcessor<Boolean> {

	// mapping needed for conversion
	
	public BooleanProcessor(int length) {
		super(length, Primitive.BOOLEAN);
	}

	@Override
	public Boolean interpret(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] toBytes(Boolean element) {
		// TODO Auto-generated method stub
		return null;
	}
}