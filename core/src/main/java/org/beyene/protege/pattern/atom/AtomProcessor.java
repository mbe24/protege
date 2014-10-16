package org.beyene.protege.pattern.atom;

import org.beyene.protege.data.Primitive;


public interface AtomProcessor<T> {

	public int getLength();
	
	public Primitive<T> getPrimitive();
	
	public T interpret(byte[] bytes);
	
	public byte[] toBytes(T element);
}