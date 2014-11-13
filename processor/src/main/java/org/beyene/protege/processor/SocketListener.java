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
package org.beyene.protege.processor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.processor.protocol.DefaultHeaderProcessor;
import org.beyene.protege.processor.protocol.DefaultUnitProcessor;
import org.beyene.protege.processor.protocol.HeaderProcessor;
import org.beyene.protege.processor.protocol.UnitProcessor;
import org.beyene.protege.processor.util.IoUtil;
import org.beyene.protege.processor.util.ProtocolUtil;

final class SocketListener implements Callable<Void>, AutoCloseable {

    private final InputReader reader;
    private final InputStream is;
    private final Protocol p;

    private HeaderProcessor headerProcessor = DefaultHeaderProcessor.INSTANCE;
    private UnitProcessor unitProcessor = DefaultUnitProcessor.INSTANCE;
    
    private boolean online = true;
    
    // exceptions
    private IOException ioe;

    public SocketListener(InputReader reader, InputStream is, Protocol p) {
	this.reader = reader;
	this.is = is;
	this.p = p;
    }

    @Override
    public Void call() {

	InputStream currentStream = is;
	try {
	    while (online) {
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
		reader.supply(dataUnit);
		
		currentStream = is;
	    }
	} catch (IOException e) {
	    ioe = e;
	    reader.setException();
	}
	return null;
    }

    IOException getIOException() {
	return ioe;
    }

    @Override
    public void close() {
	online = false;
    }
}