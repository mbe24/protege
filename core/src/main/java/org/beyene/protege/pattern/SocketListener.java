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
package org.beyene.protege.pattern;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import org.beyene.protege.core.ComplexType;
import org.beyene.protege.core.Protocol;

final class SocketListener implements Callable<Void> {

	private final InputParser ip;
	private final InputStream is;
	private final Protocol p;

	// exceptions
	private IOException ioe;

	public SocketListener(InputParser ip, InputStream is, Protocol p) {
		this.ip = ip;
		this.is = is;
		this.p = p;
	}

	// add notifier for important exceptions
	@Override
	public Void call() throws Exception {
		// TODO implement
		
		@SuppressWarnings("unused")
		ComplexType header = p.getHeader();

		@SuppressWarnings("unused")
		byte octett;
		try {
			while ((octett = (byte) is.read()) != -1) {

			}
		} catch (IOException e) {
			ioe = e;
			ip.setException();

		}
		
		return null;
	}

	IOException getIOException() {
		return ioe;
	}
}