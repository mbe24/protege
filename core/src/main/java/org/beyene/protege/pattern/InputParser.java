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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.beyene.protege.core.ComplexType;
import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Unit;
import org.beyene.protege.data.DataUnit;

public class InputParser implements Closeable {

	private final String protocol;

	private final InputStream is;

	private final ComplexType header;
	private final List<Unit> units;

	private CountDownLatch ready;
	
	private InputParser(InputStream is, Protocol p) {
		this.protocol = p.getName();

		this.is = is;

		this.header = p.getHeader();
		this.units = p.getUnits();
		
		this.ready = new CountDownLatch(1);
	}

	public static InputParser from(InputStream is, Protocol p) {
		return new InputParser(is, p);
	}

	public DataUnit read() {
		try {
			ready.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DataUnit result = null;
		this.ready = new CountDownLatch(1);
		
		return result;
	}
	
	@Override
	public void close() throws IOException {
		is.close();
	}
}