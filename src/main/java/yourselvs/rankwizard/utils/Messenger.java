package yourselvs.rankwizard.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mythicacraft.voteroulette.utils.InteractiveMessageAPI.FormattedText;
import com.mythicacraft.voteroulette.utils.InteractiveMessageAPI.InteractiveMessage;
import com.mythicacraft.voteroulette.utils.InteractiveMessageAPI.InteractiveMessageElement;
import com.mythicacraft.voteroulette.utils.InteractiveMessageAPI.InteractiveMessageElement.ClickEvent;
import com.mythicacraft.voteroulette.utils.InteractiveMessageAPI.InteractiveMessageElement.HoverEvent;

public class Messenger {
	private String prefix;
	private String linkPrefix;
	private JavaPlugin plugin;
	
	public Messenger(JavaPlugin instance, String prefix, String linkPrefix){
		this.plugin = instance;
		this.prefix = prefix;
		this.linkPrefix = prefix;
	}
	
	public void setPrefix(String prefix){
		this.prefix = prefix;
	}
	
	public void setLinkPrefix(String prefix){
		this.linkPrefix = prefix;
	}
	
	public void setUnformattedPrefix(String prefix){
	}
	
	public void sendPlayerLog(Player player, String message){
		plugin.getLogger().info("Player \"" + player.getName() + "\" : " + message + "(UUID: " + player.getUniqueId().toString() + ")");
	}
	
	public void sendMessage(Player player, String message){
		player.sendMessage(prefix + ChatColor.RESET + message + ChatColor.RESET);
	}
	
	public void sendMessages(Player player, List<String> msgs){
		ArrayList<String> updateMessages = new ArrayList<String>();
		updateMessages.add(prefix + "- - - - - - - - - - - - - - ");
		for(String message : msgs)
			updateMessages.add(message + ChatColor.RESET);
		updateMessages.add(prefix + "- - - - - - - - - - - - - - ");
		player.sendMessage(updateMessages.toArray(new String[msgs.size()]));
	}
	
	public void sendMessage(CommandSender player, String message){
		player.sendMessage(prefix + message);
	}
	
	public void sendServerMessage(String message){
		for(Player player : plugin.getServer().getOnlinePlayers())
			sendMessage(player, message);
	}	
	
	public void sendServerMessage(ArrayList<String> messages){
		for(Player player : plugin.getServer().getOnlinePlayers())
			sendMessages(player, messages);
	}
	
	public void sendClickMessage(Player player, String line, String hoverMessage, String command){
		InteractiveMessage message = new InteractiveMessage(new InteractiveMessageElement(new FormattedText(linkPrefix + line), HoverEvent.SHOW_TEXT, new FormattedText(hoverMessage), ClickEvent.RUN_COMMAND, command));	
		message.sendTo(player);
	}
	
	public void sendClickMessage(Player player, String line, String command){
		InteractiveMessage message = new InteractiveMessage(new InteractiveMessageElement(new FormattedText(linkPrefix + line), HoverEvent.NONE, new FormattedText(""), ClickEvent.RUN_COMMAND, command));	
		message.sendTo(player);
	}
	
	public void sendSuggestMessage(Player player, String line, String hoverMessage, String command){
		InteractiveMessage message = new InteractiveMessage(new InteractiveMessageElement(new FormattedText(linkPrefix + line), HoverEvent.SHOW_TEXT, new FormattedText(hoverMessage), ClickEvent.SUGGEST_COMMAND, command));	
		message.sendTo(player);
	}

	public void sendSuggestMessage(Player player, String line, String command){
		InteractiveMessage message = new InteractiveMessage(new InteractiveMessageElement(new FormattedText(linkPrefix + line), HoverEvent.NONE, new FormattedText(""), ClickEvent.SUGGEST_COMMAND, command));
		message.sendTo(player);
	}
}
