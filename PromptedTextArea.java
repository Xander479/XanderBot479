package xander479;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class PromptedTextArea extends JTextArea implements DocumentListener {	
	private static final long serialVersionUID = 1L;
	private String placeholder;
	private JLabel label;
	private Document document = this.getDocument();
	private Color color = new Color(0, 0, 0, 128);
	
	public PromptedTextArea(int rows, int columns, String placeholder, String position) {
		super(rows, columns);
		this.placeholder = placeholder;
		label = new JLabel(" " + placeholder);
		label.setForeground(color);
		setLayout(new BorderLayout());
		add(label, position);
		document.addDocumentListener(this);
	}
	
	// Getter
	public String getPlaceholder() {
		return placeholder;
	}

	// Setter
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
		label.setText(" " + placeholder);
	}
	
	void showHidePlaceholder() {
		if(document.getLength() > 0) {
			label.setVisible(false);
			return;
		}
		else {
			label.setVisible(true);
			return;
		}
	}
	
	public void insertUpdate(DocumentEvent e) {
		showHidePlaceholder();
	}

	public void removeUpdate(DocumentEvent e) {
		showHidePlaceholder();
	}

	public void changedUpdate(DocumentEvent e) {
		// Unused
	}

}
