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

import static org.beyene.protege.processor.atom.AtomProcessorFactory.getProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.beyene.protege.core.Element;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.core.encoding.Classifications;
import org.beyene.protege.processor.util.IoUtil;

public class GenericElementProcessor implements ElementProcessor {

    private static final LengthProcessor lengthProcessor = new LengthProcessor();

    @Override
    public Object fromStream(Element e, InputStream is) throws IOException {
	byte[] bytes = null;
	Type type = e.getType();
	String classification = e.getClassification();

	int length = lengthProcessor.fromStream(e, is);
	Object result = null;
	
	// TODO code too long, use map or sth. to stop iterating the same pattern
	//@formatter:off
	switch (type) {
	case BOOLEAN:
	    // TODO implement mechanism for boolean, e.g. mapping
	    Primitive<Boolean> _boolean = Primitive.BOOLEAN;
	    result = getProcessor(_boolean).interpret(bytes, null);
	    break;
		
	case BYTE:
	    Primitive<Byte[]> _byte = Primitive.BYTES;
	    result = getProcessor(_byte).interpret(bytes, null);
	    break;
		
	case DOUBLE:
	    Primitive<Double> _double = Primitive.DOUBLE;
	    // if classification == null then set to default encoding
	    bytes = IoUtil.readBytes(length, is);
	    result = getProcessor(_double).interpret(bytes, Classifications.get(classification, _double));
	    break;
		
	case FLOAT:
	    Primitive<Float> _float = Primitive.FLOAT;
	    bytes = IoUtil.readBytes(length, is);
	    result = getProcessor(_float).interpret(bytes, Classifications.get(classification, _float));
	    break;
		
	case INTEGER:
	    Primitive<Long> _integer = Primitive.INTEGER;
	    bytes = IoUtil.readBytes(length / 8, is);
	    result = getProcessor(_integer).interpret(bytes, Classifications.get(classification, _integer));
	    break;

	case STRING:
	    Primitive<String> _string = Primitive.STRING;
	    bytes = IoUtil.readBytes(length / 8, is);
	    result = getProcessor(_string).interpret(bytes, Classifications.get(classification, _string));
	    break;

	default:
	    // if number of primitives does not increase, this default case can never be reached.
	    throw new IllegalStateException();
	}
	//@formatter:on
	return result;
    }

    // for testing purposes
    <T> T fromStream(Element e, InputStream is, Primitive<T> primitive) throws IOException {
	if (e.getType() != primitive.getMappingType())
	    throw new IllegalArgumentException(
		    String.format("Element's type %s and primitive's type %s are not compatible!",
			    e.getType().name(), primitive.getMappingType().name()));
	
	return primitive.getType().cast(fromStream(e, is));
    }

    @Override
    public <T> void toStream(T object, Element e, OutputStream os)
	    throws IOException {
	// TODO Auto-generated method stub
    }
}