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
		
		final String askerUid = params[0];
		final String bAcceptStr = params[1];
		final boolean bAccept = Boolean.parseBoolean(bAcceptStr);
		
		final User me = responser.getMeOrNull();
		final Room room = new Room(askerUid, me.uid);
		
		if (me != null && Server.PendingChats.getOrDefault(askerUid, null) == me.uid) {
			
			Server.PendingChats.remove(askerUid);
		
			final PrintWriter fWriter = Server.Announcers.getOrDefault(askerUid, null);
			if (fWriter != null) {
				
				fWriter.print(EResponse.ANNOUNCE_ACK_CHAT.getValue());
				fWriter.print(' ');
				
				fWriter.print(2);
				fWriter.print(' ');
				
				fWriter.println(bAcceptStr);
				if (bAccept) {
					
					Server.ChatRooms.put(room.hashCode(), room);
					fWriter.println(room.hashCode());
				} else {
					
					fWriter.println(0);
				}
				fWriter.flush();
				
				response = EResponse.ACK_CHAT_OK;
			}
		} 
		
		writer.println(response.getValue());
		if (bAccept && response == EResponse.ACK_CHAT_OK) {
			
			writer.println(room.hashCode());
		}
		
		writer.flush();
		
		return response;
	}
}
