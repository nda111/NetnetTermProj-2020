package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import app.Server;
import app.ServerResponser;
import data.EResponse;
import data.User;
import interaction.IResponse;

public final class SignUpResponse implements IResponse {

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {
		
		EResponse response = null;
		
		if (User.isUidAvailable(params[0])) { // Available UID
			
			String uid = params[0];
			String email = params[1];
			String password = params[2];
			String name = params[3];
			long birth = Long.parseLong(params[4]);
			
			User user = new User(uid, email, password, name, birth);
			Server.Users.put(uid, user);
			user.tryWriteFile();
			
			response = EResponse.SIGNUP_OK;
		} else { // Fail (UID duplication)
			
			System.out.println("UID duplication error");
			response = EResponse.SIGNUP_ERR;
		}
		
		writer.println(response.getValue());
		writer.flush();
		
		return response;
	}
}
