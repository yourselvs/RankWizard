package yourselvs.rankwizard.objects;

import java.util.ArrayList;
import java.util.List;

import yourselvs.rankwizard.actions.RankAction;

public class Rank {
	private String name;
	private String description;
	private boolean mainClassOnly;
	private boolean forceRankupOnly;
	private List<RankAction> requirements;
	private List<RankAction> rewards;
	
	public Rank(String name) {
		this.name = name;
		description = "";
		mainClassOnly = false;
		forceRankupOnly = false;
		requirements = new ArrayList<RankAction>();
		rewards = new ArrayList<RankAction>();
	}
	
	public String getName() {return name;}
	public String getDescription() {return description;}
	public boolean isMainClassOnly() {return mainClassOnly;}
	public boolean isForceRankupOnly() {return forceRankupOnly;}
	public List<RankAction> getRequirements() {return requirements;}
	public List<RankAction> getRewards() {return rewards;}
	
	public void setName(String name) {this.name = name;}
	public void setDescription(String description) {this.description = description;}
	public void setMainClassOnly(boolean mainClassOnly) {this.mainClassOnly = mainClassOnly;}
	public void setForceRankupOnly(boolean forceRankupOnly) {this.forceRankupOnly = forceRankupOnly;}
	public void addRequirement(RankAction requirement) {requirements.add(requirement);}
	public void addReward(RankAction reward) {rewards.add(reward);}
}
