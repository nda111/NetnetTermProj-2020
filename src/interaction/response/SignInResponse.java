package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import app.Server;
import app.ServerResponser;
import data.EResponse;
import data.User;
import interaction.IResponse;

public final class SignInResponse implements IResponse{

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {
		
		EResponse response = null;
		
		String uid = params[0];
		String password = params[1];
		
		User user = Server.Users.getOrDefault(uid, null);
		if (user == null) { 						// No such account
			
			response = EResponse.SIGNIN_ERR_NO_UID;
		} else if (!user.matchPassword(password)) { // Wrong password
			
			response = EResponse.SIGNIN_ERR_PW;
		} else if (user.isSignedIn()){ 				// Multi-Signin attemption
			
			response = EResponse.SIGNIN_ERR_MULTI;
		} else { 									// Success
			
			response = EResponse.SIGNIN_OK;
			
			user.signInBy(responser.getClientAddress());
			responser.setMe(user);
		}
		
		writer.println(response.getValue());
		writer.flush();
		
		return response;
	}

}
