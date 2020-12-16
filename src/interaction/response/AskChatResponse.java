package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import app.Server;
import app.ServerResponser;
import data.EResponse;
import data.User;
import interaction.IResponse;

//For sending whom I want to chat with
public final class AskChatResponse implements IResponse {

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {
	
		EResponse response = null;
		
		final User me = responser.getMeOrNull();
		
		if (me != null) {
			
			final String fUid = params[0];
			final PrintWriter fWriter = Server.Announcers.getOrDefault(fUid, null);
			if (fWriter != null) {
				
				fWriter.print(EResponse.ANNOUNCE_ASK_CHAT.getValue());
				fWriter.print(' ');
				
				fWriter.print(2);
				fWriter.print(' ');
				
				fWriter.println(me.uid); //when accepted, to check whom sending message is coming in
				fWriter.println(me.name); //for who requetsed for message
				fWriter.flush();
				
				Server.PendingChats.put(me.uid, fUid);//requestor uid, receiver uid 
				response = EResponse.ASK_CHAT_OK;
			} else {
				
				response = EResponse.ASK_CHAT_OFFLINE;
			}
		} else {
			
			response = EResponse.ASK_CHAT_ERR;
		}

		writer.println(response.getValue());
		writer.flush();
		
		return response;
	}
}
