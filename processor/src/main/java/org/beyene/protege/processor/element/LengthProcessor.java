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
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import org.beyene.protege.core.Element;
import org.beyene.protege.core.Length;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.Value;
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.core.encoding.Classifications;
import org.beyene.protege.core.encoding.IntegerEncoding;
import org.beyene.protege.processor.util.IoUtil;

enum LengthProcessor implements ElementProcessor<Integer> {

    INSTANCE;

    @Override
    public Integer read(Element e, ReadableByteChannel channel) throws IOException {
	// doubles and floats have fixed length
	Type type = e.getType();
	if (type == Type.DOUBLE)
	    return Classifications.get(e.getClassification(), Primitive.DOUBLE).getWidth() / 8;
	else if (type == Type.FLOAT)
	    return Classifications.get(e.getClassification(), Primitive.FLOAT).getWidth() / 8;

	Length l = e.getLength();
	int length = 0;
	if (l != null) {
	    Integer n = l.getQuantity();
	    if (n != null) {
		length = n;
	    } else {
		Integer lengthField = l.getPrecedingLengthFieldSize();
		if (lengthField == null)
		    throw new IllegalArgumentException("Preceding length field is null!");

		byte[] preBytes = IoUtil.readBytes(lengthField, channel);
		/*
		 * conversion to int should be safe, since preceding length
		 * field of more than 4 byte is not realistic (1 byte is enough)
		 * 
		 * NOTE: It's may not be working, if you want an 8 byte unsigned
		 * field because if the highest bit (of 64) is set, number is
		 * interpreted as two's complement.
		 */
		length = getProcessor(Primitive.INTEGER).interpret(preBytes,
			IntegerEncoding.UNSIGNED).intValue();
	    }
	} else {
	    // only used for reading of expected plain bytes
	    Value v = e.getValue();
	    if (v != null)
		length = v.getBytes().length;
	}
	return length;
    }

    @Override
    public int write(Integer object, Element e, WritableByteChannel channel)
	    throws IOException {
	// doubles and floats have fixed length
	Type type = e.getType();
	if (type == Type.DOUBLE)
	    return Classifications.get(e.getClassification(), Primitive.DOUBLE).getWidth() / 8;
	else if (type == Type.FLOAT)
	    return Classifications.get(e.getClassification(), Primitive.FLOAT).getWidth() / 8;

	Length l = e.getLength();
	int length = 0;
	if (l != null) {
	    Integer n = l.getQuantity();
	    if (n != null) {
		length = n;
	    } else {
		Integer lengthField = l.getPrecedingLengthFieldSize();
		if (lengthField == null)
		    throw new IllegalArgumentException("Preceding length field is null!");
		if (object == null)
		    throw new IllegalArgumentException("Preceding length value is null!");

		byte[] bytes = getProcessor(Primitive.INTEGER).toBytes(
			object.longValue(), IntegerEncoding.UNSIGNED,
			lengthField);

		IoUtil.writeBytes(bytes, channel);
		length = object;
	    }
	} else {
	    // only used for reading of expected plain bytes
	    Value v = e.getValue();
	    if (v != null)
		length = v.getBytes().length;
	}
	return length;
    }
}