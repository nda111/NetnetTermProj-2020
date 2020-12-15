package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import app.ServerResponser;
import data.EResponse;
import data.User;
import interaction.IResponse;

public final class WhoAmIResponse implements IResponse {

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {

		User user = responser.getMeOrNull();

		//if there is user, put values to  the user information and 
		if (user != null) {

			writer.println(EResponse.WHO_AM_I_OK.getValue());
			writer.println(user.toJson().toJSONString());

			writer.flush();
			return EResponse.WHO_AM_I_OK; // Success to get User's name

		} else {//if not, falied to put the user information
			
			writer.println(EResponse.WHO_AM_I_NO.getValue());
			writer.flush();

			return EResponse.WHO_AM_I_NO; // Fail to get User's name
		}
	}

}
