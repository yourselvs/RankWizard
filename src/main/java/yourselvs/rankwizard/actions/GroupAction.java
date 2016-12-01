package yourselvs.rankwizard.actions;

import java.io.Serializable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import yourselvs.rankwizard.RankWizard;

public class GroupAction implements RankAction, Serializable {
	private String group;
	private transient RankWizard instance;
	
	public GroupAction() {
		
	}
	
	public GroupAction(RankWizard instance, String groupName) {
		this.group = groupName;
		this.instance = instance;
	}

	public boolean canGiveToPlayer(Player player) {
		return true;
	}

	public boolean canTakeFromPlayer(Player player) {
		if(instance.getPerms().playerInGroup(player, group)) {
			return true;
		}
		
		return false;
	}

	public void giveToPlayer(Player player) {
		PermissionUser user = PermissionsEx.getUser(player);
		
		user.addGroup(group);
	}

	public void takeFromPlayer(Player player) {
		PermissionUser user = PermissionsEx.getUser(player);
		 
		user.removeGroup(group);
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof GroupAction;
	}

	public String getGroup() {
		return group;
	}
	
	@Override
	public String toString() {
		return "PEX group: " + ChatColor.YELLOW + group;
		
	}
	
	@Override
	public void setInstance(RankWizard instance) {
		this.instance = instance;
	}
}
