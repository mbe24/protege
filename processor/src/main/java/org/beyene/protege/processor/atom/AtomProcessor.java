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
package org.beyene.protege.processor.atom;

import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.core.encoding.Encoding;

public interface AtomProcessor<T> {

	public Primitive<T> getPrimitive();

	public T interpret(byte[] bytes, Encoding<T> encoding);

	public byte[] toBytes(T element, Encoding<T> encoding, int bits);
}