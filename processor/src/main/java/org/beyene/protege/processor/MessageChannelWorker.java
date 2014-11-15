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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.processor.protocol.DefaultMessageProcessor;
import org.beyene.protege.processor.protocol.MessageProcessor;

final class MessageChannelWorker implements Callable<Void>, AutoCloseable {

    private final MessageChannel channel;
    private final Protocol p;
    
    private final InputStream is;
    private final OutputStream os;

    private final MessageProcessor mp;
    
    private boolean online = true;
    
    // exceptions
    private IOException ioe;

    public MessageChannelWorker(MessageChannel channel, InputStream is, OutputStream os, Protocol p) {
	this.channel = channel;
	this.p = p;
	this.mp = new DefaultMessageProcessor(p);
	
	this.is = is;
	this.os = os;
    }

    @Override
    public Void call() {
	try {
	    while (online) {
		DataUnit dataUnit = mp.fromStream(p, is);
		channel.supply(dataUnit);
	    }
	} catch (IOException e) {
	    ioe = e;
	    channel.setException();
	}
	return null;
    }

    IOException getIOException() {
	return ioe;
    }

    public int write(DataUnit object) throws IOException {
	return mp.toStream(object, p, os);
    }
    
    @Override
    public void close() {
	online = false;
    }
}