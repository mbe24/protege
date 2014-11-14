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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.beyene.protege.core.Type;

public class DataBroker implements HeterogeneousContainer {

    private final Map<Type, Map<String, Object>> primitives = new HashMap<>();
    private final Map<String, Composition> complexObjects = new HashMap<>();
    private final Map<String, Collection<Composition>> complexCollections = new HashMap<>();

    {
	for (Type type : Type.values())
	    primitives.put(type, new HashMap<String, Object>());
    }

    @Override
    public boolean addPrimitiveValue(String id, Type type, Object value)
	    throws IllegalArgumentException {
	if (id == null)
	    return false;

	Primitive<?> primitive = Primitive.forType(type);
	if (!primitive.getType().isInstance(value))
	    throw new IllegalArgumentException(String.format(
		    "Supplied object is not of type %s!", primitive.getType()
			    .getSimpleName()));

	Map<String, Object> typeMap = primitives.get(type);
	Object previousValue = typeMap.put(id, value);
	return previousValue == null;
    }

    @Override
    public <T> T getPrimitiveValue(String id, Primitive<T> primitive)
	    throws IllegalArgumentException {
	Map<String, Object> typeMap = primitives
		.get(primitive.getMappingType());
	if (!typeMap.containsKey(id))
	    return null;
	Object value = typeMap.get(id);

	if (!primitive.getType().isInstance(value))
	    throw new IllegalArgumentException(String.format(
		    "Expected object is not of type %s!", primitive.getType()
			    .getSimpleName()));

	return primitive.getType().cast(value);
    }

    @Override
    public boolean addComplexObject(String id, Composition c) {
	if (id == null)
	    return false;

	if (complexCollections.containsKey(id)) {
	    complexCollections.get(id).add(c);
	}
	/*
	 * no mapping for single composition means its either a list element or
	 * single element
	 */
	else if (!complexObjects.containsKey(id)) {
	    complexObjects.put(id, c);
	}
	else {
	    /*
	     * if there already is a mapping, composition gets added to a list
	     */
	    Collection<Composition> list = new ArrayList<>();
	    Composition toBeAdded = complexObjects.remove(id);
	    list.add(toBeAdded);
	    list.add(c);
	    complexCollections.put(id, list);
	}
	return true;
    }

    // @Override
    // public Composition getComplexObject(String id) {
    // return complexObjects.get(id);
    // }
    //
    // @Override
    // public boolean addComplexCollection(String id, Collection<Composition>
    // col) {
    // if (id == null)
    // return false;
    //
    // return complexCollections.put(id, col) == null;
    // }

    @Override
    public Collection<Composition> getComplexCollection(String id) {
	return complexCollections.get(id);
    }
}