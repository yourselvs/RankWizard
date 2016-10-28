package yourselvs.rankwizard.commands;

import org.bukkit.entity.Player;

import yourselvs.rankwizard.RankWizard;
import yourselvs.rankwizard.actions.RankAction;
import yourselvs.rankwizard.objects.Rank;
import yourselvs.rankwizard.objects.RankClass;

public class CommandManager {
	RankWizard instance;
	
	public CommandManager(RankWizard instance) {
		this.instance = instance;
	}
	
	public void createRank(Player player, Rank rank, RankClass classObj) {
		
	}
	
	public void setRankDescription(Player player, Rank rank, String description) {
		
	}
	
	public void setRankMainClassOnly(Player player, Rank rank, boolean mainClassOnly) {
		
	}
	
	public void setForceRankup(Player player, Rank rank, boolean forceRankup) {
		
	}
	
	public void listRequirements(Player player, Rank rank) {
		
	}
	
	public void addRequirement(Player player, Rank rank, RankAction requirement) {
		
	}
	
	public void removeRequirement(Player player, Rank rank, int id) {
		
	}
	
	public void listRewards(Player player, Rank rank) {
		
	}
	
	public void addReward(Player player, Rank rank, RankAction reward) {
		
	}
	
	public void removeReward(Player player, Rank rank, int id) {
		
	}
	
	public void createClass(Player player, String name) {
		
	}
	
	public void setClassDescription(Player player, String name, String description) {
		
	}
	
	public void setDefaultClass(Player player, String name) {
		
	}
	
	public void rankup(Player player) {
		
	}
	
	public void chooseClass(Player player, String classStr) {
		
	}
	
	public void viewRank(Player player) {
		
	}
	
	public void viewClass(Player player) {
		
	}
}
