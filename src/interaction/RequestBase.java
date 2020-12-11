package interaction;

import java.io.PrintWriter;
import java.util.Scanner;

import data.ERequest;
import data.EResponse;

public abstract class RequestBase {
	
	private ERequest request;
	private String[] params;
	private Scanner reader;
	private PrintWriter writer;

	public RequestBase(ERequest request, String[] params, Scanner reader, PrintWriter writer) {
		
		this.request = request;
		this.params = params;
		this.reader = reader;
		this.writer = writer;
	}
	
	public EResponse request() {
		
		writer.print(request.getValue());
		writer.print(' ');
		
		writer.print(params.length);
		writer.print(' ');
		
		for (String param : params) {
			
			writer.println(param);
		}
		writer.flush();
		
		EResponse response = EResponse.valueOf(reader.nextShort());
		
		System.out.println(response.toString());
		
		handle(response, reader, writer);
		
		return response;
	}
	
	protected abstract void handle(EResponse response, Scanner reader, PrintWriter writer);
}
