package yourselvs.rankwizard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class RankPlayer {
	private Player player;
	private List<RankClass> classes; // first class is default class, second class is main class, last class is current class
	private List<Rank> ranks; // sorted in chronological order
	
	public RankPlayer(Player player, RankClass currClass) {
		this.player = player;
		classes = new ArrayList<RankClass>();
		classes.add(currClass);
		ranks = new ArrayList<Rank>();
		ranks.add(currClass.getRanks().get(0));
	}
	
	public Player getPlayer() {return player;}
	public List<RankClass> getClasses() {return classes;}
	public List<Rank> getRanks() {return ranks;}
	public Rank getRank() {return ranks.get(ranks.size() - 1);}
	
	public RankClass getMainClass() {
		if(classes.size() > 1) {
			return classes.get(1);
		}
		return classes.get(0);
	}
	
	public RankClass getCurrentClass() {
		return classes.get(classes.size() - 1);
	}
}
