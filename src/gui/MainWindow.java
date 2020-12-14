package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import app.Client;
import data.ERequest;
import data.EResponse;
import data.User;
import interaction.RequestBase;

public final class MainWindow extends WindowBase {
	public MainWindow() {
	}
	
	private JTextField friendTextField;
	private JButton addFriendButton;
	private JLabel meLabel;
	private JList<User> onlineList;
	private JList<User> offlineList;
	
	private ArrayList<User> onlines = null;
	private ArrayList<User> offlines = null;

	@Override
	public void configureWindow() {

		setSize(400, 800);
		setResizable(false);
	}

	@Override
	public void initializeGuiComponents(JPanel root) {
		
		/*
		 * TODO:
		 * 
		 * 	1. Create WhoAmIRequest-Response
		 * 	2. Ask who am I, put the info on the meLabel
		 * 	3. Ask my friend list using AskFriendResponse (reference AskFriendResponse.java)
		 * 	4. Add some event listeners to the onlineList so that you can double-click to request chat
		 * 	5. A confirm dialog must be popped up when you double-click your online friend to request chat
		 *  6. Fill updateFriendList() method up.
		 * 	7. Create a pull request
		 * 
		 ********************************************************************
		 * 	Make sure you write a proper commit message for every commits.	*
		 * 	Description for commit can be omitted if not necessary. 		*
		 ********************************************************************
		 *
		 */

		//
		// Top Container
		//		friendTextField
		//		addFriendbutton
		//		meLabel
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
		//meLabel.setText(Client.Me);
		meLabel.setBackground(Color.WHITE);
		meLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 3, 0));
		topContainer.add(meLabel, BorderLayout.SOUTH);
		
		//
		// Fill Container
		//		onlineList
		//		offlineList
		//
		GridLayout fillLayout = new GridLayout(0, 1);
		JPanel centerContainer = new JPanel(fillLayout);
		centerContainer.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
		
		// onlineList
		onlineList = new JList<>();
		onlineList.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(5, 5, 2, 5), 
				BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)));
		centerContainer.add(onlineList);
		
		// offlineList
		offlineList = new JList<>();
		offlineList.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(2, 5, 5, 5), 
				BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)));
		centerContainer.add(offlineList);
		
		//
		// Root Container
		//		Top Container
		//		Fill Container
		//
		BorderLayout rootLayout = new BorderLayout();
		root.setLayout(rootLayout);
		root.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
		
		root.add(topContainer, BorderLayout.NORTH);
		root.add(centerContainer, BorderLayout.CENTER);
		
		//
		// Set values
		//
		updateFriendList();
	}

	@Override
	public void setGuiEvents() {

		addFriendButton.addActionListener(new ActionListener() {
			
			private User friend = null;

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				
				System.out.println(Client.Me);
			
				final String uid = friendTextField.getText().trim();
				final String friendUid = friendTextField.getText().trim();
				
				new RequestBase(ERequest.WHOAMI, new String[0]) {

					@Override
					protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
						
						switch (response) {
						
						case WHO_AM_I_OK:	
							String jsonStr = reader.nextLine();
							
							User me = User.parseJsonOrNull(jsonStr);
							
							if (me != null) {
								
								Client.Me = me;
								
								break;
							}
							
						case WHO_AM_I_NO:
						default:
							JOptionPane.showMessageDialog(null, "Unknown Error: Please try again.");
						}
					}
				}.request();
							
				new RequestBase(ERequest.ADD_FRIEND, new String[] { friendUid }) {

					@Override
					protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
						
						switch (response) {
						
						case ADD_FRIEND_OK:
							reader.nextLine();
							friend = User.parseJsonOrNull(reader.nextLine());
							
							Client.Friends.put(friend.uid, friend);
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
		
		onlineList.removeAll();
		offlineList.removeAll();
		
		onlines = new ArrayList<>();
		offlines = new ArrayList<>();
		ArrayList<String> onlineNames = new ArrayList<>();
		ArrayList<String> offlinesNames = new ArrayList<>();
		
		for( Map.Entry<String, User> friend : Client.Friends.entrySet() ){
			String uid = friend.getKey();
			User user = friend.getValue();

			if (Client.FriendsIn.contains(uid)) {

				// 온라인에 추가
				onlines.add(user);
				
				onlineNames.add(user.name);
			}
			else {

				// 오프라인에 추가
				offlines.add(user);
				
				offlinesNames.add(user.name);
				
			}
		}	
		
	}
}
