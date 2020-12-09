package app;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
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
			
			ERequest request = ERequest.valueOf(reader.nextByte()); // Recognize request
			int paramCount = reader.nextInt(); // Get the number of parameters
			
			// Receive parameters in string form
			String[] params = new String[paramCount];
			for (int i = 0; i < paramCount; i++) {
				
				params[i] = reader.nextLine();
			}
			
			// Act appropriate response
			IResponse responser = Server.Responses.get(request);
			EResponse response = responser.response(params, reader, writer);
			
			// Send the response result
			writer.println(response.getValue());
		}
	}
}
