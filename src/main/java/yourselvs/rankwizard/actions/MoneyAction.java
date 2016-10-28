package yourselvs.rankwizard.actions;

import java.math.BigDecimal;

import org.bukkit.entity.Player;

import net.ess3.api.Economy;

public class MoneyAction implements RankAction {
	private BigDecimal value;
	
	public MoneyAction(BigDecimal value) {
		this.value = value;
	}
	
	public boolean canGiveToPlayer(Player player) {
		return true;
	}

	public boolean canTakeFromPlayer(Player player) {
		try {
			if(!Economy.hasLess(player.getName(), value)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void giveToPlayer(Player player) {
		try {
			Economy.add(player.getName(), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void takeFromPlayer(Player player) {
		try {
			Economy.substract(player.getName(), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
