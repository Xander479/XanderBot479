package xander479.events;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import xander479.DiscordBot;
import xander479.TwitchBot;
import xander479.TwitterBot;

public class BotEvent implements ActionListener {
	public final static int DISCORD = 0;
	public final static int TWITCH = 1;
	public final static int TWITTER = 2;
	private int type;
	private Frame parent;
	
	public BotEvent(int type, Frame parent) {
		this.type = type;
		this.parent = parent;
	}
	
	// Determine which bot to activate
	public void actionPerformed(ActionEvent e) {
		switch(type) {
			case DISCORD:
				DiscordBot.getInstance(parent);
				break;
			case TWITCH:
				TwitchBot.getInstance(parent);
				break;
			case TWITTER:
				TwitterBot.getInstance(parent);
				break;
			default:
				System.err.println("Invalid int input to constructor");
		}
	}
}
