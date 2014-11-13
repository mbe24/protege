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

import java.util.Collection;

import org.beyene.protege.core.Type;
import org.beyene.protege.core.Unit;

public class DataUnit implements HeterogeneousContainer {

    private Unit unit;
    private final DataBroker delegate = new DataBroker();

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public boolean addPrimitiveValue(String id, Type type, Object value) throws IllegalArgumentException {
	return delegate.addPrimitiveValue(id, type, value);
    }

    @Override
    public <T> T getPrimitiveValue(String id, Primitive<T> primitive) throws IllegalArgumentException {
	return delegate.getPrimitiveValue(id, primitive);
    }

    @Override
    public boolean addComplexObject(String id, Composition c) {
	return delegate.addComplexObject(id, c);
    }

    @Override
    public Composition getComplexObject(String id) {
	return delegate.getComplexObject(id);
    }

    @Override
    public boolean addComplexCollection(String id, Collection<Composition> col) {
	return delegate.addComplexCollection(id, col);
    }

    @Override
    public Collection<Composition> getComplexCollection(String id) {
	return delegate.getComplexCollection(id);
    }
}