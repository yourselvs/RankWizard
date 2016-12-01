package yourselvs.rankwizard.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import yourselvs.rankwizard.RankWizard;

public class RankClass implements Serializable {
	private String name;
	private String description;
	private List<Rank> ranks; // in order from first to last
	
	public RankClass() {
		
	}
	
	public RankClass(String name) {
		this.setName(name);
		description = "";
		ranks = new ArrayList<Rank>();
	}

	public String getName() {return name;}
	public String getDescription() {return description;}
	public List<Rank> getRanks() {return ranks;}
	
	public Rank getRank(String str) {
		for(Rank rank : ranks) {
			if(rank.getName().equalsIgnoreCase(str)) {
				return rank;
			}
		}
		return null;
	}
	
	public Rank getNextRank(String str, boolean mainClass) {
		
		if(!mainClass) {
			int index = -1;
			
			List<Rank> notMainRanks = new ArrayList<Rank>();
			for(Rank rank : ranks) {
				if(!rank.isMainClassOnly()) {
					notMainRanks.add(rank);
				}
			}
			
			if(str == null) {
				return notMainRanks.get(0);
			}
			
			for(int i = 0; i < notMainRanks.size() - 1; i++) {
				if(notMainRanks.get(i).getName().equalsIgnoreCase(str)) {
					index = i;
				}
			}
			
			if(index == -1) {
				return null;
			}
			
			return notMainRanks.get(index + 1);
		}
		else {
			int index = -1;
			
			if(str == null) {
				return ranks.get(0);
			}
			
			for(int i = 0; i < ranks.size() - 1; i++) {
				if(ranks.get(i).getName().equalsIgnoreCase(str)) {
					index = i;
				}
			}
			
			if(index == -1) {
				return null;
			}
			
			return ranks.get(index + 1);
		}
	}

	public void setName(String name) {
		this.name = name; 
		RankWizard.saveManager();
	}
	
	public void setDescription(String description) {
		this.description = description; 
		RankWizard.saveManager();
	}
	
	public void setRanks(List<Rank> ranks) {
		this.ranks = ranks; 
		RankWizard.saveManager();
	}
	
	public void addRank(Rank rank) {
		ranks.add(rank); 
		RankWizard.saveManager();
	}
	
	public Rank removeRank(String rank) {
		for(int i = 0; i < ranks.size(); i++) {
			if(ranks.get(i).getName().equalsIgnoreCase(rank)) {
				Rank removedRank = ranks.remove(i);
				RankWizard.saveManager();
				return removedRank;
			}
		}
		return null;
	}
	
	public boolean doesRankExist(String str) {
		for(Rank rank : ranks) {
			if(rank.getName().equalsIgnoreCase(str)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasNextRank(String str) {
		int index = 0;
		
		for(int i = 0; i < ranks.size(); i++) {
			if(ranks.get(i).getName().equalsIgnoreCase(str)) {
				index = i;
			}
		}
		
		return index < ranks.size() - 1;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof RankClass)) {
			return false;
		}
		
		RankClass classObj = (RankClass) o;
		
		return classObj.name.equalsIgnoreCase(name);
	}
}