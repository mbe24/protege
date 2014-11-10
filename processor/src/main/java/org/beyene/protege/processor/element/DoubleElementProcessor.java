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
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.core.encoding.Classifications;
import org.beyene.protege.core.encoding.Encoding;
import org.beyene.protege.processor.util.IoUtil;

enum DoubleElementProcessor implements ElementProcessor<Double> {

    INSTANCE;
    
    @Override
    public Double fromStream(Element e, InputStream is) throws IOException {
	int length = LengthProcessor.INSTANCE.fromStream(e, is);
	// if classification == null then set to default encoding
	String classification = e.getClassification();
	Encoding<Double> encoding = Classifications.get(classification, Primitive.DOUBLE);
	return getProcessor(Primitive.DOUBLE).interpret(IoUtil.readBytes(length / 8, is), encoding);
    }

    @Override
    public int toStream(Double object, Element e, OutputStream os) throws IOException {
	String classification = e.getClassification();
	Encoding<Double> encoding = Classifications.get(classification, Primitive.DOUBLE);
	int length = LengthProcessor.INSTANCE.toStream(-1, e, os);
	byte[] bytes = getProcessor(Primitive.DOUBLE).toBytes(object, encoding, length);
	return IoUtil.writeBytes(bytes, length / 8, os);
    }
}