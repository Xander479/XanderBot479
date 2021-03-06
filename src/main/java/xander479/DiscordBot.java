package xander479;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.*;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.w3c.dom.Document;

import xander479.events.DiscordMessageEvent;

public class DiscordBot {
	private static DiscordBot instance;
	final static String CONFIG_FILE = "src/main/resources/discord.xml";
	final static String TOKEN;
	static String prefix;
	static Document config;
	static {
		try {
			config = Launcher.getConfig(CONFIG_FILE);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.err.println("Check that there is a valid token in the " + CONFIG_FILE + " file.");
		}
		TOKEN = config.getElementsByTagName("token").item(0).getTextContent(); 
	}
	static DiscordApi api;
	static JFrame frame;
	Frame parent;
	
	private DiscordBot(Frame parent) {
		this.parent = parent;
		api = new DiscordApiBuilder().setToken(TOKEN).login().join();
		api.addMessageCreateListener(new DiscordMessageEvent());
		createAndDisplayGUI();
	}
	
	public static synchronized DiscordBot getInstance(Frame parent) {
		if(instance != null) return instance;
		instance = new DiscordBot(parent);
		return instance;
	}
	
	// Creates the GUI to confirm the bot is running.
	void createAndDisplayGUI() {
		frame = new JFrame("Discord");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(300, 150));
		frame.setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
		
		JLabel label = new JLabel("Bot is running.", JLabel.CENTER);
		frame.getContentPane().add(label);
		
		frame.setJMenuBar(createMenuBar());
		frame.pack();
		frame.setLocationRelativeTo(parent);
		frame.setVisible(true);
	}
	
	// Returns the menu bar to be added to the frame
	JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Settings");
		menu.setMnemonic(KeyEvent.VK_S);
		menuBar.add(menu);
		// TODO Add settings menu
		JMenuItem menuItem = new JMenuItem("I'm working on it...");
		menu.add(menuItem);
		return menuBar;
	}

	// Used within DiscordMessageEvent to assign roles etc.
	public static Collection<User> getUsers(String user, Server server) {
		return api.getCachedUsersByDisplayNameIgnoreCase(user,  server);
	}
	
	// Used within DiscordMessageEvent to disconnect the bot
	public static void disconnect() {
		api.disconnect();
		frame.dispose();
	}
}
