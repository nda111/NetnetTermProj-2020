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
import data.User;
import interaction.IResponse;


public class ServerResponser implements Runnable {
	
	private Socket client = null;
	private Scanner reader = null;
	private PrintWriter writer = null;
	
	private User me = null;

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
	public void run() {
		
		EResponse response = null;
		while (response != EResponse.QUIT_OK) {
			
			final ERequest request = ERequest.valueOf(reader.nextByte()); // Recognize request
			final int paramCount = reader.nextInt(); // Get the number of parameters
			
			// Receive parameters in string form
			final String[] params = new String[paramCount];
			for (int i = 0; i < paramCount; i++) {
				
				params[i] = reader.nextLine().trim();
			}
			//ANNOUNCE is for 'server to client'
			//During client-server request, when data is needed to be sent, use ANNOUNCE
			if (request == ERequest.ANNOUNCE) {
				
				System.out.println("ANNOUNCE, " + client.getRemoteSocketAddress().toString());
			} else if (me == null || me.certificateByAddress(this.getClientAddress())) {

				// Act appropriate response
				final IResponse responser = Server.Responses.get(request);
				response = responser.response(params, this, reader, writer);
				
				final StringBuilder logBuilder = new StringBuilder("RESP, ");
				logBuilder.append(client.getRemoteSocketAddress().toString());
				logBuilder.append(", [");
	
				logBuilder.append(request.toString());
				logBuilder.append(", ");
				
				logBuilder.append(String.join(", ", params));
				logBuilder.append("], ");
				
				logBuilder.append(response.toString());
				if (response.getRequest() != request) {
					
					logBuilder.append(": RR-mismatch");
				}
				System.out.println(logBuilder.toString());
			} else {
				
				Server.Responses.get(ERequest.ECHO).response(params, this, reader, writer);
				System.out.println("RESP, ECHO_OK");
			}
		}
		
		try {
			//when client is over, close thread
			reader.close(); 
			writer.close(); 
			client.close(); 
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	//For checking whether client IP address is hacking or not
	public SocketAddress getClientAddress() {
		
		return client.getRemoteSocketAddress();
	}

	//bring own information
	public void setMe(User user) {
	
		this.me = user;
	}
	//storing own information
	public User getMeOrNull() {
		
		return this.me;
	}
}
