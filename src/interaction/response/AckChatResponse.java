package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import app.Server;
import app.ServerResponser;
import data.EResponse;
import data.Room;
import data.User;
import interaction.IResponse;


public final class AckChatResponse implements IResponse{

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {

		EResponse response = EResponse.ACK_CHAT_LATE;
		
		final String askerUid = params[0].trim(); //chat reqeustor (asker)userid
		final String bAcceptStr = params[1].trim(); //sent string from asker
		final boolean bAccept = Boolean.parseBoolean(bAcceptStr); //whether message is sent or not 
		
		final User me = responser.getMeOrNull();
		final Room room = new Room(askerUid, me.uid);
		
		//If the chat asker ID is your own ID or an empty value,
		if (me != null && Server.PendingChats.getOrDefault(askerUid, null).equals(me.uid)) {
			
			Server.PendingChats.remove(askerUid); //remove from the list where requestor-receiver pair for chat rooms awaiting acceptance/rejection
		
			
			final PrintWriter fWriter = Server.Announcers.getOrDefault(askerUid, null);
			if (fWriter != null) {
				
				fWriter.print(EResponse.ANNOUNCE_ACK_CHAT.getValue());
				fWriter.print(' ');
				
				fWriter.print(2);
				fWriter.print(' ');
				
				fWriter.println(bAcceptStr);
				if (bAccept) {
					//Put a pair of chat rooms in the hashmap containing the room value that contains who is participating in the chat room (by int type).
					Server.ChatRooms.put(room.hashCode(), room);
					fWriter.println(room.hashCode()); //send  id  in room to who requested
				} else {
					
					fWriter.println(0);
				}
				fWriter.flush();
				
				response = EResponse.ACK_CHAT_OK;
			}
		} 
		
		//send  id  in room to whom requested
		writer.println(response.getValue());
		
		//if message sent boolean value is same as same as chat accepted message
		if (bAccept && response == EResponse.ACK_CHAT_OK) {
			
			writer.println(room.hashCode()); //send room hashcode
		}
		
		writer.flush();
		
		return response;
	}
}
