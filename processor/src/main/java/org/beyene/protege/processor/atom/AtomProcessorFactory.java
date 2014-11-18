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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.beyene.protege.core.data.Primitive;

public final class AtomProcessorFactory {

    private static Map<Primitive<?>, AtomProcessor<?>> processors = new HashMap<Primitive<?>, AtomProcessor<?>>();
    static {
	for (AtomProcessor<?> p : Arrays.asList(
		new ByteProcessor(), new DoubleProcessor(),
		new FloatProcessor(), new IntegerProcessor(),
		new StringProcessor())) {
	    processors.put(p.getPrimitive(), p);
	}
    }

    private AtomProcessorFactory() {
	// private constructor to prevent instantiation
    }

    @SuppressWarnings("unchecked")
    public static <T> AtomProcessor<T> getProcessor(Primitive<T> primitive) {
	return (AtomProcessor<T>) processors.get(primitive);
    }
}