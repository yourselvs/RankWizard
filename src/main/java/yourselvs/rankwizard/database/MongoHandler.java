 package yourselvs.rankwizard.database;

import java.util.List;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.World;

import yourselvs.rankwizard.RankWizard;
import yourselvs.rankwizard.database.interfaces.IDatabase;
import yourselvs.rankwizard.database.interfaces.IMongo;
import yourselvs.rankwizard.objects.Rank;
import yourselvs.rankwizard.objects.RankClass;
import yourselvs.rankwizard.objects.RankPlayer;

public class MongoHandler implements IDatabase {
	private RankWizard plugin;
	private IMongo db;
	private MongoVars v;
	
	public MongoHandler(RankWizard instance, IMongo db){
		this.plugin = instance;
		this.db = db;
		v = new MongoVars();
	}

	public List<RankClass> getClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeClass(RankClass classObj) {
		// TODO Auto-generated method stub
		
	}

	public void addClass(RankClass classObj) {
		// TODO Auto-generated method stub
		
	}

	public void updateClass(RankClass classObj) {
		// TODO Auto-generated method stub
		
	}

	public List<RankPlayer> getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removePlayer(RankPlayer player) {
		// TODO Auto-generated method stub
		
	}

	public void addPlayer(RankPlayer player) {
		// TODO Auto-generated method stub
		
	}

	public void updatePlayer(RankPlayer player) {
		// TODO Auto-generated method stub
		
	}
	
	public List<Rank> getRankes() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeRank(Rank RankObj) {
		// TODO Auto-generated method stub
		
	}

	public void addRank(Rank RankObj) {
		// TODO Auto-generated method stub
		
	}

	public void updateRank(Rank RankObj) {
		// TODO Auto-generated method stub
		
	}

	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Location buildLocation(Document doc){
		String worldString = doc.getString(v.world);
		World world = plugin.getServer().getWorld(worldString);
		double x = doc.getDouble(v.locX);
		double y = doc.getDouble(v.locY);
		double z = doc.getDouble(v.locZ);
		float yaw = doc.getInteger(v.yaw);
		float pitch = doc.getInteger(v.pitch);
		Location loc = new Location(world, x, y, z, yaw, pitch);
		return loc;
	}
}
