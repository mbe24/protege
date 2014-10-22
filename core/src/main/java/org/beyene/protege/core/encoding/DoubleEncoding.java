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
package org.beyene.protege.core.encoding;

import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import org.beyene.protege.data.Primitive;

@XmlType(name = "double-encoding")
public enum DoubleEncoding implements Encoding<Double> {
	
	@XmlEnumValue("ieee754-double")
	IEEE_754_DOUBLE("ieee754-double", 64);

	private final String key;
	private final int bits;
	
	private DoubleEncoding(String key, int bits) {
		this.key = key;
		this.bits = bits;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public int getWidth() {
		return bits;
	}

	@Override
	public Primitive<Double> getPrimitive() {
		return Primitive.DOUBLE;
	}
}