package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import app.Server;
import app.ServerResponser;
import data.EResponse;
import data.Room;
import data.User;
import interaction.IResponse;
import util.DateTime;

public final class SayChatResponse implements IResponse {

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {
		
		EResponse response;

		final int roomId = Integer.parseInt(params[0]);
		final long now = Long.parseLong(params[1]);
		final String text = params[2];
		
		final User me = responser.getMeOrNull();
		final Room room = Server.ChatRooms.getOrDefault(roomId, null);
		if (room != null) {
			
			if (me != null && room.isIn(me.uid)) {
			
				final PrintWriter[] writers = new PrintWriter[] {
						
					Server.Announcers.getOrDefault(room.uid1, null),
					Server.Announcers.getOrDefault(room.uid2, null),
				};
				if (writers[0] != null && writers[1] != null) {
					
					final StringBuilder lineBuilder = new StringBuilder();
					lineBuilder.append(DateTime.millisToShortTimeString(now) + ' ');
					lineBuilder.append(me.name + ": ");
					lineBuilder.append(text);
					
					final String line = lineBuilder.toString();
					
					for (PrintWriter cWriter : writers) {
						
						cWriter.print(EResponse.ANNOUNCE_SAY_CHAT.getValue());
						cWriter.print(' ');
						
						cWriter.print(1);
						cWriter.print(' ');
						
						cWriter.print(line);
						cWriter.flush();
					}
					
					response = EResponse.SAY_CHAT_OK;
				} else {

					response = EResponse.SAY_CHAT_ERR;
				}
			} else {
				
				response = EResponse.SAY_CHAT_ERR;
			}
		} else {
			
			response = EResponse.SAY_CHAT_NO_ROOM;
		}
		
		writer.println(response.getValue());
		writer.flush();
		
		return response;
	}

}
