package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import app.Server;
import app.ServerResponser;
import data.EResponse;
import interaction.IResponse;


public final class ValidateUidResponse implements IResponse {

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {
		
		EResponse response = null;
		
		final String uid = params[0];

		if (Server.Users.containsKey(uid)) {
			
			response = EResponse.VALIDATE_UID_NO; // Fail (UID duplication)
		} else {
			
			response = EResponse.VALIDATE_UID_OK; // Sucess (unique UID)
		}
		
		writer.println(response.getValue());
		writer.flush();
		
		return response;
	}

}
