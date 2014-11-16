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
package org.beyene.protege.processor.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public final class IoUtil {

    private IoUtil() {
	// private constructor to prevent instantiation
    }

    public static byte[] readBytes(int n, InputStream is) throws IOException {
	byte[] bytes = new byte[n];
	for (int i = 0; i < n; i++) {
	    int b = is.read();
	    if (b == -1)
		throw new IOException(
			"Could not finish reading expected number of bytes!");
	    bytes[i] = (byte) b;
	}
	return bytes;
    }

    public static byte[] readBytes(int n, ReadableByteChannel channel)
	    throws IOException {
	ByteBuffer bb = ByteBuffer.allocate(n);
	while (bb.remaining() > 0)
	    if (channel.read(bb) == -1)
		;// TODO handle end-of-stream
	return bb.array();

    }

    public static int writeBytes(byte[] bytes, WritableByteChannel channel) throws IOException {
	ByteBuffer bb = ByteBuffer.wrap(bytes);
	int bytesWritten = 0;
	while (bytesWritten < bytes.length)
	    bytesWritten += channel.write(bb);
	return bytes.length;
    }
}