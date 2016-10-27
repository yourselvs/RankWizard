package yourselvs.rankwizard.actions;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import yourselvs.rankwizard.RankAction;

public class PermissionAction implements RankAction {
	Permission permission;
	
	public PermissionAction(Permission permission) {
		this.permission = permission;
	}
	public boolean canGiveToPlayer(Player player) {		
		if(player.hasPermission(permission)) {
			return false;
		}
		
		return true;
	}

	public boolean canTakeFromPlayer(Player player) {
		if(player.hasPermission(permission)) {
			return true;
		}
		
		return false;
	}

	public void giveToPlayer(Player player) {
		PermissionUser user = PermissionsEx.getUser(player);
		
		user.addPermission(permission.getName());
	}

	public void takeFromPlayer(Player player) {
		PermissionUser user = PermissionsEx.getUser(player);
		
		user.removePermission(permission.getName());
	}

}
