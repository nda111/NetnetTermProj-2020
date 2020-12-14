package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;

import app.Server;
import app.ServerResponser;
import data.EResponse;
import data.Room;
import data.User;
import interaction.IResponse;

public final class ByeChatResponse implements IResponse {

	@Override
	public EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer) {

		EResponse response = EResponse.END_CHAT_OK;

		if (params.length != 0 && !params[0].equals("null")) {

			final int roomId = Integer.parseInt(params[0]);

			final User me = responser.getMeOrNull();
			if (me == null) {

				response = EResponse.END_CHAT_ERR;
			} else {

				final Room room = Server.ChatRooms.getOrDefault(roomId, null);
				if (room != null) {

					Server.ChatRooms.remove(roomId);

					final String fUid = room.opponentOf(me.uid);
					final PrintWriter fWriter = Server.Announcers.getOrDefault(fUid, null);
					if (fWriter != null) {

						fWriter.print(EResponse.ANNOUNCE_END_CHAT.getValue());
						fWriter.print(' ');

						fWriter.print(0);
						fWriter.print(' ');

						fWriter.flush();
					}
				}
			}
		}

		writer.println(response.getValue());
		writer.flush();

		return response;
	}
}
