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
package org.beyene.protege.pattern.atom;

import java.nio.ByteBuffer;

import org.beyene.protege.core.encoding.Encoding;
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
	public Integer interpret(byte[] bytes, Encoding<Integer> encoding) {
		ByteBuffer bb = ByteBuffer.allocate(4);
//	    bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.position(4 - bytes.length);
		bb.put(bytes);
		bb.position(0);
		return bb.asIntBuffer().get();
	}

	@Override
	public byte[] toBytes(Integer element, Encoding<Integer> encoding) {
		ByteBuffer bb = ByteBuffer.allocate(4);
//	    bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putInt(element);
		
		bb.position(bb.limit() - numBytes);
		byte[] one = new byte[numBytes];
		bb.get(one);
		return one;
	}
}