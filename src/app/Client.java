package app;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import gui.SignInWindow;

public class Client {
	
	public static void main(String[] args) {
		
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
		} catch (IOException e) {

			e.printStackTrace();
			
			System.exit(0);
		}
		
		SignInWindow window = new SignInWindow(socket);
		window.setVisible(true);
	}
}
