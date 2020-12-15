package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import app.Client;
import data.ERequest;
import data.EResponse;
import data.User;
import gui.dialog.EditInfoDialog;
import gui.dialog.UserInfoDialog;
import interaction.RequestBase;
import util.Weather;

public final class MainWindow extends WindowBase {

	private JTextField friendTextField;
	private JButton addFriendButton;
	private JButton editButton;
	private JLabel meLabel;
	private JList<String> onlineList;
	private JList<String> offlineList;
	private JLabel weatherLabel;
	
	private JPopupMenu contextMenu;
	private JMenuItem askChatItem;
	private JMenuItem showInfoItem;

	private ArrayList<User> onlines;
	private ArrayList<User> offlines;
	private DefaultListModel<String> onlineModel;
	private DefaultListModel<String> offlineModel;
	
	private User selectedFriend = null;
  
	@Override
	public void configureWindow() {

		setSize(400, 800);
		setResizable(false);
	}

	@Override
	public void initializeGuiComponents(JPanel root) {

		//
		// Top Container
		// friendTextField
		// addFriendbutton
		// meLabel
		//
		BorderLayout topLayout = new BorderLayout();
		JPanel topContainer = new JPanel(topLayout);
		topContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

		// friendTextField
		friendTextField = new JTextField();
		topContainer.add(friendTextField, BorderLayout.CENTER);

		// addFriendButton
		addFriendButton = new JButton("Add");
		topContainer.add(addFriendButton, BorderLayout.EAST);
		
		// nameContainer
		BorderLayout nameLayout = new BorderLayout();
		JPanel nameContainer = new JPanel(nameLayout);
		nameContainer.setBorder(BorderFactory.createEmptyBorder(5, 0, 3, 0));
		topContainer.add(nameContainer, BorderLayout.SOUTH);
		
		// editButton
		editButton = new JButton("Edit");
		nameContainer.add(editButton, BorderLayout.WEST);

		// meLabel
		meLabel = new JLabel("Me");
		meLabel.setBackground(Color.WHITE);
		nameContainer.add(meLabel, BorderLayout.CENTER);

		//
		// Fill Container
		// onlineList
		// offlineList
		//
		GridLayout fillLayout = new GridLayout(0, 1);
		JPanel centerContainer = new JPanel(fillLayout);
		centerContainer.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

		// onlineList
		onlines = new ArrayList<>();
		onlineModel = new DefaultListModel<String>();
		onlineList = new JList<String>(onlineModel);
		onlineList.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 2, 5), BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)));
		onlineList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		centerContainer.add(onlineList);

		// offlineList
		offlines = new ArrayList<>();
		offlineModel = new DefaultListModel<String>();
		offlineList = new JList<String>(offlineModel);
		offlineList.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 5, 5, 5), BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)));
		offlineList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		centerContainer.add(offlineList);

		//
		// Root Container
		// Top Container
		// Fill Container
		//
		BorderLayout rootLayout = new BorderLayout();
		root.setLayout(rootLayout);
		root.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

		root.add(topContainer, BorderLayout.NORTH);
		root.add(centerContainer, BorderLayout.CENTER);

		Weather weather = new Weather(62, 124, "JSON", System.currentTimeMillis());
		weatherLabel = new JLabel("Weather here");
		weatherLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
		weatherLabel.setText(weather.getDataAsHtml(1, 10));
		root.add(weatherLabel, BorderLayout.SOUTH);
		
		//
		// Context menu
		//
		contextMenu = new JPopupMenu();
		askChatItem = new JMenuItem("Ask Chat");
		showInfoItem = new JMenuItem("Show informations");
		
		contextMenu.add(askChatItem);
		contextMenu.add(showInfoItem);

		onlineList.setComponentPopupMenu(contextMenu);
		offlineList.setComponentPopupMenu(contextMenu);

		//
		// Set values
		//
		// Send a request to WhoAmI response and wait for a response
		new RequestBase(ERequest.WHOAMI, new String[0]) {

			@Override
			protected void handle(EResponse response, Scanner reader, PrintWriter writer) {

				switch (response) {

				case WHO_AM_I_OK: // Sucess to get user name
					String jsonStr = "";
					while (jsonStr.length() == 0) {
						
						jsonStr = reader.nextLine();
					}

					User me = User.parseJsonOrNull(jsonStr);
					if (me != null) {

						Client.Me = me;
						meLabel.setText(me.name);
					}
					break;

				case WHO_AM_I_NO: // Fail to get user name
					System.out.println("Unknown Error: Please try again.");
					System.exit(0);
					break;
				}
			}
		}.request();
		
		// Send a request to AskFriend response and wait for a response
		new RequestBase(ERequest.ASK_FRIEND, new String[0]) {

			@Override
			protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
				
				switch (response) {
				
				case ASK_FRIEND_OK: // Friend existence
					while (true) {
						
						// Get the friends list
						String json = "";
						while (json.length() == 0) {
							
							json = reader.nextLine();
						}
						if (json.equals("end")) {
							
							break;
						}
						
						boolean signedIn = Boolean.parseBoolean(reader.nextLine());
						User user = User.parseJsonOrNull(json);
						
						Client.Friends.put(user.uid, user);
						if (signedIn) {
						
							Client.FriendsIn.add(user.uid.trim());
						}
					}

					updateFriendList();
					break;
					
				case ASK_FRIEND_NO: // No friends
				default:
					JOptionPane.showMessageDialog(null, "Unknown Error: Please try again.");
					System.exit(0);
					break;
				}
			}
		}.request();
	}

	@Override
	public void setGuiEvents() {

		addFriendButton.addActionListener(new ActionListener() {

			private User friend = null;

			@Override
			public void actionPerformed(ActionEvent arg0) {

				final String uid = friendTextField.getText().trim();
				final String friendUid = friendTextField.getText().trim();

				// Send a request to AddFriend response and wait for a response
				new RequestBase(ERequest.ADD_FRIEND, new String[] { friendUid }) {

					@Override
					protected void handle(EResponse response, Scanner reader, PrintWriter writer) {

						switch (response) {

						case ADD_FRIEND_OK: // Success to add friend
							reader.nextLine();
							friend = User.parseJsonOrNull(reader.nextLine());
							boolean signedIn = Boolean.parseBoolean(reader.nextLine());

							Client.Friends.put(friend.uid, friend);
							if (signedIn) {
								
								Client.FriendsIn.add(friend.uid.trim());
							}
							updateFriendList();

							friendTextField.setText("");
							break;

						case ADD_FRIEND_ERR_YOU: // Failed(Add yourself as a friend)
							JOptionPane.showMessageDialog(null, "It's you idiot.");
							friendTextField.selectAll();
							friendTextField.grabFocus();
							break;

						case ADD_FRIEND_ERR_UID: // Failed(non-exist user)
							JOptionPane.showMessageDialog(null, "There's no such user: " + friendUid + ".");
							friendTextField.selectAll();
							friendTextField.grabFocus();
							break;

						case ADD_FRIEND_ERR_ALREADY: // Failed(Already friend)
							JOptionPane.showMessageDialog(null, "You're already a friend of " + friendUid + ".");
							friendTextField.selectAll();
							friendTextField.grabFocus();
							break;

						case ADD_FRIEND_ERR: // Fail
						default:
							JOptionPane.showMessageDialog(null, "Unknown Error: Please try again.");
							break;
						}
					}
				}.request();
			}
		});
		
		final MouseListener listMouseListener = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

				final JList<String> sender = (JList<String>) e.getSource();
				final int index = sender.locationToIndex(e.getPoint());

				ArrayList<User> users = null;
				if (sender.equals(onlineList)) {

					users = onlines;
					offlineList.clearSelection();
				} else {

					users = offlines;
					onlineList.clearSelection();
				}

				if (0 <= index && index < users.size()) {

					final User user = users.get(index);

					selectedFriend = user;
					
					askChatItem.setEnabled(sender.equals(onlineList));
					showInfoItem.setEnabled(true);
				} else {
					
					selectedFriend = null;
					
					askChatItem.setEnabled(false);
					showInfoItem.setEnabled(false);
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		};
		onlineList.addMouseListener(listMouseListener);
		offlineList.addMouseListener(listMouseListener);
		
		askChatItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (selectedFriend != null) {
					
					askChat(selectedFriend.uid);
				}
			}
		});
		
		showInfoItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (selectedFriend != null) {
					
					MainWindow.this.setEnabled(false);
					UserInfoDialog dialog = new UserInfoDialog(MainWindow.this, selectedFriend);
					dialog.setVisible(true);
				}
			}
		});
		
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				MainWindow.this.setEnabled(false);
				EditInfoDialog dialog = new EditInfoDialog(MainWindow.this);
				dialog.addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent e) {

						if (dialog.newName != null) {
							
							Client.Me.name = dialog.newName;
							MainWindow.this.meLabel.setText(dialog.newName);
						}
						if (dialog.newMessage != null) {
							
							Client.Me.personalMessage = dialog.newMessage;
						}
					}
				});
				dialog.setVisible(true);
			}
		});
	}

	@Override
	public void handleAnnouncement(EResponse response, String[] params) {

		super.handleAnnouncement(response, params);

		switch (response) {

		case ANNOUNCE_ADD_FRIEND:
		case ANNOUNCE_FRIEND_IN:
		case ANNOUNCE_FRIEND_OUT:
		case ANNOUNCE_EDIT_INF:
			updateFriendList();
			break;
        
			//when  ANNOUNCE_ASK_CHAT occurs
		case ANNOUNCE_ASK_CHAT:
			final String fUid = params[0].trim();
			final String fName = params[1];
			final Boolean bAccept = JOptionPane.showConfirmDialog(this, 
					fName + " want's to talk to you.\nWill you accept?", "Chat", 
					JOptionPane.YES_NO_OPTION) == 0; //message box occurs whether approve or not

			//response for ACK_CAHT
			new RequestBase(ERequest.ACK_CHAT, new String[] { fUid, bAccept.toString() }) { //(message from whom, whether accept or not)

				@Override
				protected void handle(EResponse response, Scanner reader, PrintWriter writer) {

					switch (response) {
					
					//if online, and didn't go out from chatting room, ACK_CHAT_OK 
					case ACK_CHAT_OK:
						if (bAccept) {
							
							//Uid, Chatting Room num is received and chatting window starts
							ChatWindow chatWin = new ChatWindow(fUid.trim(), reader.nextInt());
							switchWindow(chatWin, false);
						}
						break;
						
					case ACK_CHAT_LATE:
						JOptionPane.showMessageDialog(null, "The opponent has left.");
						break;
						
					default:
						JOptionPane.showMessageDialog(null, "Unknown Error");
						break;
					}
				}
			}.request();
			break;
        
		default:
			break;
		}
	}

	@Override
	public void onBackFromChild() {

		updateFriendList();
	}

	private void updateFriendList() {

		onlineModel.removeAllElements();
		offlineModel.removeAllElements();
				
		onlines.clear();
		offlines.clear();

		for (Map.Entry<String, User> friend : Client.Friends.entrySet()) {
			
			String uid = friend.getKey();
			User user = friend.getValue();

			if (Client.FriendsIn.contains(uid)) {

				onlines.add(user);
				onlineModel.addElement(user.name + " (" + user.uid + ")");
			} else {

				offlines.add(user);
				offlineModel.addElement(user.name + " (" + user.uid + ")");
			}
		}

		onlineList.setModel(onlineModel);
		offlineList.setModel(offlineModel);
  }
	
	
	private void askChat(String fUid) {
		
		if (Client.FriendsIn.contains(fUid)) { //Uid of someone who wants to talk with 
			
			ChatWindow chatWin = new ChatWindow(fUid); //chat window executes
			switchWindow(chatWin, false);
		} else {

			JOptionPane.showMessageDialog(null, "The opponent is offline.");
		}
	}
}
