package yourselvs.rankwizard.objects;

import java.io.Serializable;
import java.util.List;

import yourselvs.rankwizard.RankWizard;

public class RankPlayer implements Serializable{
	private String player;
	private List<RankClass> classes; // first class is default class, second class is main class, last class is current class
	private Rank rank; // sorted in chronological order
	
	public RankPlayer() {
		
	}
	
	public RankPlayer(String player, List<RankClass> classes, Rank rank) {
		this.player = player;
		this.classes = classes;
		this.rank = rank;
	}
	
	public String getName() {return player;}
	public List<RankClass> getClasses() {return classes;}
	public Rank getRank() {return rank;}
	
	public RankClass getMainClass() {
		if(classes.size() > 1) {
			return classes.get(1);
		}
		return classes.get(0);
	}
	
	public RankClass getCurrentClass() {
		return classes.get(classes.size() - 1);
	}
	
	public void setRank(Rank rank) {
		this.rank = rank;
		RankWizard.saveManager();
	}
	
	public void addClass(RankClass classObj) {
		classes.add(classObj);
		RankWizard.saveManager();
	}

	public boolean containsClass(RankClass classObj) {
		for(RankClass classCompare : classes) {
			if(classObj.getName() == classCompare.getName()) {
				return true;
			}
		}
		
		return false;
	}
}
