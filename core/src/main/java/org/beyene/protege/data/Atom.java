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
package org.beyene.protege.data;

import org.beyene.protege.core.Type;

// data represented by primitive element
public class Atom {

	// IDs can be referenced
	private final String id;
	private final Primitive<?> primitive;
	private Object value;

	public Atom(String id, Primitive<?> primitive, Object value) {
		this.id = id;
		this.primitive = primitive;
		this.value = value;

		if (primitive.getType().isInstance(value))
			throw new IllegalStateException(String.format("Object is not assignable from %s!", primitive.getType().getName()));
	}
	
	public Atom(String id, Primitive<?> primitive) {
		this.id = id;
		this.primitive = primitive;
		this.value = null;
	}

	public String getId() {
		return id;
	}

	public boolean hasId() {
		return id != null;
	}
	
	public Type getType() {
		return primitive.getMappingType();
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		if (primitive.getType().isInstance(value))
			throw new IllegalStateException(String.format("Object is not assignable from %s!", primitive.getType().getName()));
		this.value = value;
	}
}