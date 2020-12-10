package app;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

import data.ERequest;
import data.EResponse;
import interaction.IResponse;


public class ServerResponser extends Thread {
	
	private Socket client = null;
	private Scanner reader = null;
	private PrintWriter writer = null;

	public ServerResponser(Socket client) {

		this.client = client;
		
		try {
			
			reader = new Scanner(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	@Override
	public void start() {
		
		while (true) {
			
			final StringBuilder logBuilder = new StringBuilder("RESP, ");
			logBuilder.append(client.getRemoteSocketAddress().toString());
			logBuilder.append(", [");
			
			final ERequest request = ERequest.valueOf(reader.nextByte()); // Recognize request
			final int paramCount = reader.nextInt(); // Get the number of parameters
			
			logBuilder.append(request.toString());
			logBuilder.append(", ");
			
			// Receive parameters in string form
			final String[] params = new String[paramCount];
			for (int i = 0; i < paramCount; i++) {
				
				params[i] = reader.nextLine();
				
				logBuilder.append(params[i]);
				logBuilder.append(", ");
			}
			logBuilder.delete(logBuilder.length() - 3, logBuilder.length() - 1);
			logBuilder.append("], ");
			
			// Act appropriate response
			final IResponse responser = Server.Responses.get(request);
			final EResponse response = responser.response(params, this, reader, writer);
			
			logBuilder.append(response.toString());
			if (response.getRequest() != request) {
				
				logBuilder.append(": RR-mismatch");
			}
			
			System.out.println(logBuilder.toString());
		}
	}
	
	public SocketAddress getClientAddress() {
		
		return client.getRemoteSocketAddress();
	}
}
