package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import app.Client;
import data.ERequest;
import data.EResponse;
import interaction.RequestBase;
import util.DateTime;

public final class ChatWindow extends WindowBase {
	
	private final StyleContext styles = new StyleContext();
	private final Style chatStyle = styles.addStyle("CHAT", null);
	private final Style systemStyle = styles.addStyle("SYSTEM", null);
	private final Style byeStyle = styles.addStyle("BYE", null);
	
	private JTextArea chatBox;
	private JTextField chatTextField;
	private JButton sendButton;
	
	private String roomIdStr = null;
	private boolean isLocked = false;
	
	private int day = -1;
	
	public ChatWindow(String fUid) {
		
		super();
		
		chatStyle.addAttribute(StyleConstants.Foreground, Color.BLACK);
		systemStyle.addAttribute(StyleConstants.Foreground, Color.LIGHT_GRAY);
		systemStyle.addAttribute(StyleConstants.Bold, Boolean.valueOf(true));
		byeStyle.addAttribute(StyleConstants.Foreground, Color.RED);
		
		this.setTitle(Client.Friends.get(fUid).name);
		setChatLocked(true);
		
		new RequestBase(ERequest.ASK_CHAT, new String[] { fUid }) {

			@Override
			protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
				
				switch (response) {
				
				case ASK_CHAT_OK:
					printText("Please wait until the opponent joins.", systemStyle);
					break;
					
				case ASK_CHAT_OFFLINE:
					Client.FriendsIn.remove(fUid);
					JOptionPane.showMessageDialog(null, "Opponent is offline.");
					backToParent();
					break;
					
				case ASK_CHAT_ERR:
				default:
					JOptionPane.showMessageDialog(null, "Unknown Error: Please try again.");
					backToParent();
					break;
				}
			}
		}.request();
	}
	
	public ChatWindow(String fUid, int roomId) {

		this.setTitle(Client.Friends.get(fUid).name);
		this.roomIdStr = Integer.toString(roomId);
		
		printText("\'" + Client.Me.name + "\' joined.", systemStyle);
	}

	@Override
	public void configureWindow() {

		setSize(400, 800);
		setResizable(false);
	}

	@Override
	public void initializeGuiComponents(JPanel root) {

		BorderLayout bottomLayout = new BorderLayout();
		JPanel bottomContainer = new JPanel(bottomLayout);
		
		chatTextField = new JTextField();
		bottomContainer.add(chatTextField, BorderLayout.CENTER);
		
		sendButton = new JButton("Send");
		bottomContainer.add(sendButton, BorderLayout.EAST);
		
		BorderLayout rootLayout = new BorderLayout();
		root.setLayout(rootLayout);
		
		chatBox = new JTextArea();
		chatBox.setEnabled(false);
		root.add(chatBox, BorderLayout.CENTER);
		
		root.add(bottomContainer, BorderLayout.SOUTH);
	}

	@Override
	public void setGuiEvents() {
		
		final ActionListener SendListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				final String text = chatTextField.getText().trim();
				
				if (text.length() != 0) {
				
					sendChat(text);
				}
			}
		};

		chatTextField.addActionListener(SendListener);
		
		sendButton.addActionListener(SendListener);
	}
	
	@Override
	public void handleAnnouncement(EResponse response, String[] params) {
		
		super.handleAnnouncement(response, params);
		
		switch (response) {
		
		case ANNOUNCE_ACK_CHAT:
			
			final boolean bAccept = Boolean.parseBoolean(params[0]);
			final String roomIdStr = params[1];
			if (bAccept) {
				
				this.roomIdStr = roomIdStr;
				printText("\'" + getTitle() + "\' joined.", systemStyle);
				setChatLocked(false);
			} else {
				
				backToParent();
			}
			break;
			
		case ANNOUNCE_SAY_CHAT:
			final String line = params[0];
			printText(line, chatStyle);
			break;
			
		case ANNOUNCE_END_CHAT:
			printText("\'" + getTitle() + "\' has left.", byeStyle);
			setChatLocked(true);
			chatTextField.setText("");
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void backToParent() {
		
		new RequestBase(ERequest.END_CHAT, new String[] { roomIdStr }) {

			@Override
			protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
				
				switch (response) {
				
				case END_CHAT_OK:
					ChatWindow.super.backToParent();
					break;
					
				default:
					JOptionPane.showMessageDialog(null, "Unknown Error: Please try again.");
					break;
				}
			}
		}.request();
	}
	
	private void sendChat(String text) {
		
		if (!isLocked) {
		
			final String nowStr = Long.toString(System.currentTimeMillis());
			
			setChatLocked(true);
			new RequestBase(ERequest.SAY_CHAT, new String[] { roomIdStr, nowStr, text }) {
	
				@Override
				protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
					
					switch (response) {
					
					case SAY_CHAT_OK:
						setChatLocked(false);
						chatTextField.setText("");
						chatTextField.grabFocus();
						break;
						
					case SAY_CHAT_NO_ROOM:
						printText("\'" + getTitle() + "\' has left.", byeStyle);
						setChatLocked(true);
						chatTextField.setText("");
						break;
						
					case SAY_CHAT_ERR:
					default:
						JOptionPane.showMessageDialog(null, "Unknown Error: Please try again.");
						break;
					}
				}
			}.request();
		}
	}
	
	private void printText(String text, Style style) {

		try {
			
			Document doc = chatBox.getDocument();
			
			long todayMillis = System.currentTimeMillis();
			Calendar today = Calendar.getInstance();
			today.setTimeInMillis(todayMillis);
			final int day = today.get(Calendar.DATE);
			
			if (day != this.day) {
				
				this.day = day;
				
				doc.insertString(doc.getLength(), '\n' + DateTime.millisToLongDateString(todayMillis) + "\n\n", systemStyle);
			}
			
			doc.insertString(doc.getLength(), text + '\n', style);
		} catch (BadLocationException e) {
			
			e.printStackTrace();
		}
	}
	
	private void setChatLocked(boolean locked) {
		
		final boolean unlocked = !locked;
		
		isLocked = locked;
		sendButton.setEnabled(unlocked);
		chatTextField.setEnabled(unlocked);
	}
}
