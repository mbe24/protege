package org.beyene.protege.pattern.atom;

import java.nio.charset.Charset;

import org.beyene.protege.data.Primitive;

public class StringProcessor extends AbstractAtomProcessor<String> {

	private final Charset charset;

	public StringProcessor(int length) {
		this(length, "UTF-8");
	}

	public StringProcessor(int length, String charSet) {
		super(length, Primitive.STRING);
		this.charset = Charset.forName(charSet);
	}

	@Override
	public String interpret(byte[] bytes) {
		return new String(bytes, charset);
	}

	@Override
	public byte[] toBytes(String element) {
		return element.getBytes(charset);
	}
}