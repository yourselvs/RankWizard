package yourselvs.rankwizard;

import java.util.ArrayList;
import java.util.List;

public class RankClass {
	private String name;
	private String description;
	private List<Rank> ranks; // in order from first to last
	
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
			if(rank.getName().equals(str)) {
				return rank;
			}
		}
		return null;
	}
	
	public Rank getNextRank(String str) {
		int index = 0;
		
		for(int i = 0; i < ranks.size(); i++) {
			if(ranks.get(i).getName().equals(str)) {
				index = i;
			}
		}
		
		return ranks.get(index + 1);
	}

	public void setName(String name) {this.name = name;}
	public void setDescription(String description) {this.description = description;}
	public void setRanks(List<Rank> ranks) {this.ranks = ranks;}
	public void addRank(Rank rank) {ranks.add(rank);}
	
	public Rank removeRank(String rank) {
		for(int i = 0; i < ranks.size(); i++) {
			if(ranks.get(i).getName().equals(rank)) {
				return ranks.remove(i);
			}
		}
		return null;
	}
	
	public boolean containsRank(String str) {
		for(Rank rank : ranks) {
			if(rank.getName().equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasNextRank(String str) {
		int index = 0;
		
		for(int i = 0; i < ranks.size(); i++) {
			if(ranks.get(i).getName().equals(str)) {
				index = i;
			}
		}
		
		return index < ranks.size() - 1;
	}
}