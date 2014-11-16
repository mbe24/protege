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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.Callable;

import org.beyene.protege.core.Protocol;
import org.beyene.protege.processor.protocol.MessageProcessor;

public class Server implements Callable<Void> {

    private final Selector selector;
    private final ClientHandler clientHandler;

    private Server(Protocol p, MessageProcessor mp, ServerSocketChannel channel, Selector selector) throws IOException {
	this.selector = selector;
	this.clientHandler = ClientHandler.create(p, mp);
    }

    public static Server newInstance(Protocol p, MessageProcessor mp, InetSocketAddress addr) throws IOException {
	ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
	serverSocketChannel.configureBlocking(false);
	serverSocketChannel.socket().bind(new InetSocketAddress(80));
	Selector selector = Selector.open();
	serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	return new Server(p, mp, serverSocketChannel, selector);
    }

    @Override
    public Void call() throws Exception {
	boolean online = true;
	while (online) {
	    selector.select();
	    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
	    while (it.hasNext()) {
		SelectionKey key = it.next();
		it.remove();

		if (key.isAcceptable()) {
		    ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
		    SocketChannel channel = ssChannel.accept();
		    channel.configureBlocking(false);
		    clientHandler.addChannel(channel, new SimpleMessageHandler());
		}
	    }
	}
	return null;
    }
}