package yourselvs.rankwizard.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import yourselvs.rankwizard.RankWizard;
import yourselvs.rankwizard.actions.RankAction;

public class Rank implements Serializable{
	private String name;
	private String classStr;
	private String description;
	private boolean mainClassOnly;
	private boolean forceRankupOnly;
	private List<RankAction> requirements;
	private List<RankAction> rewards;
	
	public Rank() {
		
	}
	
	public Rank(String name, String classStr) {
		this.name = name;
		this.classStr = classStr;
		description = "";
		mainClassOnly = false;
		forceRankupOnly = false;
		requirements = new ArrayList<RankAction>();
		rewards = new ArrayList<RankAction>();
	}
	
	public String getName() {return name;}
	public String getClassStr() {return classStr;}
	public String getDescription() {return description;}
	public boolean isMainClassOnly() {return mainClassOnly;}
	public boolean isForceRankupOnly() {return forceRankupOnly;}
	public List<RankAction> getRequirements() {return requirements;}
	public List<RankAction> getRewards() {return rewards;}
	
	public void setName(String name) {
		this.name = name;
		RankWizard.saveManager();
	}
	
	public void setDescription(String description) {
		this.description = description;
		RankWizard.saveManager();
	}
	
	public void setMainClassOnly(boolean mainClassOnly) {
		this.mainClassOnly = mainClassOnly;
		RankWizard.saveManager();
	}
	
	public void setForceRankupOnly(boolean forceRankupOnly) {
		this.forceRankupOnly = forceRankupOnly;
		RankWizard.saveManager();
	}
	
	public void addRequirement(RankAction requirement) {
		requirements.add(requirement);
		RankWizard.saveManager();
	}
	
	public void addReward(RankAction reward) {
		rewards.add(reward);
		RankWizard.saveManager();
	}
}
