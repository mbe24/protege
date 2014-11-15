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
package org.beyene.protege.processor.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.processor.util.IoUtil;
import org.beyene.protege.processor.util.ProtocolUtil;

public class DefaultMessageProcessor implements MessageProcessor {

    private final HeaderProcessor headerProcessor;
    private final UnitProcessor unitProcessor = DefaultUnitProcessor.INSTANCE;

    public DefaultMessageProcessor(Protocol p) {
	this.headerProcessor = new DefaultHeaderProcessor(p);
    }

    @Override
    public DataUnit fromStream(Protocol p, InputStream is) throws IOException {
	InputStream currentStream = is;
	DataUnit dataUnit = headerProcessor.fromStream(p, currentStream);
	
	int totalLength = ProtocolUtil.getTotalLength(dataUnit, p);
	/*
	 * could be read in parallel for instance.
	 */
	if (totalLength != -1) {
	    int toRead = totalLength - ProtocolUtil.getHeaderBytes(p);
	    byte[] bytes = IoUtil.readBytes(toRead, currentStream);
	    currentStream = new ByteArrayInputStream(bytes);
	}

	dataUnit = unitProcessor.fromStream(dataUnit, p, currentStream);
	return dataUnit;
    }

    @Override
    public int toStream(DataUnit du, Protocol p, OutputStream os) throws IOException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	int bytesWritten = unitProcessor.toStream(du, p, baos);
	
	if(ProtocolUtil.hasTotalLength(p)) {
	    String totalLengthId = p.getHeader().getConfiguration().getTotalLengthId();
	    int headerLength = ProtocolUtil.getHeaderBytes(p);
	    du.addPrimitiveValue(totalLengthId, Type.INTEGER, Long.valueOf(headerLength + bytesWritten));
	}
	
	bytesWritten += headerProcessor.toStream(du, p, os);
	baos.writeTo(os);
	return bytesWritten;
    }
}