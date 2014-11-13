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
package org.beyene.protege.processor.element;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.beyene.protege.core.Element;

public enum GenericElementProcessor implements ElementProcessor<Object> {

    INSTANCE;

    @Override
    public Object fromStream(Element e, InputStream is) throws IOException {
	ElementProcessor<Object> ep = ElementProcessorFactory.getProcessor(e.getType());

	// if number of types does not increase, this default case can never be
	// reached.
	if (ep == null)
	    throw new IllegalStateException();

	return ep.fromStream(e, is);
    }

    @Override
    public int toStream(Object object, Element e, OutputStream os) throws IOException {
	ElementProcessor<Object> ep = ElementProcessorFactory.getProcessor(e.getType());

	// if number of types does not increase, this default case can never be
	// reached.
	if (ep == null)
	    throw new IllegalStateException();
	return ep.toStream(object, e, os);
    }
}