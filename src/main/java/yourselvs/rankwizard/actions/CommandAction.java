package yourselvs.rankwizard.actions;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import yourselvs.rankwizard.RankWizard;

public class CommandAction implements RankAction, Serializable {
	String cmd;
	
	public CommandAction(String command) {
		cmd = command;
	}
	
	@Override
	public boolean canGiveToPlayer(Player player) {
		return true;
	}

	@Override
	public boolean canTakeFromPlayer(Player player) {
		return true;
	}

	@Override
	public void giveToPlayer(Player player) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
	}

	@Override
	public void takeFromPlayer(Player player) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof CommandAction;
	}
	
	@Override
	public String toString() {
		return "Command run: " + ChatColor.YELLOW + cmd;
	}
	
	@Override
	public void setInstance(RankWizard instance) {
		
	}
}
