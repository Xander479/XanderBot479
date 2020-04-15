package xander479;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * Modified version of the swing {@link JTextArea} which takes a {@link String} parameter to display placeholder text.
 * Much like with the HTML placeholder attribute, the placeholder text disappears once text is entered to the field. 
 * 
 * @author Xander Clinton
 * @version 1.1
 */
public class PromptedTextArea extends JTextArea implements DocumentListener {	
	private static final long serialVersionUID = 1L;
	private String placeholder;
	private JLabel label = new JLabel();
	private Document document = getDocument();
	private Color color = new Color(0, 0, 0, 128);
	
	/**
	 * Constructs a new PromptedTextArea. A default model is set, the initial string is null, and all rows/columns are set to 0.
	 * The placeholder text, and its position are set according to the specified parameters.
	 * 
	 * @param placeholder the text to be shown when the text area is empty
	 * @param position where to place the placeholder text within the text area. Defined in {@link BorderLayout}
	 */
	public PromptedTextArea(String placeholder, String position) {
		super();
		this.placeholder = placeholder;
		addPlaceholder(position);
	}

	/**
	 * Constructs a new PromptedTextArea with the specified number of rows and columns, and the given model.
	 * The placeholder text, and its position are set according to the specified parameters. 
	 * 
	 * @param doc the model to use, or create a default one if null
	 * @param text the text to be displayed, null if none
	 * @param rows the number of rows >= 0
	 * @param columns the number of columns >= 0
	 * @param placeholder the text to be shown when the text area is empty
	 * @param position where to place the placeholder text within the text area. Defined in {@link BorderLayout}
	 */
	public PromptedTextArea(Document doc, String text, int rows, int columns, String placeholder, String position) {
		super(doc, text, rows, columns);
		this.placeholder = placeholder;
		addPlaceholder(position);
	}

	/**
	 * Constructs a new JTextArea with the given document model, and defaults for all of the other arguments (null, 0, 0).
	 * The placeholder text, and its position are set according to the specified parameters.
	 * 
	 * @param doc the model to use
	 * @param placeholder the text to be shown when the text area is empty
	 * @param position where to place the placeholder text within the text area. Defined in {@link BorderLayout}
	 */
	public PromptedTextArea(Document doc, String placeholder, String position) {
		super(doc);
		this.placeholder = placeholder;
		addPlaceholder(position);
	}

	/**
	 * Constructs a new empty PromptedTextArea with the specified number of rows and columns. A default model is created, and the initial string is null.
	 * The placeholder text, and its position are set according to the specified parameters.
	 * 
	 * @param rows the number of rows >= 0
	 * @param columns the number of columns >= 0
	 * @param placeholder the text to be shown when the text area is empty
	 * @param position where to place the placeholder text within the text area. Defined in {@link BorderLayout}
	 */
	public PromptedTextArea(int rows, int columns, String placeholder, String position) {
		super(rows, columns);
		this.placeholder = placeholder;
		addPlaceholder(position);
	}

	/**
	 * Constructs a new PromptedTextArea with the specified text and number of rows and columns. A default model is created.
	 * The placeholder text, and its position are set according to the specified parameters.
	 * 
	 * @param text the text to be displayed, or null
	 * @param rows the number of rows >= 0
	 * @param columns the number of columns >= 0
	 * @param placeholder the text to be shown when the text area is empty
	 * @param position where to place the placeholder text within the text area. Defined in {@link BorderLayout}
	 */
	public PromptedTextArea(String text, int rows, int columns, String placeholder, String position) {
		super(text, rows, columns);
		this.placeholder = placeholder;
		addPlaceholder(position);
	}

	/**
	 * Constructs a new PromptedTextArea with the specified text displayed. A default model is created and rows/columns are set to 0.
	 * 
	 * @param text the text to be displayed, or null
	 * @param placeholder the text to be shown when the text area is empty
	 * @param position where to place the placeholder text within the text area. Defined in {@link BorderLayout}
	 */
	public PromptedTextArea(String text, String placeholder, String position) {
		super(text);
		this.placeholder = placeholder;
		addPlaceholder(position);
	}

	/**
	 * Used within constructors to avoid duplicate code. Adds the placeholder text to the text area.
	 * 
	 * @param position Where to place the placeholder text within the text area. Defined in {@link BorderLayout}
	 */
	private void addPlaceholder(String position) {
		setPlaceholder(placeholder);
		label.setForeground(color);
		setLayout(new BorderLayout());
		add(label, position);
		document.addDocumentListener(this);
	}
	
	/**
	 * Getter method for the placeholder text.
	 */
	public String getPlaceholder() {
		return placeholder;
	}

	/**
	 * Changes the placeholder text in the text area.
	 * 
	 * @param placeholder the text to be shown when the text area is empty
	 */
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
		label.setText(" " + placeholder);
	}
	
	/**
	 * Used to display the placeholder text when there is no text in the text area
	 * and to hide it when text is entered.
	 */
	private void showHidePlaceholder() {
		if(document.getLength() > 0) {
			label.setVisible(false);
			return;
		}
		else {
			label.setVisible(true);
			return;
		}
	}
	
	/**
	 * Hides the placeholder text if there is text in the text area.
	 */
	public void insertUpdate(DocumentEvent e) {
		showHidePlaceholder();
	}

	/**
	 * Displays the placeholder text if there is no text in the text area.
	 */
	public void removeUpdate(DocumentEvent e) {
		showHidePlaceholder();
	}

	/**
	 * Unused.
	 */
	public void changedUpdate(DocumentEvent e) {
		// Unused
	}
}
