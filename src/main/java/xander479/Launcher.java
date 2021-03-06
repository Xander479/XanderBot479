package xander479;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import xander479.events.BotEvent;
import xander479.events.GitHubEvent;

public class Launcher {
	public static void main(String[] args) {
		// Set up frame
		JFrame frame = new JFrame("XanderBot479");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(450, 250));
		frame.setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c;
		
		// Create discord button
		JButton discordButton = new JButton("Discord");
		discordButton.setMnemonic(KeyEvent.VK_D);
		discordButton.addActionListener(new BotEvent(BotEvent.DISCORD, frame));
		discordButton.setPreferredSize(new Dimension(100, 30));
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.insets = new Insets(5, 5, 5, 5);
		pane.add(discordButton, c);
		
		// Create twitch button
		JButton twitchButton = new JButton("Twitch");
		twitchButton.setMnemonic(KeyEvent.VK_T);
		twitchButton.addActionListener(new BotEvent(BotEvent.TWITCH, frame));
		twitchButton.setPreferredSize(new Dimension(100, 30));
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.insets = new Insets(5, 5, 5, 5);
		pane.add(twitchButton, c);
		
		// Create twitter button
		JButton twitterButton = new JButton("Twitter");
		twitterButton.setMnemonic(KeyEvent.VK_W);
		twitterButton.addActionListener(new BotEvent(BotEvent.TWITTER, frame));
		twitterButton.setPreferredSize(new Dimension(100, 30));
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 1;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.insets = new Insets(5, 5, 5, 5);
		pane.add(twitterButton, c);
		
		frame.setJMenuBar(createMenuBar());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	// Return the menu bar to be added to the frame 
	static JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("About");
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);
		JMenuItem menuItem = new JMenuItem("GitHub repo link");
		menuItem.setMnemonic(KeyEvent.VK_G);
		menuItem.addActionListener(new GitHubEvent("XanderBot479"));
		menu.add(menuItem);
		return menuBar;
	}
	
	// Return the config file passed as a String
	public static Document getConfig(String path) throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document config = builder.parse(new File(path));
		config.getDocumentElement().normalize();
		return config;
	}
}
