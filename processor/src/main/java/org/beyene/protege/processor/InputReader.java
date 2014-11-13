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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.data.DataUnit;

public class InputReader implements AutoCloseable {

    private final InputStream is;
    private final SocketListener delegate;

    private final ExecutorService executor;
    private final ConcurrentLinkedQueue<DataUnit> queue = new ConcurrentLinkedQueue<>();

    private CountDownLatch ready;
    private final AtomicBoolean hasException = new AtomicBoolean();

    private InputReader(InputStream is, Protocol p) {
	this.is = is;

	this.delegate = new SocketListener(this, is, p);
	this.executor = Executors.newFixedThreadPool(1);
	executor.submit(delegate);

	this.ready = new CountDownLatch(1);
    }

    public static InputReader from(InputStream is, Protocol p) {
	return new InputReader(is, p);
    }

    public DataUnit read() throws IOException {
	try {
	    ready.await();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	if (hasException.get())
	    rethrowException();

	this.ready = new CountDownLatch(1);
	return queue.poll();
    }

    @Override
    public void close() throws IOException {
	delegate.close();
	is.close();
	executor.shutdown();
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
}