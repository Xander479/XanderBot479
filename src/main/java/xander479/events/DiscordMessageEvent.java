package xander479.events;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import xander479.DiscordBot;
import xander479.Launcher;

public class DiscordMessageEvent implements MessageCreateListener {
	private final static String CONFIG_FILE = "src/main/resources/discord.xml";
	private final static Color EMBED_COLOR = Color.GRAY;
	private static Document config;
	private static String prefix;
	private static ArrayList<String> commands = new ArrayList<String>();
	
	public DiscordMessageEvent() {
		try {
			config = Launcher.getConfig(CONFIG_FILE);
			prefix = config.getElementsByTagName("prefix").item(0).getTextContent();
			
			NodeList commandsNL = config.getElementsByTagName("title");
			for(int i = 0; i < commandsNL.getLength(); i++) {
				commands.add(commandsNL.item(i).getTextContent());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void onMessageCreate(MessageCreateEvent event) {
		String msg = event.getMessageContent();
		if(!msg.startsWith(prefix)) {
			return;
		}
		String[] words = msg.split(" ");
		TextChannel channel = event.getChannel();
		MessageAuthor author = event.getMessageAuthor();
		Server server = event.getServer().get();
		
		switch(words[0].substring(prefix.length())) {
			case "help":
				if(words.length > 1 && commands.contains(words[1])) {
					help(words[1], channel);
				}
				else {
					help(channel);
				}
				break;
				
			case "changePrefix":
				if(words.length > 1) {
					changePrefix(words[1], author, channel);
				}
				else {
					help("changePrefix", channel);
				}
				break;
				
			case "addRole":
				if(event.isServerMessage()) {
					if(words.length > 2 ) {
						manageRoles(words[1], words[2], ADD_ROLE, author, server, channel);
					}
					else {
						help("addRole", channel);
					}
				}
				else {
					channel.sendMessage("This command can only be used in servers.");
				}
				break;
				
			case "removeRole":
				if(event.isServerMessage()) {
					if(words.length > 2) {
						manageRoles(words[1], words[2], REMOVE_ROLE, author, server, channel);
					}
					else {
						help("removeRole", channel);
					}
				}
				else {
					channel.sendMessage("This command can only be used in servers.");
				}
				break;
				
			case "disconnect":
				channel.sendMessage("Goodbye!");
				DiscordBot.disconnect();
				break;
				
			default:
				EmbedBuilder embed = new EmbedBuilder()
						.setTitle("Error")
						.setDescription("This command does not exist. Use `" + prefix + "help` for a list of commands.")
						.setColor(EMBED_COLOR);
				channel.sendMessage(embed);
		}
	}

	public static void help(TextChannel channel) {
		NodeList commandsNL = config.getElementsByTagName("command");
		String message = "";
		for(int i = 0; i < commandsNL.getLength(); i++) {
			Node node = commandsNL.item(i);
			
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				message += "**" + prefix + element.getElementsByTagName("syntax").item(0).getTextContent() + System.lineSeparator();
			}
		}
		EmbedBuilder embed = new EmbedBuilder()
				.setTitle("help")
				.setDescription(message)
				.setColor(EMBED_COLOR);
		channel.sendMessage(embed);
	}
	
	public static void help(String command, TextChannel channel) {
		String syntax = "**" + prefix;
		String description = "";
		NodeList commandsNL = config.getElementsByTagName("command");
		for(int i = 0; i < commandsNL.getLength(); i++) {
			Node node = commandsNL.item(i);
			if(node.getChildNodes().item(1).getTextContent().equals(command)) {
				syntax += node.getChildNodes().item(3).getTextContent();
				description += node.getChildNodes().item(5).getTextContent();
			}
		}
		EmbedBuilder embed = new EmbedBuilder()
				.setTitle("**" + prefix + command + "**")
				.addField("syntax", syntax)
				.setFooter(description)
				.setColor(EMBED_COLOR);
		channel.sendMessage(embed);
	}
	
	public static void changePrefix(String newPrefix, MessageAuthor author, TextChannel channel) {
		try {
			Element element = (Element)config.getElementsByTagName("prefix").item(0);
			element.setTextContent(newPrefix);
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT,  "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			
			DOMSource source = new DOMSource(config);
			StreamResult result = new StreamResult(new File(CONFIG_FILE));
			transformer.transform(source, result);
			
			EmbedBuilder embed = new EmbedBuilder()
					.setAuthor(author.getDisplayName(), null, author.getAvatar())
					.setDescription("changed the command prefix from **" + prefix + "** to **" + newPrefix + "**.")
					.setColor(EMBED_COLOR);
			channel.sendMessage(embed);
			prefix = newPrefix;
		}
		catch(Exception e) {
			e.printStackTrace();
			EmbedBuilder embed = new EmbedBuilder()
					.setTitle("Error")
					.setDescription("Change unsuccessful. Prefix is still **" + prefix + "**.")
					.setColor(EMBED_COLOR);
			channel.sendMessage(embed);
		}
	}
	
	private final static int ADD_ROLE = 0;
	private final static int REMOVE_ROLE = 1;
	public static void manageRoles(String userName, String roleName, int action, MessageAuthor author, Server server, TextChannel channel) {
		long userID = 0L;
		long roleID = 0L;
		boolean IDs = false;
		try {
			userID = Long.parseLong(userName);
			roleID = Long.parseLong(roleName);
			IDs = true;
		}
		catch(NumberFormatException e) {
			// User and/or role was not a long, so names will be used instead
			IDs = false;
		}
		User user = null;
		Role role = null;
		if(IDs) {
			if(server.getMemberById(userID).isPresent() && server.getRoleById(roleID).isPresent()) {
				user = server.getMemberById(userID).get();
				role = server.getRoleById(roleID).get();
			}
			else {
				EmbedBuilder embed = new EmbedBuilder()
						.setTitle("Error")
						.setDescription("This user and/or role doesn't exist.")
						.setColor(EMBED_COLOR);
				channel.sendMessage(embed);
			}
		}
		else {
			ArrayList<User> users = new ArrayList<User>(DiscordBot.getUsers(userName, server));
			List<Role> roles = server.getRolesByNameIgnoreCase(roleName);
			
			// Tell the user to use IDs if there are duplicate user/role names
			if(users.size() > 1 || roles.size() > 1) {
				EmbedBuilder embed = new EmbedBuilder()
						.setTitle("Error")
						.setDescription("There are multiple users or roles with the same name. Please use `" + prefix + "addRole <user ID> <role ID>`.")
						.setColor(EMBED_COLOR);
				channel.sendMessage(embed);
			}
			// If there are no users/roles found with the given name...
			else if(users.size() == 0 || roles.size() == 0) {
				EmbedBuilder embed = new EmbedBuilder()
						.setTitle("Error")
						.setDescription("This user and/or role doesn't exist.")
						.setColor(EMBED_COLOR);
				channel.sendMessage(embed);
			}
			else {
				user = users.get(0);
				role = roles.get(0);
			}
		}
		
		if(user != null && role != null) {
			switch(action) {
				case ADD_ROLE:
					server.addRoleToUser(user,  role);
					EmbedBuilder embed = new EmbedBuilder()
							.setAuthor(author.getDisplayName(), null, author.getAvatar())
							.setDescription("added the " + role.getName() + " role to " + user.getDisplayName(server) + ".")
							.setColor(EMBED_COLOR);
					channel.sendMessage(embed);
					break;
					
				case REMOVE_ROLE:
					server.removeRoleFromUser(user,  role);
					embed = new EmbedBuilder()
							.setAuthor(author.getDisplayName(), null, author.getAvatar())
							.setDescription("removed the " + role.getName() + " role from " + user.getDisplayName(server) + ".")
							.setColor(EMBED_COLOR);
					channel.sendMessage(embed);
					break;
					
				default:
					System.err.println("Method was called with invalid int for 'action'.");
					channel.sendMessage("Error with implemented method. Roles have not been changed.");
			}
		}
	}
}
