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

import org.beyene.protege.core.encoding.Encoding;
import org.beyene.protege.data.Primitive;

public class FloatProcessor extends AbstractAtomProcessor<Float> {

	public FloatProcessor(int length) {
		super(length, Primitive.FLOAT);
	}

	@Override
	public Float interpret(byte[] bytes, Encoding<Float> encoding) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] toBytes(Float element, Encoding<Float> encoding) {
		// TODO Auto-generated method stub
		return null;
	}
}