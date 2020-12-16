package gui.dialog;

import java.awt.Container;
import java.awt.Dimension;
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
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import data.ERequest;
import data.EResponse;
import data.User;
import interaction.RequestBase;

public final class UserInfoDialog extends JDialog {

	private JLabel nameLabel;
	private JLabel messageLabel;
	private JButton closeButton;

	private User user;

	public UserInfoDialog(JFrame parent, User user) {

		super(parent);

		this.user = user;
		if (user != null) {

			user.valueChangeListener = new User.ValueChangeListener() {

				@Override
				public void valueChanged(String name, String message) {

					setValues();
				}
			};
		}

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

		if (this.user != null) {

			this.user.valueChangeListener = null;
		}

		dispose();
		getParent().setEnabled(true);
	}

	private void initGuiComponents(JPanel root) {

		BoxLayout rootLayout = new BoxLayout(root, BoxLayout.Y_AXIS);
		root.setLayout(rootLayout);
		root.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

		nameLabel = new JLabel("Nickname: ");
		root.add(nameLabel);
		
		root.add(Box.createRigidArea(new Dimension(0, 10)));

		messageLabel = new JLabel("Message: ");
		root.add(messageLabel);
		
		root.add(Box.createRigidArea(new Dimension(0, 10)));

		closeButton = new JButton("Close");
		closeButton.setHorizontalAlignment(SwingConstants.CENTER);
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				close();
			}
		});
		root.add(closeButton);

		setValues();
	}

	private void setValues() {

		String name = "-";
		String message = "-";

		if (this.user != null) {

			if (this.user.name != null) {
				name = this.user.name;
			}

			if (this.user.personalMessage != null) {
				message = this.user.personalMessage;
			}
		}

		nameLabel.setText("Nickname: " + name);
		messageLabel.setText("Message: " + message);
	}
}
