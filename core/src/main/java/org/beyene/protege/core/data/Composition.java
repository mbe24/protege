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

import java.util.List;

import org.beyene.protege.core.Type;

public class Composition implements HeterogeneousContainer {
    
    private String id;
    private final DataBroker delegate = new DataBroker();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean addPrimitiveValue(String id, Type type, Object value) {
	return delegate.addPrimitiveValue(id, type, value);
    }

    @Override
    public <T> T getPrimitiveValue(String id, Primitive<T> primitive) {
	return delegate.getPrimitiveValue(id, primitive);
    }

    @Override
    public boolean addComplexObject(String id, Composition c) {
	return delegate.addComplexObject(id, c);
    }

    @Override
    public List<Composition> getComplexCollection(String id) {
	return delegate.getComplexCollection(id);
    }
}