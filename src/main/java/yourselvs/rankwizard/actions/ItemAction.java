package yourselvs.rankwizard.actions;

import java.io.Serializable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemAction implements RankAction, Serializable {
	private ItemStack item;
	
	public ItemAction() {
		
	}
	
	public ItemAction(ItemStack item) {
		this.item = item;
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

	@Override
	public boolean equals(Object o) {
		return o instanceof ItemAction;
	}
	
	@Override
	public String toString() {
		return item.toString();
	}
}
