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
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

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
    public DataUnit read(Protocol p, ReadableByteChannel channel) throws IOException {
	ReadableByteChannel currentChannel = channel;
	DataUnit dataUnit = headerProcessor.read(p, currentChannel);
	
	int totalLength = ProtocolUtil.getTotalLength(dataUnit, p);
	/*
	 * could be read in parallel for instance.
	 */
	if (totalLength != -1) {
	    int toRead = totalLength - ProtocolUtil.getHeaderBytes(p);
	    byte[] bytes = IoUtil.readBytes(toRead, currentChannel);
	    currentChannel = Channels.newChannel(new ByteArrayInputStream(bytes));
	}

	dataUnit = unitProcessor.fromStream(dataUnit, p, currentChannel);
	currentChannel = channel;
	return dataUnit;
    }

    @Override
    public int write(DataUnit du, Protocol p, WritableByteChannel channel) throws IOException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	WritableByteChannel bufferChannel = Channels.newChannel(baos);
	int bytesWritten = unitProcessor.toStream(du, p, bufferChannel);
	
	if(ProtocolUtil.hasTotalLength(p)) {
	    String totalLengthId = p.getHeader().getConfiguration().getTotalLengthId();
	    int headerLength = ProtocolUtil.getHeaderBytes(p);
	    du.addPrimitiveValue(totalLengthId, Type.INTEGER, Long.valueOf(headerLength + bytesWritten));
	}
	
	bytesWritten += headerProcessor.write(du, p, channel);
	IoUtil.writeBytes(baos.toByteArray(), channel);
	return bytesWritten;
    }
}