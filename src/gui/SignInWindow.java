package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import app.Client;
import data.ERequest;
import data.EResponse;
import interaction.IResponse;
import interaction.RequestBase;


public final class SignInWindow extends WindowBase {

	private JLabel uidLabel;
	private JTextField uidTextField;
	private JLabel pwLabel;
	private JPasswordField pwTextField;
	private JButton signInButton;
	private JButton signUpButton;

	public SignInWindow(Socket socket) {

		super(socket);
	}

	@Override
	public void configureWindow() {
		
		setSize(300, 600);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	@Override
	public void initializeGuiComponents(JPanel root) {
		
		//
		// ID textField
		// password textField
		// sign-up botton
		// sign-un button
		//
		GridLayout layout = new GridLayout(0, 2);
		layout.setVgap(5);
		root.setLayout(layout);
		root.setBorder(BorderFactory.createEmptyBorder(80, 20, 370, 20));

		// Enter ID
		uidLabel = new JLabel("User", SwingConstants.RIGHT);
		root.add(uidLabel);

		uidTextField = new JTextField(20);
		root.add(uidTextField);

		// Enter password
		pwLabel = new JLabel("Password", SwingConstants.RIGHT);
		root.add(pwLabel);

		pwTextField = new JPasswordField(20);
		root.add(pwTextField);

		// Sign-up button
		signUpButton = new JButton("Sign Up");
		root.add(signUpButton);

		// Sign-in button
		signInButton = new JButton("Sign In");
		root.add(signInButton);
	}

	@Override
	public void setGuiEvents() {
		// If sign-up burron is clicked, go to sign-up window
		signUpButton.addActionListener(new ActionListener() { 
			
			public void actionPerformed(ActionEvent e){
				
				SignUpWindow signUp = new SignUpWindow();
				switchWindow(signUp, false);
			}
		});
		
		//If sign-in button is clicked, 
		signInButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String uid = uidTextField.getText().trim();
				String pw = new String(pwTextField.getPassword()).trim();
				if (uid.length() == 0) {
					// If user name field is empty, and error message will display telling which part is missing
					JOptionPane.showMessageDialog(null, "Please enter the user name.");
					uidTextField.grabFocus();
				} else if (pw.length() == 0) {
					//If password field is empty, an error message will display telling which part is missing
					JOptionPane.showMessageDialog(null, "Please enter the password.");
					pwTextField.grabFocus();
				} else {
					// When filled both fields well,
					new RequestBase(ERequest.SIGNIN, new String[] { uid, pw }) {
						//Send a request to sign-in response and wait for a response
						@Override
						protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
							
							switch (response) {
							
							case SIGNIN_OK: // Sucess log-in
								System.out.println("Signed In: " + uid);
								
								MainWindow mainWindow = new MainWindow();
								switchWindow(mainWindow, true);
								break;
								
							case SIGNIN_ERR_NO_UID: // Fail log-in (Non-existent account)
								JOptionPane.showMessageDialog(null, "This user name is not registered.");
								pwTextField.setText("");
								uidTextField.selectAll();
								uidTextField.grabFocus();
								break;
								
							case SIGNIN_ERR_PW: // Fail log-in (Wrong password)
								JOptionPane.showMessageDialog(null, "You've entered a wrong password.");
								pwTextField.selectAll();
								pwTextField.grabFocus();
								break;
								
							case SIGNIN_ERR_MULTI: // Fail log-in (Log-in multiple devices)
								JOptionPane.showMessageDialog(null, "You signed in on other device.\nIf it's not you, please contact us.");
								break;
								
							default: // Fail log-in
								JOptionPane.showMessageDialog(null, "Unknown Error: Please try again.");
								break;
							}
						}
					}.request();
				}
			}
		});
	}

	@Override
	public void handleAnnouncement(EResponse response, String[] params) {
		
		// Empty
	}
}
