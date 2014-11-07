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
import org.beyene.protege.core.Length;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.Value;
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.core.encoding.Classifications;
import org.beyene.protege.core.encoding.DoubleEncoding;
import org.beyene.protege.core.encoding.FloatEncoding;
import org.beyene.protege.core.encoding.IntegerEncoding;
import org.beyene.protege.processor.util.IoUtil;

public class GenericElementProcessor implements ElementProcessor {

    @Override
    public Object fromStream(Element e, InputStream is) throws IOException {
	byte[] bytes = null;
	Type type = e.getType();
	String classification = e.getClassification();
	Length l = e.getLength();

	int length = 0;
	if (l != null) {
	    Integer bits = l.getBit();
	    if (bits != null) {
		length = bits;
	    } else {
		Integer lengthField = l.getPrecedingLengthFieldSize();
		if (lengthField == null)
		    throw new IllegalArgumentException(
			    "Preceding length field is null!");

		byte[] preBytes = IoUtil.readBytes(lengthField / 8, is);
		/*
		 * conversion to int should be safe, since preceding length
		 * field of more than 4 byte is not realistic (1 byte is enough)
		 */
		length = getProcessor(Primitive.INTEGER).interpret(preBytes,
			IntegerEncoding.TWOS_COMPLEMENT).intValue();
	    }
	} else {
	    Value v = e.getValue();
	    if (v != null)
		length = 8 * v.getBytes().length;
	}

	Object result = null;

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
			DoubleEncoding de = DoubleEncoding.valueOf(classification);
			bytes = IoUtil.readBytes(de.getWidth() / 8, is);
			result = getProcessor(_double).interpret(bytes, de);
			break;
			
		case FLOAT:
			Primitive<Float> _float = Primitive.FLOAT;
			FloatEncoding fe = FloatEncoding.valueOf(classification);
			bytes = IoUtil.readBytes(fe.getWidth() / 8, is);
			result = getProcessor(_float).interpret(bytes, fe);
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
			break;
		}
		//@formatter:on
	return result;
    }

    @Override
    public <T> void toStream(T object, Element e, OutputStream os)
	    throws IOException {
	// TODO Auto-generated method stub
    }
}