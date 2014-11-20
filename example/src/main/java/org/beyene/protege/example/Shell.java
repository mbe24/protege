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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.WritableByteChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.processor.protocol.DefaultMessageProcessor;
import org.beyene.protege.processor.protocol.MessageProcessor;

public final class Shell {

    private static final String PROMPT = "> ";
    
    private static final Pattern startServer = Pattern.compile("start\\s+([1-9][0-9]+)");
    private static final Pattern connectServer = Pattern.compile("join\\s+([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}):([1-9][0-9]+)");
    
    private static boolean connected = false;
    
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static Server server;
    private static Client client;
    private static WritableByteChannel channel;
    
    private static final Protocol p = Units.getProtocol();
    private static final MessageProcessor mp = new DefaultMessageProcessor(p);
    
    public static void main(String[] args) {
	
	boolean quit = false;
	
	Scanner s = new Scanner(System.in);
	while (!quit) {
	    System.out.print(PROMPT);
	    quit = evaluateInput(s.nextLine());
	}
	s.close();
	if (server != null)
	    server.shutdownServer();
	
	if (client != null)
	    client.shutdownClient();
	
	executor.shutdown();
	System.out.println("Execution finished!");
    }
    
    private static boolean evaluateInput(String input) {
	boolean quit = false;
	boolean caseMatched = false;
	if ("quit".equals(input) || "q".equals(input)) {
	    quit = true;
	    caseMatched = true;
	} 
	
	Matcher m = startServer.matcher(input);
	if (!caseMatched && m.find()) {
	    int port = Integer.valueOf(m.group(1));
	    try {
		server = Server.newInstance(p, mp, new InetSocketAddress(InetAddress.getByName(null), port));
		executor.submit(server);
	    } catch (IOException e) {
		System.err.println(e.getMessage());
	    }
	    caseMatched = true;
	}
	
	m = connectServer.matcher(input);
	if (!caseMatched && m.find()) {
	    int port = Integer.valueOf(m.group(2));
	    try {
		client = Client.newInstance(p, mp, new InetSocketAddress(m.group(1), port));
		executor.submit(client);
	    } catch (IOException e) {
		System.err.println(e.getMessage());
	    }
	    caseMatched = true;
	}
	
	if (!caseMatched && "status".equals(input)) {
	    System.out.printf("status: %s%n", (connected) ? "online" : "offline");
	    caseMatched = true;
	}
	
	// send if connected
	if (!connected && !caseMatched) {
	    System.out.println("Command not supported!");
	} else if (connected && !caseMatched) {
	    try {
		DataUnit du = Units.getEmptyMessageFor("message", "miky");
		du.addPrimitiveValue("message", Type.STRING, input);
		mp.write(du, p, channel);
	    } catch (IOException e) {
		System.err.println(e.getMessage());
		connected = false;
	    }
	}
	return quit;
    }
    
    public static void setWritableChannel(WritableByteChannel channel) {
	Shell.channel = channel;
	connected = true;
    }
}