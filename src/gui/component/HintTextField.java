package gui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;

public final class HintTextField extends JTextField {
	
	private final String hint;

	public HintTextField(final String hint) {
		
		this.hint = hint;

		setText(hint);
		setForeground(Color.GRAY);

		this.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				
				if (getText().equals(hint)) {
					
					setText("");
				} else {
					
					setText(getText());
				}
				
				setForeground(Color.BLACK);
			}

			@Override
			public void focusLost(FocusEvent e) {
				
				if (getText().length() == 0) {
					
					setText(hint);
					setForeground(Color.GRAY);
				} else {
					
					setText(getText());
					setForeground(Color.BLACK);
				}
			}
		});
	}
	
	@Override 
	public String getText() {
		
		if (super.getText().equals(hint)) {
			
			return "";
		} else {
			
			return super.getText();
		}
	}
}

// [Source: https://hwangcoding.tistory.com/15]
