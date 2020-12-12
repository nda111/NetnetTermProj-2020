package interaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import data.ERequest;
import data.EResponse;
import gui.WindowBase;

public abstract class RequestBase {

	private static HashMap<ERequest, RequestBase> Requests = new HashMap<>();
	private static HashMap<ERequest, EResponse> Responses = new HashMap<>();
	
	private static boolean once = true;
	
	private static Scanner reader;
	private static PrintWriter writer;
	
	public static boolean tryInitSocket(Socket socket) {
		
		try {
		
			reader = new Scanner(socket.getInputStream());
			writer = new PrintWriter(socket.getOutputStream());
			
			return true;
		} catch (IOException e) {
			
			e.printStackTrace();
			
			return false;
		}
	}
	
	private ERequest request;
	private String[] params;

	public RequestBase(ERequest request, String[] params) {
		
		this.request = request;
		this.params = params;
		
		if (once) {
			
			once = false;
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {

					EResponse response = null;
					while (response != EResponse.QUIT_OK) {
						
						while (!reader.hasNextShort()) {
							
							reader.next();
						}
						
						response = EResponse.valueOf(reader.nextShort());
						ERequest request = response.getRequest();
						
						System.out.println(response);
						
						if (request == ERequest.ANNOUNCE) {
							
							while (!reader.hasNextInt()) {
								
								reader.next();
							}
							
							int numParams = reader.nextInt();
							String[] announceParams = new String[numParams];
							
							for (int i = 0; i < numParams; i++) {
								
								announceParams[i] = reader.nextLine();
							}
							
							if (WindowBase.CurrentWindow != null) {
								
								WindowBase.CurrentWindow.handleAnnouncement(response, announceParams);
								
								Responses.put(request, response);
							}
						} else if (Requests.containsKey(request)) {
	
							Requests.get(request).handle(response, reader, writer);
							Requests.remove(response.getRequest());
							
							Responses.put(request, response);
						} else {
							
							System.out.println("?" + response.toString());
						}
					}
				}
			}).start();
		}
	}
	
	public void request() {
		
		if (request != ERequest.ANNOUNCE) {

			Requests.put(request, this);
		}
		
		writer.print(request.getValue());
		writer.print(' ');
		
		writer.print(params.length);
		writer.print(' ');
		
		for (String param : params) {
			
			writer.println(param);
		}
		writer.flush();
	}
	
	protected abstract void handle(EResponse response, Scanner reader, PrintWriter writer);
}
