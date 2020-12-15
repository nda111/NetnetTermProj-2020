package gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;

import app.Client;
import data.ERequest;
import data.EResponse;
import data.User;
import interaction.RequestBase;

public final class EditInfoDialog extends JDialog {

	private JLabel nameLabel;
	private JTextField nameField;
	private JLabel messageLabel;
	private JTextField messageField;
	private JButton cancelButton;
	private JButton okButton;
	
	public String newName = null;
	public String newMessage = null;

	public EditInfoDialog(JFrame parent) {

		super(parent);

		this.setSize(500, 300);

		Point position = parent.getLocationOnScreen();
		this.setLocation(position.x + (parent.getWidth() - this.getWidth()) / 2, position.y + (parent.getHeight() - this.getHeight()) / 2);

		initGuiComponents((JPanel) this.getContentPane());

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

				close();
			}
		});
	}

	public void close() {

		dispose();
		getParent().setEnabled(true);
	}

	private void initGuiComponents(JPanel root) {

		BoxLayout rootLayout = new BoxLayout(root, BoxLayout.Y_AXIS);
		root.setLayout(rootLayout);
		root.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

		BorderLayout nameLayout = new BorderLayout();
		JPanel namePanel = new JPanel(nameLayout);
		root.add(namePanel);
		
		nameLabel = new JLabel("Nickname: ");
		namePanel.add(nameLabel, BorderLayout.WEST);
		
		nameField = new JTextField(Client.Me.name);
		namePanel.add(nameField, BorderLayout.CENTER);
		
		root.add(Box.createRigidArea(new Dimension(0, 10)));

		BorderLayout messageLayout = new BorderLayout();
		JPanel messagePanel = new JPanel(messageLayout);
		root.add(messagePanel);
		
		messageLabel = new JLabel("Message: ");
		messagePanel.add(messageLabel, BorderLayout.WEST);
		
		messageField = new JTextField(Client.Me.personalMessage == null ? "" : Client.Me.personalMessage);
		messagePanel.add(messageField, BorderLayout.CENTER);
		
		root.add(Box.createRigidArea(new Dimension(0, 10)));

		FlowLayout buttonLayout = new FlowLayout();
		JPanel buttonContainer = new JPanel(buttonLayout);
		root.add(buttonContainer);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setHorizontalAlignment(SwingConstants.CENTER);
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				close();
			}
		});
		buttonContainer.add(cancelButton);
		
		okButton = new JButton("OK");
		okButton.setHorizontalAlignment(SwingConstants.CENTER);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String name = nameField.getText().trim();
				String message = messageField.getText().trim();
				
				if (name.length() != 0 && !name.equals(Client.Me.name)) {
					
					newName = name;
				}
				
				if (!message.equals(Client.Me.personalMessage)) {
					
					newMessage = message;
				}
				
				JSONObject json = new JSONObject();
				json.put("name", name);
				json.put("message", message);
				
				new RequestBase(ERequest.EDIT_INF, new String[] { json.toJSONString() }) {

					@Override
					protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
						
						switch (response) {
						
						case EDIT_INF_OK:
							close();
							break;
							
						case EDIT_INF_ERR:
						default:
							newName = null;
							newMessage = null;
							
							JOptionPane.showMessageDialog(null, "Unknown Error: Please try again.");
							break;
						}
					}
				}.request();
			}
		});
		buttonContainer.add(okButton);
	}
}
