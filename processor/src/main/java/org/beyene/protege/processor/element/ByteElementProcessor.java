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
import java.util.Arrays;

import org.beyene.protege.core.Element;
import org.beyene.protege.core.Value;
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.processor.exception.DataMismatchException;
import org.beyene.protege.processor.util.ElementUtil;
import org.beyene.protege.processor.util.IoUtil;

enum ByteElementProcessor implements ElementProcessor<Byte[]> {

    INSTANCE;

    @Override
    public Byte[] read(Element e, ReadableByteChannel channel) throws IOException {
	int length = LengthProcessor.INSTANCE.read(e, channel);
	byte[] bytes = IoUtil.readBytes(length / 8, channel);

	if (ElementUtil.hasValue(e)) {
	    Value value = ElementUtil.getValue(e);
	    byte[] fixed = value.getBytes();
	    if (!Arrays.equals(fixed, bytes))
		throw new DataMismatchException(fixed, bytes);
	}
	return getProcessor(Primitive.BYTES).interpret(bytes, null);
    }

    @Override
    public int write(Byte[] object, Element e, WritableByteChannel channel) throws IOException {
	int l = (object == null) ? 0 : object.length * 8;
	int length = LengthProcessor.INSTANCE.write(l, e, channel);
	byte[] bytes;
	if (ElementUtil.hasValue(e)) {
	    Value value = ElementUtil.getValue(e);
	    byte[] fixed = value.getBytes();
	    if (object != null) {
		bytes = getProcessor(Primitive.BYTES).toBytes(object, null, length);
		if (!Arrays.equals(fixed, bytes))
		    throw new DataMismatchException(fixed, bytes);
	    }
	    bytes = fixed;
	} else
	    bytes = getProcessor(Primitive.BYTES).toBytes(object, null, length);

	return IoUtil.writeBytes(bytes, channel) + ElementUtil.precedingLengthWritten(e);
    }
}