package xander479;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.w3c.dom.Document;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

import xander479.events.twitch.StartupEvent;

public class TwitchBot {
	private final static String CONFIG_FILE = "src/main/resources/twitch.xml";
	private static Document config;
	private static String CHANNEL_NAME;
	private static String ACCESS_TOKEN;
	private static String CLIENT_ID;
	private static String CLIENT_SECRET;
	static {
		try {
			config = Launcher.getConfig(CONFIG_FILE);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.err.println("Check that there is a valid token in the " + CONFIG_FILE + " file.");
		}
		CHANNEL_NAME = config.getElementsByTagName("channelName").item(0).getTextContent();
		ACCESS_TOKEN = config.getElementsByTagName("accessToken").item(0).getTextContent();
		CLIENT_ID = config.getElementsByTagName("clientID").item(0).getTextContent();
		CLIENT_SECRET = config.getElementsByTagName("clientSecret").item(0).getTextContent();
	}
	static JFrame frame;
	Frame parent;
	
	public TwitchBot(Frame parent) {
		this.parent = parent;
		TwitchClient client = TwitchClientBuilder.builder()
				.withClientId(CLIENT_ID)
				.withClientSecret(CLIENT_SECRET)
				.withChatAccount(new OAuth2Credential("twitch", ACCESS_TOKEN))
				.withEnableChat(true)
				.build();
		
		SimpleEventHandler eventHandler = client.getEventManager().getEventHandler(SimpleEventHandler.class);
		new StartupEvent(eventHandler);
		
		createAndDisplayGUI();
		client.getChat().joinChannel(CHANNEL_NAME);
	}
	
	void createAndDisplayGUI() {
		frame = new JFrame("Twitch");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(400, 150));
		frame.setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
		
		JLabel label = new JLabel("Bot is connected. UI will be worked on soon.", JLabel.CENTER);
		frame.getContentPane().add(label);
		
		frame.setJMenuBar(createMenuBar());
		frame.pack();
		frame.setLocationRelativeTo(parent);
		frame.setVisible(true);
	}
	
	// Return the menu bar to be added to the frame
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
}
