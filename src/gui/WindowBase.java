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
		
		configureWindow();
		initializeGuiComponents((JPanel)this.getContentPane());
		setGuiEvents();
		
		if (announceListener == null) {
		
			new Thread(new Runnable() {
				
				@Override
				public void run() {

					announceListener = new RequestBase(ERequest.ANNOUNCE, new String[0]) {

						@Override
						protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
							
							int numParams = reader.nextInt();
							String[] params = new String[numParams];
							for (int i = 0; i < numParams; i++) {
								
								params[i] = reader.nextLine();
							}
			
							CurrentWindow.handleAnnouncement(response, params);
							
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
		
		matchCenter(win);

		win.setVisible(true);
		setVisible(false);
		
		if (newRoot) {
			
			disposeAll();
			win.parent = null;
		} else {
			
			win.parent = this;
		}
		
		CurrentWindow = win;
	}
	
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
			
			CurrentWindow = this;
		}
	}
	
	public void handleAnnouncement(EResponse response, String[] params) {
		
		String fUid;
		long signOutTime;
		
		switch (response) {
		
		case ANNOUNCE_ADD_FRIEND:
			String json = params[0];
			User user = User.parseJsonOrNull(json);
			Client.Friends.put(user.uid, user);
			Client.FriendsIn.add(user.uid);
			break;
			
		case ANNOUNCE_FRIEND_IN:
			fUid = params[0];
			Client.FriendsIn.add(fUid.trim());
			break;
			
		case ANNOUNCE_FRIEND_OUT:
			fUid = params[0];
			signOutTime = Long.parseLong(params[1]);
			
			//Client.Friends.get(fUid).signOutTime = signOutTime;
			Client.FriendsIn.remove(fUid.trim());
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
