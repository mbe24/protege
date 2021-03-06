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

import java.nio.charset.Charset;

import org.beyene.protege.core.encoding.Encoding;
import org.beyene.protege.core.data.Primitive;

class StringProcessor implements AtomProcessor<String> {

    @Override
    public Primitive<String> getPrimitive() {
	return Primitive.STRING;
    }

    @Override
    public String interpret(byte[] bytes, Encoding<String> encoding) {
	Charset charset = Charset.forName(encoding.getKey());
	return new String(bytes, charset);
    }

    @Override
    public byte[] toBytes(String element, Encoding<String> encoding, int bytes) {
	Charset charset = Charset.forName(encoding.getKey());
	byte[] raw = element.getBytes(charset);

	assert interpret(raw, encoding).equals(element) : "Encoded bytes are invalid!";
	// TODO only return specified bytes
	return raw;
    }
}