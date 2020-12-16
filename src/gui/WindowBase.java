package gui;


import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import app.Client;
import data.ERequest;
import data.EResponse;
import data.User;
import interaction.RequestBase;

public abstract class WindowBase extends JFrame {

	private static RequestBase announceListener = null;
	public static WindowBase CurrentWindow = null;
	
	private WindowBase parent = null;
	Socket socket;
	Scanner reader;
	PrintWriter writer;

	public WindowBase(Socket socket) {
		
		this.socket = socket;
		if (socket == null) {
			
			reader = null;
			writer = null;
		} else {
			
			try {
				
				reader = new Scanner(socket.getInputStream());
				writer = new PrintWriter(socket.getOutputStream());
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
		}
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {

				if (isRoot()) {
					
					new RequestBase(ERequest.QUIT, new String[0]) {

						@Override
						protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
							
							System.exit(0);
						}
					}.request();
				} else {

					backToParent();
				}
	        }
		});
		
		configureWindow(); //set Window size
		initializeGuiComponents((JPanel)this.getContentPane()); //Add gui component to window 
		setGuiEvents(); //make events for gui component
		
		if (announceListener == null) { 
		
			new Thread(new Runnable() {
				
				@Override
				public void run() {

					announceListener = new RequestBase(ERequest.ANNOUNCE, new String[0]) { 
						//if window is first made, do ANNOUNCE request
						@Override
						protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
							
							int numParams = reader.nextInt();
							String[] params = new String[numParams];
							for (int i = 0; i < numParams; i++) {
								
								params[i] = reader.nextLine();
							}
			
							CurrentWindow.handleAnnouncement(response, params);
							//if request over, do request again
							this.request();
						}
					};
					announceListener.request();
				}
			}).start();
		}
	}
	
	public WindowBase() {
		
		this(null);
	}
	
	private void matchCenter(WindowBase other) {
		
		Rectangle bounds = this.getBounds();
		Dimension winSize = other.getSize();
		other.setLocation(
				bounds.x + (bounds.width - winSize.width) / 2,
				bounds.y + (bounds.height - winSize.height) / 2);
	}
	
	public boolean isRoot() {
		
		return parent == null;
	}
	
	
	public void switchWindow(WindowBase win, boolean newRoot) {
		
		win.socket = this.socket;
		win.reader = this.reader;
		win.writer = this.writer;
		
		matchCenter(win); //Adjust window coordinates

		win.setVisible(true);//Make the new window visible
		setVisible(false); //Make the existing window visible
		
		//If new window starts
		if (newRoot) {
			
			disposeAll(); //dispose previous windows
			win.parent = null;
		} else {
			
			win.parent = this; 
		}
		
		CurrentWindow = win; //Make this window boot
	}
	
	//Go back to parent go back to previous  window
	public void backToParent() {
		
		if (parent != null) {

			matchCenter(parent);

			parent.setVisible(true);
			setVisible(false);
			
			dispose();
			
			CurrentWindow = parent;
			
			parent.onBackFromChild();
		}
	}
	
	public void disposeAll() {

		dispose();
		
		if (parent != null) {
			
			parent.dispose();
		}
	}
	
	@Override
	public void setVisible(boolean b) {
		
		super.setVisible(b);
		
		if (b) {
			
			CurrentWindow = this; //make available to recall HandleAnnounce
		}
	}
	
	
	public void handleAnnouncement(EResponse response, String[] params) {
		
		String fUid;
		long signOutTime;
		User user;
		
		switch (response) {
		
		case ANNOUNCE_ADD_FRIEND:
			String json = params[0];
			user = User.parseJsonOrNull(json);
			Client.Friends.put(user.uid, user);
			Client.FriendsIn.add(user.uid);
			break;
			
		case ANNOUNCE_FRIEND_IN:
			fUid = params[0];
			Client.FriendsIn.add(fUid.trim());
			break;
			
		case ANNOUNCE_FRIEND_OUT:
			fUid = params[0].trim();
			signOutTime = Long.parseLong(params[1]);
			
			Client.Friends.get(fUid).signOutTime = signOutTime;
			Client.FriendsIn.remove(fUid);
			break;
			
		case ANNOUNCE_EDIT_INF:
			fUid = params[0].trim();
			String name = params[1].trim();
			String message = params[2].trim().substring(1);
			
			user = Client.Friends.get(fUid);
			if (user != null) {
				
				user.updateValue(name, message);
			}
			break;
			
		default:
			break;
		}
	}
	
	public void onBackFromChild() {
	
		// Empty
	}
	
	public abstract void configureWindow();
	
	public abstract void initializeGuiComponents(JPanel root);
	
	public abstract void setGuiEvents();
}
