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
package org.beyene.protege.core.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.beyene.protege.core.Type;

public final class Primitive<T> {

	public static final Primitive<Boolean> BOOLEAN = new Primitive<>(Boolean.class, Type.BOOLEAN);
	public static final Primitive<Byte[]> BYTES = new Primitive<>(Byte[].class, Type.BYTE);
	public static final Primitive<Long> INTEGER = new Primitive<>(Long.class, Type.INTEGER);
	public static final Primitive<String> STRING = new Primitive<>(String.class, Type.STRING);
	public static final Primitive<Float> FLOAT = new Primitive<>(Float.class, Type.FLOAT);
	public static final Primitive<Double> DOUBLE = new Primitive<>(Double.class, Type.FLOAT);

	private static final Map<Type, Primitive<?>> types = new HashMap<>();
	static {
		for (Primitive<?> primitive : Arrays.asList(BOOLEAN, BYTES, INTEGER, STRING, FLOAT, DOUBLE))
			types.put(primitive.getMappingType(), primitive);
	}

	private final Class<T> type;
	private final Type mappingType;
	
	private Primitive(Class<T> type, Type mappingType) {
		this.type = type;
		this.mappingType = mappingType;
	}

	public Class<T> getType() {
		return type;
	}

	public Type getMappingType() {
		return mappingType;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// object identity is sufficient
		return super.equals(obj);
	}

	public static Primitive<?> forType(Type type) {
		return types.get(type);
	}
}