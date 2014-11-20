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
package org.beyene.protege.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.Callable;

import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.processor.protocol.MessageProcessor;

public class Client implements Callable<Void> {

    private final Selector selector;
    private final MessageProcessor mp;
    private final Protocol p;

    private volatile boolean online = true;

    private Client(Protocol p, MessageProcessor mp, SocketChannel channel, Selector selector) throws IOException {
	this.selector = selector;
	this.mp = mp;
	this.p = p;
    }

    public static Client newInstance(Protocol p, MessageProcessor mp, InetSocketAddress addr) throws IOException {
	SocketChannel channel = SocketChannel.open();
	channel.configureBlocking(false);
	channel.connect(addr);
	while (!channel.finishConnect());
	
	Selector selector = Selector.open();
	channel.register(selector, SelectionKey.OP_READ);
	Shell.setWritableChannel(channel);
	System.out.printf("Created client for %s:%d!%n", addr.getHostString(), addr.getPort());
	return new Client(p, mp, channel, selector);
    }

    public void shutdownClient() {
	online = false;
	selector.wakeup();
    }

    @Override
    public Void call() throws Exception {
	while (online) {
	    selector.select();
	    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
	    while (it.hasNext()) {
		SelectionKey key = it.next();
		it.remove();

		if (key.isReadable()) {
		    try {
			DataUnit du = mp.read(p, (SocketChannel) key.channel());
			if (!"message".equals(du.getUnit().getName())) {
			    System.out.printf("Skipping unit: %s.%n", du.getUnit().getName());
			    continue;
			}

			MetaData md = Units.extractMetaData(du);
			String message = du.getPrimitiveValue("message", Primitive.STRING);

			System.out.printf("%n>> %s - %s%n", md.getUser(), md.getDate().toString());
			System.out.printf(">  %s%n", message);
		    } catch (IOException e) {
			System.out.printf("Client %d disconnected!%n", (int) key.attachment());
			key.channel().close();
			key.cancel();
		    }
		}
	    }
	}
	
	for (SelectionKey key : selector.keys()) {
	    key.channel().close();
	    key.cancel();
	}
	
	selector.close();
	return null;
    }
}