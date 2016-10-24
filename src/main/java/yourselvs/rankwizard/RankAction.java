package yourselvs.rankwizard;

import org.bukkit.entity.Player;

public interface RankAction {
	public boolean canGiveToPlayer(Player player);
	public boolean canTakeFromPlayer(Player player);
	public void giveToPlayer(Player player);
	public void takeFromPlayer(Player player);
}
