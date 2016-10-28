package yourselvs.rankwizard.actions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemAction implements RankAction {
	ItemStack item;
	
	public ItemAction(ItemStack item) {
		
	}
	
	public boolean canGiveToPlayer(Player player) {
		if(player.getInventory().firstEmpty() == -1) {
			return false;
		}
		
		return true;
	}

	public boolean canTakeFromPlayer(Player player) {
		if(player.getInventory().contains(item)) {
			return true;
		}
		
		return false;
	}

	public void giveToPlayer(Player player) {
		player.getInventory().setItem(player.getInventory().firstEmpty(), item);
	}

	public void takeFromPlayer(Player player) {
		player.getInventory().setItem(player.getInventory().first(item), new ItemStack(Material.AIR));
	}

}
