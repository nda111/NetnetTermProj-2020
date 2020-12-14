package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import app.Client;
import data.ERequest;
import data.EResponse;
import data.User;
import interaction.RequestBase;

public final class MainWindow extends WindowBase {

	private JTextField friendTextField;
	private JButton addFriendButton;
	private JLabel meLabel;
	private JList<String> onlineList;
	private JList<String> offlineList;

	private ArrayList<User> onlines;
	private ArrayList<User> offlines;
	private DefaultListModel<String> onlineModel;
	private DefaultListModel<String> offlineModel;
	
	public MainWindow() {
		
		super();
	}

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

		// meLabel
		meLabel = new JLabel("Me");
		// meLabel.setText(Client.Me);
		meLabel.setBackground(Color.WHITE);
		meLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 3, 0));
		topContainer.add(meLabel, BorderLayout.SOUTH);

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

		//
		// Set values
		//
		new RequestBase(ERequest.WHOAMI, new String[0]) {

			@Override
			protected void handle(EResponse response, Scanner reader, PrintWriter writer) {

				switch (response) {

				case WHO_AM_I_OK:
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

				case WHO_AM_I_NO:
					System.out.println("Unknown Error: Please try again.");
					System.exit(0);
					break;
				}
			}
		}.request();
		
		new RequestBase(ERequest.ASK_FRIEND, new String[0]) {

			@Override
			protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
				
				switch (response) {
				
				case ASK_FRIEND_OK:
					while (true) {
						
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
					
				case ASK_FRIEND_NO:
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

				new RequestBase(ERequest.ADD_FRIEND, new String[] { friendUid }) {

					@Override
					protected void handle(EResponse response, Scanner reader, PrintWriter writer) {

						switch (response) {

						case ADD_FRIEND_OK:
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

						case ADD_FRIEND_ERR_YOU:
							JOptionPane.showMessageDialog(null, "It's you idiot.");
							friendTextField.selectAll();
							friendTextField.grabFocus();
							break;

						case ADD_FRIEND_ERR_UID:
							JOptionPane.showMessageDialog(null, "There's no such user: " + friendUid + ".");
							friendTextField.selectAll();
							friendTextField.grabFocus();
							break;

						case ADD_FRIEND_ERR_ALREADY:
							JOptionPane.showMessageDialog(null, "You're already a friend of " + friendUid + ".");
							friendTextField.selectAll();
							friendTextField.grabFocus();
							break;

						case ADD_FRIEND_ERR:
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
				} else {

					users = offlines;
				}

				if (0 <= index && index < users.size()) {

					final User user = users.get(index);

					if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) { // right clicked

						// TODO: Show information
						JOptionPane.showMessageDialog(null, "Show info: " + user.uid);
					} else if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) { // left double-clicked

						if (sender.equals(onlineList)) {

							JOptionPane.showMessageDialog(null, "Ask chat: " + user.uid);
							// TODO: Ask chat

						}
					}
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
	}

	@Override
	public void handleAnnouncement(EResponse response, String[] params) {

		super.handleAnnouncement(response, params);

		switch (response) {

		case ANNOUNCE_ADD_FRIEND:
		case ANNOUNCE_FRIEND_IN:
		case ANNOUNCE_FRIEND_OUT:
			updateFriendList();
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
}
