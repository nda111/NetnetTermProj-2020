package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import org.json.simple.JSONObject;

import app.Server;
import app.ServerResponser;
import data.EResponse;
import data.User;
import interaction.IResponse;


public final class AddFriendResponse implements IResponse {

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {
	
		String friendUid = params[0];
		
		User me = responser.getMeOrNull();
		EResponse response = null;
		if (me == null) {//Fail
			
			response = EResponse.ADD_FRIEND_ERR; 
		} else if (me.uid.equals(friendUid)) { // Failed(Add yourself as a friend)
			
			response = EResponse.ADD_FRIEND_ERR_YOU; 
		} else if (!Server.Users.containsKey(friendUid)) { // Failed(non-exist user)
			
			response = EResponse.ADD_FRIEND_ERR_UID; 
		} else if (me.friends.contains(friendUid)) { // Failed(Already friend)
			
			response = EResponse.ADD_FRIEND_ERR_ALREADY;
		} else { // Successfully add friends
			
			response = EResponse.ADD_FRIEND_OK;
			
			me.friends.add(friendUid);
			if (!me.tryWriteFile()) {
				
				System.out.println("WRITE_FAILED: " + me.uid);
			}
			
			User friend = Server.Users.get(friendUid);
			friend.friends.add(me.uid);
			if (!friend.tryWriteFile()) {
				
				System.out.println("WRITE_FAILED: " + friend.uid);
			}
		}
		
		writer.println(response.getValue());
		
		if (response == EResponse.ADD_FRIEND_OK) { // When successfully adding friend
			// In the friend list of both users, enter the name of the relative user
			User fObj = Server.Users.get(friendUid);
			JSONObject friend = fObj.toJson();
			friend.put("password", null);
			friend.put("friends", null);
			
			String jsonString = friend.toJSONString().replace('\n', ' ');
			System.out.println(jsonString);
			writer.println(jsonString);
			writer.println(fObj.isSignedIn());
			writer.flush();
			
			JSONObject meJson = me.toJson();
			meJson.put("password", null);
			meJson.put("friends", null);
			
			jsonString = meJson.toJSONString().replace('\n', ' ');
			if (Server.Announcers.containsKey(friendUid)) {
				
				PrintWriter friendWriter = Server.Announcers.get(friendUid);
				friendWriter.print(EResponse.ANNOUNCE_ADD_FRIEND.getValue());
				friendWriter.print(' ');
				
				friendWriter.print(1);
				friendWriter.print(' ');
				
				friendWriter.println(jsonString);
				
				friendWriter.flush();
			}
		}
		
		return response;
	}
}
