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

import java.util.HashMap;
import java.util.Map;

import org.beyene.protege.core.data.Primitive;

public final class Classifications {

	private Classifications() {
		// private constructor to prevent instantiation
	}

	private static Map<Primitive<?>, Map<String, Encoding<?>>> map = new HashMap<>();
	static {
		map.put(Primitive.INTEGER, new HashMap<String, Encoding<?>>());
		map.put(Primitive.STRING, new HashMap<String, Encoding<?>>());
		map.put(Primitive.FLOAT, new HashMap<String, Encoding<?>>());
		map.put(Primitive.DOUBLE, new HashMap<String, Encoding<?>>());

		for (IntegerEncoding enc : IntegerEncoding.values())
			map.get(enc.getPrimitive()).put(enc.getKey(), enc);

		for (StringEncoding enc : StringEncoding.values())
			map.get(enc.getPrimitive()).put(enc.getKey(), enc);

		for (FloatEncoding enc : FloatEncoding.values())
			map.get(enc.getPrimitive()).put(enc.getKey(), enc);

		for (DoubleEncoding enc : DoubleEncoding.values())
			map.get(enc.getPrimitive()).put(enc.getKey(), enc);
	}

	public static <T> Encoding<T> get(String key, Primitive<T> type) {
		Map<String, Encoding<?>> inner = map.get(type);
		@SuppressWarnings("unchecked")
		Encoding<T> enc = (Encoding<T>) inner.get(key);

		if (enc == null)
			throw new IllegalArgumentException(String.format(
					"No mapping of type %s for key '%s' was found!",
					type.getMappingType(), key));

		return enc;
	}
}