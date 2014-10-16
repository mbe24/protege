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