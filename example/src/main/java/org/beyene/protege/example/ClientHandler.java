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
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.processor.protocol.MessageProcessor;

public class ClientHandler implements Callable<Void> {

    private final Selector selector;
    private final MessageProcessor mp;
    private final Protocol p;

    private final Map<SocketChannel, MessageHandler> handlers = new ConcurrentHashMap<>();

    private ClientHandler(Selector selector, Protocol p, MessageProcessor mp) {
	this.selector = selector;
	this.p = p;
	this.mp = mp;
    }

    public static ClientHandler create(Protocol p, MessageProcessor mp) throws IOException {
	return new ClientHandler(Selector.open(), p, mp);
    }

    public SocketChannel addChannel(SocketChannel channel, MessageHandler handler) throws ClosedChannelException {
	channel.register(selector, SelectionKey.OP_READ);
	handlers.put(channel, handler);
	return channel;
    }

    public SocketChannel removeChannel(SocketChannel channel,  MessageHandler handler) {
	channel.keyFor(selector).cancel();
	handlers.remove(channel);
	return channel;
    }

    @Override
    public Void call() throws IOException {
	boolean online = true;
	while (online) {
	    selector.select();

	    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
	    while (it.hasNext()) {
		SelectionKey key = it.next();
		it.remove();

		SocketChannel channel = (SocketChannel) key.channel();
		MessageHandler handler = handlers.get(channel);
		if (key.isReadable()) {
		    DataUnit message;
		    try {
			message = mp.read(p, channel);
			handler.handleMessage(message);
		    } catch (IOException e) {
			handler.notifyDisconnect();
			handlers.remove(channel);
			key.cancel();
		    }
		}
	    }
	}
	return null;
    }
    
    public int clientCount() {
	return handlers.size();
    }
}