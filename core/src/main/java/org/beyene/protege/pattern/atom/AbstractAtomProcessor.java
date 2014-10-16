package org.beyene.protege.pattern.atom;

import org.beyene.protege.data.Primitive;

abstract class AbstractAtomProcessor<T> implements AtomProcessor<T> {

	protected final int length;
	protected final Primitive<T> primitive;
	
	public AbstractAtomProcessor(int length, Primitive<T> primitive) {
		this.length = length;
		this.primitive = primitive;
	}

	@Override
	public int getLength() {
		return length;
	}
	
	@Override
	public Primitive<T> getPrimitive() {
		return primitive;
	}

	@Override
	public abstract T interpret(byte[] bytes);

	@Override
	public abstract byte[] toBytes(T element);
}