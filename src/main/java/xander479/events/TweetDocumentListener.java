package xander479.events;

import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import static xander479.TwitterBot.TWITTER_CHAR_LIMIT;

public class TweetDocumentListener implements DocumentListener {
	private Document tweetDocument;
	private JLabel charsLeft;
	public TweetDocumentListener(Document tweetDocument, JLabel charsLeft) {
		this.tweetDocument = tweetDocument;
		this.charsLeft = charsLeft;
	}
	
	public void insertUpdate(DocumentEvent e) {
		changedUpdate(e);
	}

	public void removeUpdate(DocumentEvent e) {
		changedUpdate(e);
	}

	public void changedUpdate(DocumentEvent e) {
		int chars = tweetDocument.getLength();
		charsLeft.setText(new Integer(TWITTER_CHAR_LIMIT - chars).toString());
	}
}
