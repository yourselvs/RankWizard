package yourselvs.rankwizard;

import java.util.HashSet;
import java.util.Set;

//TODO add equals override

public class Rank {
	private String name;
	private String description;
	private boolean mainClassOnly;
	private boolean forceRankupOnly;
	private Set<RankAction> requirements;
	private Set<RankAction> rewards;
	
	public Rank(String name) {
		this.name = name;
		description = "";
		mainClassOnly = false;
		forceRankupOnly = false;
		requirements = new HashSet<RankAction>();
		rewards = new HashSet<RankAction>();
	}
	
	public String getName() {return name;}
	public String getDescription() {return description;}
	public boolean isMainClassOnly() {return mainClassOnly;}
	public boolean isForceRankupOnly() {return forceRankupOnly;}
	public Set<RankAction> getRequirements() {return requirements;}
	public Set<RankAction> getRewards() {return rewards;}
	
	public void setName(String name) {this.name = name;}
	public void setDescription(String description) {this.description = description;}
	public void setMainClassOnly(boolean mainClassOnly) {this.mainClassOnly = mainClassOnly;}
	public void setForceRankupOnly(boolean forceRankupOnly) {this.forceRankupOnly = forceRankupOnly;}
	public void addRequirement(RankAction requirement) {requirements.add(requirement);}
	public void addReward(RankAction reward) {rewards.add(reward);}
}
