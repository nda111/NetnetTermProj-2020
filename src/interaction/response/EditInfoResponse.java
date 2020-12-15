package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import app.Server;
import app.ServerResponser;
import data.EResponse;
import data.User;
import interaction.IResponse;
//for editting information of user name, message
public class EditInfoResponse implements IResponse {

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {
		
		EResponse response = EResponse.EDIT_INF_ERR;
		String jsonString = params[0].trim();

		User me = responser.getMeOrNull();
		if (me != null) {
		
			try {
				
				JSONParser parser = new JSONParser(); 
				JSONObject json = (JSONObject)parser.parse(jsonString);
				
				String name = (String)json.getOrDefault("name", null);
				String message = (String)json.getOrDefault("message", null);
				
				if (name != null) {
					
					me.name = name;
				}
				if (message != null) {
					
					me.personalMessage = message;
				}
				if (me.tryWriteFile()) {

					response = EResponse.EDIT_INF_OK;
				}
			} catch (ParseException e) {
	
				e.printStackTrace();
			}
		}
		
		if (response == EResponse.EDIT_INF_OK) {
			
			String name = me.name;
			String message = me.personalMessage;
			
			for (String fUid : me.friends) {
				
				final PrintWriter fWriter = Server.Announcers.get(fUid);
				if (fWriter != null) {
					
					fWriter.print(EResponse.ANNOUNCE_EDIT_INF.getValue());
					fWriter.print(' ');
					
					fWriter.print(3);
					fWriter.print(' ');
					
					fWriter.println(me.uid);
					fWriter.println(name);
					fWriter.println("m" + message);
					fWriter.flush();
				}
			}
		}
		
		writer.println(response.getValue());
		writer.flush();
		
		return response;
	}
}
