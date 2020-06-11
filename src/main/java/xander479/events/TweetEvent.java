package xander479.events;

import static xander479.TwitterBot.TWITTER_CHAR_LIMIT;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.text.JTextComponent;

import org.w3c.dom.Document;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import xander479.Launcher;
import xander479.TwitterBot;

public class TweetEvent implements ActionListener {
	final static String CONFIG_FILE = "src/main/resources/twitter.xml";
	private static Document config;
	private final static String CONSUMER_KEY;
	private final static String CONSUMER_KEY_SECRET;
	private final static String ACCESS_TOKEN;
	private final static String ACCESS_TOKEN_SECRET;
	static {
		try {
			config = Launcher.getConfig(CONFIG_FILE);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.err.println("Check that there are valid tokens in the " + CONFIG_FILE + " file.");
		}
		CONSUMER_KEY = config.getElementsByTagName("consumerKey").item(0).getTextContent();
		CONSUMER_KEY_SECRET = config.getElementsByTagName("consumerKeySecret").item(0).getTextContent();
		ACCESS_TOKEN = config.getElementsByTagName("accessToken").item(0).getTextContent();
		ACCESS_TOKEN_SECRET = config.getElementsByTagName("accessTokenSecret").item(0).getTextContent();
	}
	private JTextComponent tweetInput;
	private javax.swing.text.Document tweetDocument;
	
	public TweetEvent(JTextComponent tweetInput) {
		this.tweetInput = tweetInput;
		tweetDocument = tweetInput.getDocument();
	}
	
	public void actionPerformed(ActionEvent e) {
		int chars = tweetDocument.getLength();
		// Won't allow you to send a tweet over the character limit
		if(chars > TWITTER_CHAR_LIMIT) {
			TwitterBot.charsLeft.setText("You can only have " + TWITTER_CHAR_LIMIT + " characters in a tweet.");
		}
		else {
			try {
				Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
				twitter.setOAuthAccessToken(new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET));
				
				// THIS IS THE LINE WHICH SENDS THE TWEET
				twitter.updateStatus(tweetDocument.getText(0, tweetDocument.getLength()));
				// THIS IS THE LINE WHICH SENDS THE TWEET
				
				tweetInput.setText("");
				TwitterBot.charsLeft.setText("Tweet sent!");
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
