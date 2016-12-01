package yourselvs.rankwizard.actions;

import org.bukkit.entity.Player;

import yourselvs.rankwizard.RankWizard;

public interface RankAction {
	public boolean canGiveToPlayer(Player player);
	public boolean canTakeFromPlayer(Player player);
	public void giveToPlayer(Player player);
	public void takeFromPlayer(Player player);
	public void setInstance(RankWizard instance);
}
