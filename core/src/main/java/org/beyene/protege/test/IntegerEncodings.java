package org.beyene.protege.test;

import java.nio.ByteBuffer;

import javax.xml.bind.DatatypeConverter;

public class IntegerEncodings {

	public static void main(String[] args) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(255);
		
		System.out.println(toHexString(bb.array()));
		
		System.out.printf("remaining = '%d', limit = '%d', capacity = '%d'%n", bb.remaining(), bb.limit(), bb.capacity());
		int size = 1;
		bb.position(bb.limit() - size);
		byte[] one = new byte[size];
		bb.get(one);
		System.out.println(toHexString(one));
		
		bb = ByteBuffer.allocate(4);
		bb.position(4 - one.length);
		bb.put(one);
		System.out.println(toHexString(bb.array()));
		bb.position(0);
		System.out.println(bb.asIntBuffer().get());
	}
	
	private static String toHexString(byte[] array) {
		return DatatypeConverter.printHexBinary(array);
	}

	private static byte[] toByteArray(String s) {
		return DatatypeConverter.parseHexBinary(s);
	}
}