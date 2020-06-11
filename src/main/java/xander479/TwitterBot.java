package xander479;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.text.Document;

import xander479.events.TweetDocumentListener;
import xander479.events.TweetEvent;

public class TwitterBot {
	public final static int TWITTER_CHAR_LIMIT = 280;
	final static String CONFIG_FILE = "src/main/resources/twitter.xml";
	Frame parent;
	
	public TwitterBot(Frame parent) {
		this.parent = parent;
		createAndDisplayGUI();
	}
	
	static JLabel charsLeft;
	void createAndDisplayGUI() {
		// Set up frame
		JFrame frame = new JFrame("Twitter");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(300, 200));
		frame.setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c;
		
		// Create text area
		PromptedTextArea tweetInput = new PromptedTextArea(10, 25, "What's happening?", BorderLayout.PAGE_START);
		tweetInput.setLineWrap(true);
		tweetInput.setWrapStyleWord(true);
		Document tweetDocument = tweetInput.getDocument();
		tweetDocument.addDocumentListener(new TweetDocumentListener(tweetDocument));
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 0;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.insets = new Insets(3, 3, 3, 3);
		c.fill = GridBagConstraints.BOTH;
		pane.add(tweetInput, c);
		
		// Create label for remaining characters
		charsLeft = new JLabel(new Integer(TWITTER_CHAR_LIMIT).toString());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.5;
		c.weighty = 0.1;
		c.insets = new Insets(3, 3, 3, 3);
		pane.add(charsLeft, c);
		
		// Create tweet button
		JButton tweetButton = new JButton("Tweet");
		tweetButton.setMnemonic(KeyEvent.VK_ENTER);
		tweetButton.addActionListener(new TweetEvent(tweetInput));
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.5;
		c.weighty = 0.1;
		c.insets = new Insets(3, 3, 3, 3);
		pane.add(tweetButton, c);
		
		// Show frame and add menu bar
		frame.setJMenuBar(createMenuBar());
		frame.pack();
		frame.setLocationRelativeTo(parent);
		frame.setVisible(true);
	}
	
	// Return the menu bar to be added to the frame
	static JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Settings");
		menu.setMnemonic(KeyEvent.VK_S);
		menuBar.add(menu);
		// TODO Add settings menu
		JMenuItem menuItem = new JMenuItem("I'm working on it...");
		menu.add(menuItem);
		return menuBar;
	}
}
