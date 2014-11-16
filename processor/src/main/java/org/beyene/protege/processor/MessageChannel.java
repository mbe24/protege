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

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.data.DataUnit;

public class MessageChannel implements AutoCloseable, Closeable, Channel {

    private final MessageChannelWorker delegate;

    private final ExecutorService executor;
    private final ConcurrentLinkedQueue<DataUnit> queue = new ConcurrentLinkedQueue<>();

    private CountDownLatch ready;
    private final AtomicBoolean hasException = new AtomicBoolean();

    private boolean block;
    private boolean open = true;

    private MessageChannel(Protocol p, ByteChannel channel) {
	this.delegate = new MessageChannelWorker(this, channel, p);
	this.executor = Executors.newFixedThreadPool(1);
	executor.submit(delegate);

	this.ready = new CountDownLatch(1);
	this.block = true;
    }

    public static MessageChannel from(Protocol p, ByteChannel channel) {
	return new MessageChannel(p, channel);
    }

    public DataUnit read() throws IOException {
	if (block) {
	    try {
		ready.await();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}

	if (hasException.get())
	    rethrowException();

	this.ready = new CountDownLatch(1);
	return queue.poll();
    }

    public void write(DataUnit object) throws IOException {
	delegate.write(object);
    }

    public MessageChannel configureBlocking(boolean block) {
	this.block = block;
	return this;
    }

    private void rethrowException() throws IOException {
	rethrow(delegate.getIOException());
    }

    private <T extends Exception> void rethrow(T exception) throws T {
	if (exception != null)
	    throw exception;
    }

    void setException() {
	hasException.set(true);
	ready.countDown();
    }

    void supply(DataUnit data) {
	ready.countDown();
	queue.add(data);
    }

    @Override
    public boolean isOpen() {
	return open;
    }

    @Override
    public void close() throws IOException {
	open = false;
	delegate.close();
	executor.shutdown();
    }
}