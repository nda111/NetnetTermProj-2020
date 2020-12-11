package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import app.Server;
import app.ServerResponser;
import data.EResponse;
import data.User;
import interaction.IResponse;

public final class AskUidResponse implements IResponse {

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {

		String email = params[0];
		User user = null;
		
		for (User u : Server.Users.values()) {
			
			if (u.email.equals(email)) {
				
				user = u;
				break;
			}
		}
		
		EResponse response = null;
		if (user == null) {
			
			response = EResponse.ASK_UID_NO;
			writer.println(response.getValue());
		} else {
			
			response = EResponse.ASK_UID_OK;
			writer.println(response.getValue());
			writer.println(user.uid);
		}
		writer.flush();
		
		return response;
	}
}
