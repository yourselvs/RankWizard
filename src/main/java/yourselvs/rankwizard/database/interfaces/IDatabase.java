package yourselvs.rankwizard.database.interfaces;

import java.util.List;

import yourselvs.rankwizard.objects.Rank;
import yourselvs.rankwizard.objects.RankClass;
import yourselvs.rankwizard.objects.RankPlayer;

public interface IDatabase {	
	public List<RankClass> getClasses();
	public void removeClass(RankClass classObj);
	public void addClass(RankClass classObj);
	public void updateClass(RankClass classObj);
	
	public List<RankPlayer> getPlayers();
	public void removePlayer(RankPlayer player);
	public void addPlayer(RankPlayer player);
	public void updatePlayer(RankPlayer player);
	
	public List<Rank> getRankes();
	public void removeRank(Rank RankObj);
	public void addRank(Rank RankObj);
	public void updateRank(Rank RankObj);
	
	public String getVersion();
}
