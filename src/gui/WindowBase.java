package gui;


import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class WindowBase extends JFrame {
	
	private WindowBase parent = null;
	private Socket socket;

	public WindowBase(Socket socket) {
		
		this.socket = socket;
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {

				if (isRoot()) {
					
					System.exit(0);
				} else {

					backToParent();
				}
	        }
		});
		
		configureWindow();
		initializeGuiComponents((JPanel)this.getContentPane());
		setGuiEvents();
	}
	
	public WindowBase() {
		
		this(null);
	}
	
	private void matchCenter(WindowBase other) {
		
		Rectangle bounds = this.getBounds();
		Dimension winSize = other.getSize();
		other.setLocation(
				bounds.x + (bounds.width - winSize.width) / 2,
				bounds.y + (bounds.height - winSize.height) / 2);
	}
	
	public boolean isRoot() {
		
		return parent == null;
	}
	
	public void switchWindow(WindowBase win, boolean newRoot) {
		
		win.socket = this.socket;
		
		matchCenter(win);

		win.setVisible(true);
		setVisible(false);
		
		if (newRoot) {
			
			disposeAll();
			win.parent = null;
		} else {
			
			win.parent = this;
		}
	}
	
	public void backToParent() {
		
		if (parent != null) {

			matchCenter(parent);

			parent.setVisible(true);
			setVisible(false);
			
			dispose();
		}
	}
	
	public void disposeAll() {

		dispose();
		
		if (parent != null) {
			
			parent.dispose();
		}
	}
	
	public abstract void configureWindow();
	
	public abstract void initializeGuiComponents(JPanel root);
	
	public abstract void setGuiEvents();
}
