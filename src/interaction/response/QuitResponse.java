package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import app.Server;
import app.ServerResponser;
import data.ERequest;
import data.EResponse;
import interaction.IResponse;

public final class QuitResponse implements IResponse {

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {

		if (responser.getMeOrNull() != null) {
			
			Server.Responses.get(ERequest.SIGNOUT).response(new String[0], responser, reader, null);
		}
		
		writer.println(EResponse.QUIT_OK.getValue());
		writer.flush();
		
		return EResponse.QUIT_OK; // Terminate the program successfully
	}
}
