package app;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import data.User;
import gui.SignInWindow;
import interaction.RequestBase;

public class Client {
	
	public static final HashMap<String, User> Friends = new HashMap<>();
	
	public static final HashSet<String> FriendsIn = new HashSet<>();
	
	public static User Me = null;
	
	public static void main(String[] args) {
		
		// For MacOS
		System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");
		
		Socket socket = null;
		String host = null;
		int port = -1;

		try {

			File file = new File("./server_info.txt");			
			Scanner scanner = new Scanner(file);

			host = scanner.nextLine();
			port = Integer.parseInt(scanner.nextLine());
			scanner.close();
			
			socket = new Socket(host, port);
			RequestBase.tryInitSocket(socket);
		} catch (IOException e) {

			e.printStackTrace();
			
			System.exit(0);
		}
		
		SignInWindow window = new SignInWindow(socket);
		window.setVisible(true);
	}
}
