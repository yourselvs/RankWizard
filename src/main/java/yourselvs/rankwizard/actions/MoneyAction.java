package yourselvs.rankwizard.actions;

import java.io.Serializable;

import org.bukkit.entity.Player;

import yourselvs.rankwizard.RankWizard;

public class MoneyAction implements RankAction, Serializable {
	private double value;
	private transient RankWizard instance;
	
	public MoneyAction() {
		
	}
	
	public MoneyAction(RankWizard instance, double value) {
		this.value = value;
		this.instance = instance;
	}
	
	public boolean canGiveToPlayer(Player player) {
		return true;
	}

	public boolean canTakeFromPlayer(Player player) {
		try {
			if(value <= instance.getEcon().getBalance(player)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void giveToPlayer(Player player) {
		try {
			instance.getEcon().depositPlayer(player, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void takeFromPlayer(Player player) {
		try {
			instance.getEcon().withdrawPlayer(player, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof MoneyAction;
	}

	public double getAmount() {
		return value;
	}
	
	@Override
	public String toString() {
		return instance.getEcon().format(value);
	}
}
