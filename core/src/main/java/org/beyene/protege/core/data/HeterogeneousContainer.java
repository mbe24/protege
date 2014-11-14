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

public interface HeterogeneousContainer {
    
    public boolean addPrimitiveValue(String id, Type type, Object value) throws IllegalArgumentException;
    
    public <T> T getPrimitiveValue(String id, Primitive<T> primitive) throws IllegalArgumentException;
    
    public boolean addComplexObject(String id, Composition c);
    
    public Collection<Composition> getComplexCollection(String id); 
}