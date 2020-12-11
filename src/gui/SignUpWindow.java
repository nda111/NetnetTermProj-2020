package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import data.ERequest;
import data.EResponse;
import gui.component.HintTextField;
import interaction.RequestBase;

public final class SignUpWindow extends WindowBase {

	private JLabel uidLabel;
	private JTextField uidTextField;
	private JLabel emailLabel;
	private JTextField emailTextField;
	private JLabel pwLabel;
	private JPasswordField pwTextField;
	private JLabel nameLabel;
	private JTextField nameTextField;
	private JLabel birthLabel;
	private HintTextField birthTextField;
	private JButton signUpButton;
	private JButton cancelButton;
	
	@Override
	public void configureWindow() {

		setSize(300, 600);
		setResizable(false);
	}

	@Override
	public void initializeGuiComponents(JPanel root) {
		
		GridLayout layout = new GridLayout(0, 2);
		layout.setVgap(5);
		root.setLayout(layout);
		root.setBorder(BorderFactory.createEmptyBorder(80, 20, 245, 20));

		uidLabel = new JLabel("User", SwingConstants.RIGHT);
		root.add(uidLabel);

		uidTextField = new JTextField(20);
		root.add(uidTextField);

		pwLabel = new JLabel("Password", SwingConstants.RIGHT);
		root.add(pwLabel);

		pwTextField = new JPasswordField(20);
		root.add(pwTextField);

		emailLabel = new JLabel("E-Mail", SwingConstants.RIGHT);
		root.add(emailLabel);

		emailTextField = new JTextField(20);
		root.add(emailTextField);

		nameLabel = new JLabel("Name", SwingConstants.RIGHT);
		root.add(nameLabel);

		nameTextField = new JTextField(20);
		root.add(nameTextField);

		birthLabel = new JLabel("Birthday", SwingConstants.RIGHT);
		root.add(birthLabel);

		final Date today = new Date(System.currentTimeMillis());
		birthTextField = new HintTextField(new SimpleDateFormat("yyyyMMdd").format(today));
		root.add(birthTextField);

		cancelButton = new JButton("Cancel");
		root.add(cancelButton);

		signUpButton = new JButton("Sign Up");
		root.add(signUpButton);
	}

	@Override
	public void setGuiEvents() {

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				backToParent();
			}
		});
		
		signUpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				final String uid = uidTextField.getText().trim();
				final String pw = new String(pwTextField.getPassword()).trim();
				final String email = emailTextField.getText().trim();
				final String name = nameTextField.getText().trim();
				final String birthString = birthTextField.getText().trim();
				if (uid.length() == 0) {
					
					JOptionPane.showMessageDialog(null, "Please enter the user name.");
					uidTextField.grabFocus();
				} else if (pw.length() == 0) {
					
					JOptionPane.showMessageDialog(null, "Please enter the password.");
					pwTextField.grabFocus();
				} else if (email.length() == 0) {
					
					JOptionPane.showMessageDialog(null, "Please enter your email address.");
					emailTextField.grabFocus();
				} else if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
					
					JOptionPane.showMessageDialog(null, "Please enter a valid email address.");
					emailTextField.grabFocus();
				} else if (name.length() == 0) {

					JOptionPane.showMessageDialog(null, "Please enter your name (or a nick name).");
					nameTextField.grabFocus();
				} else if (birthString.length() == 0) {
					
					JOptionPane.showMessageDialog(null, "Please enter your birthday.");
					birthTextField.grabFocus();
				} else {
					
					long birth = -1;
					try {
						
						int year = Integer.parseInt(birthString.substring(0, 3));
						int month = Integer.parseInt(birthString.substring(4, 5)) - 1;
						int day = Integer.parseInt(birthString.substring(6, 7));
						
						Date birthday = new Date();
						birthday.setYear(year);
						birthday.setMonth(year);
						birthday.setDate(year);
						
						birth = birthday.getTime();
					} catch (NumberFormatException | StringIndexOutOfBoundsException formatException) {

						JOptionPane.showMessageDialog(null, "Please enter a valid 8-digits birthday. (20000111)");
						uidTextField.grabFocus();
						
						return;
					}

					// Check uid validity
					EResponse response = new RequestBase(ERequest.VALIDATE_UID, new String[] { uid }, reader, writer) {

						@Override
						protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
							
							switch (response) {
							
							case VALIDATE_UID_OK:
								// Normal
								break;
								
							case VALIDATE_UID_NO:
								JOptionPane.showMessageDialog(null, "This user name is already in use.");
								uidTextField.setText("");
								uidTextField.grabFocus();
								break;
								
							default:
								JOptionPane.showMessageDialog(null, "Unknown Error: Please try again.");
								break;
							}
						}
					}.request();
					
					// Request sign up
					if (response == EResponse.VALIDATE_UID_OK) {
					
						System.out.println("SIGNUP");
						response = new RequestBase(ERequest.SIGNUP, new String[] { uid, email, pw, name, Long.toString(birth) }, reader, writer) {
	
							@Override
							protected void handle(EResponse response, Scanner reader, PrintWriter writer) {
								
								switch (response) {
								
								case SIGNUP_OK:
									JOptionPane.showMessageDialog(null, "Welcome!!");
									backToParent();
									break;
									
								case SIGNUP_ERR:
								default:
									JOptionPane.showMessageDialog(null, "Unknown Error: Please try again.");
									break;
								}
							}
						}.request();
					}
				}
			}
		});
	}
}
