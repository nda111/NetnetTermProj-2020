package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import app.ServerResponser;
import data.EResponse;
import interaction.IResponse;


public final class EchoResponse implements IResponse {
	
	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {
		
		final EResponse response = EResponse.ECHO_OK;
		
		writer.println(response.getValue());

		for (String param : params) {
			
			writer.println(param);
		}
		writer.flush();
		
		return response;
	}
}
