package app;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import data.ERequest;
import data.Room;
import data.User;
import interaction.IResponse;
import interaction.response.AckChatResponse;
import interaction.response.AddFriendResponse;
import interaction.response.AskChatResponse;
import interaction.response.AskFriendResponse;
import interaction.response.AskUidResponse;
import interaction.response.ByeChatResponse;
import interaction.response.EchoResponse;
import interaction.response.QuitResponse;
import interaction.response.SayChatResponse;
import interaction.response.SignInResponse;
import interaction.response.SignOutResponse;
import interaction.response.SignUpResponse;
import interaction.response.ValidateUidResponse;


public class Server {
	
	public static final HashMap<ERequest, IResponse> Responses = new HashMap<>(); // ���� ����
	
	public static final HashMap<String, User> Users = new HashMap<>(); // ����� ����Ʈ
	
	public static final HashMap<String, PrintWriter> Announcers = new HashMap<>(); // ��½�Ʈ�� 
	
	public static final HashMap<String, String> PendingChats = new HashMap<>();	// ����/���� ��� ���� ä�ù��� ��û��-������ �� 
	public static final HashMap<Integer, Room> ChatRooms = new HashMap<>(); // ��ȭ ���� ä�ù� 
	
	public static ServerSocket server = null;
	
	public static void main(String[] args) {
		
		initResponse();
		loadUserInfo();
		
		try {

			// ��Ʈ�ѹ��� �޾ƿ´�
			String Port = null;
			File file = new File("./server_info.txt");
			Scanner scanner = new Scanner(file);
			scanner.nextLine();
			int port = Integer.parseInt(scanner.nextLine());
			scanner.close();

			server = new ServerSocket(port);
			System.out.println("�����غ�Ϸ�");

			while (true) {

				Socket client = server.accept();
				
				ServerResponser handler = new ServerResponser(client); // ������ ����
				Thread thread = new Thread(handler);
				thread.start(); // ������ ����
			} // while
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	private static void initResponse() {
		
		// Simple Echo request
		Responses.put(ERequest.ECHO, new EchoResponse());
		
		// Quit request
		Responses.put(ERequest.QUIT, new QuitResponse());
		
		// ValidateUid, SignUp, SignIn, SignOut
		Responses.put(ERequest.VALIDATE_UID, new ValidateUidResponse());
		Responses.put(ERequest.SIGNUP, new SignUpResponse());
		Responses.put(ERequest.SIGNIN, new SignInResponse());
		Responses.put(ERequest.SIGNOUT, new SignOutResponse());
		Responses.put(ERequest.ASK_UID, new AskUidResponse());
		Responses.put(ERequest.ASK_FRIEND, new AskFriendResponse());
		
		// Friend
		Responses.put(ERequest.ADD_FRIEND, new AddFriendResponse());
		
		// Chat
		Responses.put(ERequest.ASK_CHAT, new AskChatResponse());
		Responses.put(ERequest.ACK_CHAT, new AckChatResponse());
		Responses.put(ERequest.SAY_CHAT, new SayChatResponse());
		Responses.put(ERequest.END_CHAT, new ByeChatResponse());
	}
	
	// Load all user info from files before the server starts.
	private static void loadUserInfo() {
		
		File dir = new File(User.UsersPath);
		File[] userFiles = dir.listFiles();
		
		if (userFiles != null) {
				
			for (File file : userFiles) { // For all user info files in the directory,
				
				try {
					
					Scanner reader = new Scanner(file);
					StringBuilder json = new StringBuilder();
					
					while (reader.hasNextLine()) { // read all lines from the file.
						
						json.append(reader.nextLine());
						json.append('\n');
					}
					reader.close();
					
					User user = User.parseJsonOrNull(json.toString()); // Then try parse as a JSON object
					if (user == null) { // If falied to parse,
						
						throw new Exception("Failed to load user"); // Throw an exception.
					} else { // Otherwise,
						
						Users.put(user.uid, user); // Put a new user information to the map.
					}
					
				} catch (Exception e) {
	
					e.printStackTrace();
				}
			}
		}
	}
}
