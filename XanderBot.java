package xander479;

import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import static xander479.Setup.*;

public class XanderBot {
	public final static int TWITTER_CHARACTER_LIMIT = 280;
	public static void main(String[] args) {
		createAndDisplayGUI();
	}
	
	static JLabel charsLeft = new JLabel();
	static void createAndDisplayGUI() {
		// Set up frame
		JFrame frame = new JFrame("XanderBot479");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(600,400));
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c;
		
		// Creates text area
		JTextArea tweetInput = new JTextArea(10, 25);
		JScrollPane scrollPane = new JScrollPane(tweetInput);
		tweetInput.setLineWrap(true);
		tweetInput.setWrapStyleWord(true);
		Document tweetDocument = tweetInput.getDocument();
		tweetDocument.addDocumentListener(new TweetListener(tweetDocument));
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 0;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.insets = new Insets(3, 3, 3, 3);
		c.fill = GridBagConstraints.BOTH;
		pane.add(tweetInput, c);
		
		// Label for remaining characters
		charsLeft.setText("" + TWITTER_CHARACTER_LIMIT);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.weightx = 0.5;
		c.weighty = 0.25;
		pane.add(charsLeft, c);
		
		// Creates tweet button
		JButton tweetButton = new JButton("Tweet");
		tweetButton.setMnemonic(KeyEvent.VK_T);
		tweetButton.addActionListener(new TweetEvent(tweetDocument));
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 0.5;
		c.weighty = 0.25;
		c.insets = new Insets(0, 5, 0, 0);
		
		pane.add(tweetButton, c);
		
		// Show frame and add menu bar
		frame.setJMenuBar(createMenuBar());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	// Create menu bar
	static JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("About");
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);
		JMenuItem menuItem = new JMenuItem("GitHub repo link");
		menuItem.setMnemonic(KeyEvent.VK_G);
		menuItem.addActionListener(new GitHubEvent());
		menu.add(menuItem);
		return menuBar;
	}
}

// Updates the label for remaining characters
class TweetListener implements DocumentListener {
	TweetListener(Document d) {
		this.d = d;
	}
	private Document d;
	
	public void changedUpdate(DocumentEvent e) {
		int chars = d.getLength();
		XanderBot.charsLeft.setText("" + (XanderBot.TWITTER_CHARACTER_LIMIT - chars));
	}
	
	public void insertUpdate(DocumentEvent e) {
		this.changedUpdate(e);
	}
	
	public void removeUpdate(DocumentEvent e) {
		this.changedUpdate(e);
	}
}

// Event takes you to the GitHub repo
class GitHubEvent implements ActionListener {
	public void actionPerformed(ActionEvent event) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(new URI("https://github.com/Xander479/XanderBot479"));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			JFrame frame = new JFrame("Error");
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			JLabel label = new JLabel("Your platform doesn't support this action.", JLabel.CENTER);
			label.setPreferredSize(new Dimension(350, 50));
			frame.getContentPane().add(label);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
	}
}

// Event sends the inputted text as a tweet
class TweetEvent implements ActionListener {
	TweetEvent(Document d) {
		this.d = d;
	}
	private Document d;
	
	public void actionPerformed(ActionEvent event) {
		int chars = d.getLength();
		// Won't allow you to send a tweet over the character limit
		if(chars > XanderBot.TWITTER_CHARACTER_LIMIT) {
			XanderBot.charsLeft.setText("You can only have " + XanderBot.TWITTER_CHARACTER_LIMIT + " characters in a tweet.");
		}
		else {
			try {
				Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(Setup.CONSUMER_KEY, Setup.CONSUMER_KEY_SECRET);
				AccessToken accessToken = new AccessToken(Setup.ACCESS_TOKEN, Setup.ACCESS_TOKEN_SECRET);
				
				twitter.setOAuthAccessToken(accessToken);
				
				// THIS IS THE LINE WHICH SENDS THE TWEET
				twitter.updateStatus(d.getText(0, d.getLength()));
				// THIS IS THE LINE WHICH SENDS THE TWEET
			}
			catch(TwitterException e) {
				XanderBot.charsLeft.setText(e.toString());
			}
			catch(BadLocationException e) {
				XanderBot.charsLeft.setText(e.toString());
			}
		}	
	}
}
