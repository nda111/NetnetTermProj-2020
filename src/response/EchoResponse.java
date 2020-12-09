package response;

import java.io.PrintWriter;
import java.util.Scanner;

import data.EResponse;
import interaction.IResponse;

public final class EchoResponse implements IResponse {
	
	@Override
	public EResponse response(String[] params, Scanner reader, PrintWriter writer) {

		for (String param : params) {
			
			writer.println(param);
		}
		
		return EResponse.ECHO_OK;
	}
}
