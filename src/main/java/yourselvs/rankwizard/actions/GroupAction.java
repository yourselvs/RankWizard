package yourselvs.rankwizard.actions;

import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class GroupAction implements RankAction {
	private PermissionGroup group;
	
	public GroupAction(PermissionGroup group) {
		this.group = group;
	}

	public boolean canGiveToPlayer(Player player) {
		PermissionUser user = PermissionsEx.getUser(player);

		if(user.inGroup(group)) {
			return false;
		}
		
		return true;
	}

	public boolean canTakeFromPlayer(Player player) {
		PermissionUser user = PermissionsEx.getUser(player);

		if(user.inGroup(group)) {
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

}
