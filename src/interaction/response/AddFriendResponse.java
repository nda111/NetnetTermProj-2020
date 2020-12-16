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

		if (me == null) {//if there is no one added in friends list
			
			response = EResponse.ADD_FRIEND_ERR; //error
		} else if (me.uid.equals(friendUid)) { //if it is same as friend userid
			
			response = EResponse.ADD_FRIEND_ERR_YOU; //add own's friend
		} else if (!Server.Users.containsKey(friendUid)) { //if there is no friend user id in users list
			
			response = EResponse.ADD_FRIEND_ERR_UID; //no friend
		} else if (me.friends.contains(friendUid)) { // if there is friend user id in users
			
			response = EResponse.ADD_FRIEND_ERR_ALREADY;//friends already in
		} else { //in other cases,

			
			response = EResponse.ADD_FRIEND_OK;//success in adding friend
			
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

		//In case if success in adding friends
		if (response == EResponse.ADD_FRIEND_OK) {
			

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
			//In case that here is friend user id 
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
