package xander479.events;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import xander479.DiscordBot;
import xander479.TwitchBot;
import xander479.TwitterBot;

/**
 * Event listener for butotns in the {@link Launcher} class.
 * 
 * @author Xander Clinton
 * @version 1.0
 */
public class BotEvent implements ActionListener {
	/**
	 * The Discord bot value.
	 */
	public final static int DISCORD = 0;
	/**
	 * The Twitch bot value.
	 */
	public final static int TWITCH = 1;
	/**
	 * The Twitter bot value.
	 */
	public final static int TWITTER = 2;
	private int type;
	private Frame parent;
	
	/**
	 * Constructs a new BotEvent.
	 * 
	 * @param type which bot to be activated
	 * @param parent The parent window
	 */
	public BotEvent(int type, Frame parent) {
		this.type = type;
		this.parent = parent;
	}
	
	/**
	 * Determines which bot to activate.
	 */
	public void actionPerformed(ActionEvent e) {
		switch(type) {
			case DISCORD:
				new DiscordBot(parent);
				break;
			case TWITCH:
				new TwitchBot(parent);
				break;
			case TWITTER:
				new TwitterBot(parent);
				break;
			default:
				System.err.println("Invalid int input to constructor");
		}
	}
}
