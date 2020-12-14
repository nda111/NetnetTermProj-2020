package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import org.json.simple.JSONObject;

import app.Client;
import app.Server;
import app.ServerResponser;
import data.EResponse;
import data.User;
import interaction.IResponse;


public final class WhoAmIResponse implements IResponse {

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {
		
		User user = responser.getMeOrNull();
		
		if( user != null) {
			writer.println(EResponse.WHO_AM_I_OK.getValue());
			
			String uid  = user.uid; 
			
			writer.println(user.toJson().toJSONString());
			
			writer.flush();
			return EResponse.WHO_AM_I_OK;
			
		}else {
			writer.println(EResponse.WHO_AM_I_NO.getValue());
			writer.flush();
			
			return EResponse.WHO_AM_I_NO;
		}
	}

}
