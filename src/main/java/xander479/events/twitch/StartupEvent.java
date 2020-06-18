package xander479.events.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelJoinEvent;

public class StartupEvent {
	public StartupEvent(SimpleEventHandler eventHandler) {
		eventHandler.onEvent(ChannelJoinEvent.class, event -> onStartup(event));
	}
	
	void onStartup(ChannelJoinEvent event) {
		String msg = "Connected!";
		
		System.out.println(event.getChannel().getName());
		event.getTwitchChat().sendMessage(event.getChannel().getName(), msg);
	}
}
