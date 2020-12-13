package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
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
import util.Weather;

public final class MainWindow extends WindowBase {
	
	private JTextField friendTextField;
	private JButton addFriendButton;
	private JLabel meLabel;
	private JList<User> onlineList;
	private JList<User> offlineList;
	private JLabel weatherLabel;

	@Override
	public void configureWindow() {

		setSize(400, 800);
		setResizable(false);
	}

	@Override
	public void initializeGuiComponents(JPanel root) {

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
		
		weatherLabel = new JLabel("Weather here");
		root.add(weatherLabel, BorderLayout.SOUTH);
		
		//
		// Set values
		//
		updateFriendList();
		Weather weather = new Weather(62, 124, "JSON", System.currentTimeMillis());
		weatherLabel.setText(weather.getDataAsString(1, 10));
	}

	@Override
	public void setGuiEvents() {

		addFriendButton.addActionListener(new ActionListener() {
			
			private User friend = null;

			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				final String friendUid = friendTextField.getText().trim();
				
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
		
		// TODO: 
	}
}
