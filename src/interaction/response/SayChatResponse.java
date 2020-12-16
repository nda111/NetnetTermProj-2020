package interaction.response;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		final int roomId = Integer.parseInt(params[0]); //roomID
		final long now = Long.parseLong(params[1]); //Time
		final String text = params[2]; //message for texting
		
		final User me = responser.getMeOrNull(); //for storing sender
		final Room room = Server.ChatRooms.getOrDefault(roomId, null); 
		if (room != null) { //if abnormal access occurs
			
			if (me != null && room.isIn(me.uid)) { //Case in chatting room, two participants 
			
				final PrintWriter[] writers = new PrintWriter[] {
					//bring inputstream	
					Server.Announcers.getOrDefault(room.uid1, null),
					Server.Announcers.getOrDefault(room.uid2, null),
				};
				if (writers[0] != null && writers[1] != null) { //if available
					
					//string which will be printed
					final StringBuilder lineBuilder = new StringBuilder();
					lineBuilder.append(DateTime.millisToShortTimeString(now) + ' ');
					lineBuilder.append(me.name + ": ");

					//Regex Expression for caculator in chatting
					final String MathExpressionRegex = "([+-]?([0-9]*[.])?[0-9]+)[\\+\\-\\*\\/](([+-]?([0-9]*[.])?[0-9]+))$";
					EResponse peerResponse = null;
					//if matches with expression, do calculation function
					if (text.matches(MathExpressionRegex)) {
						
						Pattern numPattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+"); //expression for extracting number
						Matcher numMatcher = numPattern.matcher(text);

						numMatcher.find();
						String firstStr = numMatcher.group();
						double first = Double.parseDouble(firstStr); //first number
						numMatcher.find();
						double second = Double.parseDouble(numMatcher.group()); //second number
						char operation = text.charAt(firstStr.length());
						
						double result = -1;
						switch (operation) {
						
						case '+':
							result = first + second;
							break;
							
						case '-':
							result = first - second;
							break;
							
						case '*':
							result = first * second;
							break;
							
						case '/':
							if (second != 0) { //if Number divided is 0
								
								result = first / second;
							} else {
								
								result = Double.NaN;
							}
							break;
							
						default: //if unknown operator
							result = Double.NaN;
							break;
						}
						lineBuilder.append(first + " ");
						lineBuilder.append(operation + " ");
						lineBuilder.append(second + " = ");
						lineBuilder.append(result);
						
						peerResponse = EResponse.ANNOUNCE_CALC_CHAT;
						
					} else {//if doesn't match with expression, send as string,

						lineBuilder.append(text);	
						peerResponse = EResponse.ANNOUNCE_SAY_CHAT;
					}
					
					final String line = lineBuilder.toString();
					for (PrintWriter cWriter : writers) { 
						
						cWriter.print(peerResponse.getValue());
						cWriter.print(' ');
						
						cWriter.print(1);
						cWriter.print(' ');
						
						cWriter.println(line);
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
