package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import app.Server;
import app.ServerResponser;
import data.EResponse;
import data.User;
import interaction.IResponse;

public class AskFriendResponse implements IResponse {

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {

		User user = responser.getMeOrNull();
		
		if (user != null) {
		
			writer.println(EResponse.ASK_FRIEND_OK.getValue());
			
			writer.println(user.friends.size());
			for (String uid : user.friends) {
				
				User friend = Server.Users.getOrDefault(uid, null);
				if (friend != null) {
					
					writer.println(friend.toJson().toJSONString().replace('\n', ' ')); 
				}
			}
			
			return EResponse.ASK_FRIEND_OK;
		} else {
			
			writer.println(EResponse.ASK_FRIEND_NO.getValue());
			
			return EResponse.ASK_FRIEND_NO;
		}
	}
}
