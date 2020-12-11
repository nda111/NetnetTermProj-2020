package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.User;

public final class MainWindow extends WindowBase {
	
	private JTextField friendTextField;
	private JButton addFriendButton;
	private JLabel meLabel;
	private JList<User> onlineList;
	private JList<User> offlineList;

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
		 * 	3. Ask my friend list using Ask FriendResponse (reference FriendResponse.java)
		 * 	4. Add some event listeners to the onlineList so that you can double-click to request chat
		 * 	5. A confirm dialog must be popped up when you double-click your online friend to request chat
		 * 	6. Create a pull request
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
	}

	@Override
	public void setGuiEvents() {

		
	}
}
