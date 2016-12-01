package yourselvs.rankwizard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import yourselvs.rankwizard.actions.RankAction;
import yourselvs.rankwizard.objects.Rank;
import yourselvs.rankwizard.objects.RankClass;
import yourselvs.rankwizard.objects.RankPlayer;

public class RankManager implements Serializable {
	transient RankWizard instance;
	private List<RankClass> classes;
	private List<RankPlayer> players;
	private String defaultClass;
	private Map<String, Integer> itemRepairPlayers;
	
	public RankManager() {
		
	}
	
	public RankManager(RankWizard instance) {
		this.instance = instance;
		classes = new ArrayList<RankClass>();
		players = new ArrayList<RankPlayer>();
		itemRepairPlayers = new HashMap<String, Integer>();
	}
	
	public void setInstance(RankWizard instance) {
		this.instance = instance;
		
		for(RankClass classObj : classes) {
			for(Rank rank : classObj.getRanks()) {
				for(RankAction action : rank.getRequirements()) {
					action.setInstance(instance);
				}
				for(RankAction action : rank.getRewards()) {
					action.setInstance(instance);
				}
			}
		}
	}
	
	public Map<String, Integer> getItemRepairPlayers() {
		return itemRepairPlayers;
	}
	
	public void addClass(RankClass newClass) {
		classes.add(newClass);
		RankWizard.saveManager();
	}
	
	public void setDefaultClass(String defaultClass) {
		this.defaultClass = defaultClass;
		RankWizard.saveManager();
	}
	
	public RankClass removeClass(String classStr) {
		for(int i = 0; i < classes.size(); i++) {
			if(classes.get(i).getName().equalsIgnoreCase(classStr)) {
				RankClass classObj = classes.remove(i);
				RankWizard.saveManager();
				return classObj;
			}
		}
		return null;
	}
	
	public boolean doesClassExist(String classStr) {
		for(RankClass classObj : classes) {
			if(classObj.getName().equalsIgnoreCase(classStr)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean doesRankExist(String rankStr) {
		for(RankClass classObj : classes) {
			if(classObj.doesRankExist(rankStr)) {
				return true;
			}
		}
		return false;
	}
	
	public List<RankClass>  getClasses() {
		return classes;
	}
	
	public RankClass getDefaultClass() {
		for(RankClass classObj : classes) {
			if(classObj.getName().equalsIgnoreCase(defaultClass)) {
				return classObj;
			}
		}
		
		return null;
	}
	
	public RankClass getClass(String classStr) {
		for(RankClass classObj : classes) {
			if(classObj.getName().equalsIgnoreCase(classStr)) {
				return classObj;
			}
		}
		
		return null;
	}
	
	public Rank getRank(String rankStr) {
		for(RankClass classObj : classes) {
			if(classObj.doesRankExist(rankStr)) {
				return classObj.getRank(rankStr);
			}
		}
		
		return null;
	}
	
	public List<Rank> getRanks() {
		List<Rank> ranks = new ArrayList<Rank>();
		
		for(RankClass classObj : classes) {
			ranks.addAll(classObj.getRanks());
		}
		
		return ranks;
	}
	
	public List<RankPlayer> getPlayers() { 
		return players;
	}
	
	public boolean doesPlayerExist(String name) {
		for(RankPlayer player : players) {
			if(player.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		
		return false;
	}
	
	public RankPlayer getRankPlayer(String name) {
		for(RankPlayer player : players) {
			if(player.getName().equalsIgnoreCase(name)) {
				return player;
			}
		}
		
		return null;
	}
	
	public List<RankPlayer> getRankPlayers(Rank rank) {
		List<RankPlayer> rankPlayers = new ArrayList<RankPlayer>();
		
		for(RankPlayer player : this.players) {
			if(player.getRank().getName() == rank.getName()) {
				rankPlayers.add(player);
			}
		}
		
		return rankPlayers;
	}
	
	public List<RankPlayer> getClassPlayers(RankClass classObj) {
		List<RankPlayer> classPlayers = new ArrayList<RankPlayer>();
		
		for(RankPlayer player : this.players) {
			if(player.containsClass(classObj)) {
				classPlayers.add(player);
			}
		}
		
		return classPlayers;
	}
	
	public RankPlayer resetPlayer(Player player) {
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).getName().equalsIgnoreCase(player.getName())) {
				players.remove(i);
				List<RankClass> classes = new ArrayList<RankClass>();
				classes.add(instance.getRankManager().getDefaultClass());
				Rank rank = classes.get(0).getNextRank(null, true);
				
				RankPlayer rankPlayer = new RankPlayer(player.getName(), classes, rank);
				
				List<RankAction> rewards = rank.getRewards();
				List<String> msgs = new ArrayList<String>();
				msgs.add("You've ranked up to the rank " + ChatColor.YELLOW + rank.getName() + ChatColor.RESET + ".");
				
				msgs.add("What was given to you:");
				if(rewards.isEmpty()) {
					msgs.add("   - Nothing!");
				}
				for(RankAction action : rewards) {
					action.giveToPlayer(player);
					msgs.add("   - " + action.toString());
				}
				
				rankPlayer.setRank(rank);
				
				instance.getMessenger().sendMessages(player, msgs, "Rank Summary ");
				
				players.add(rankPlayer);
				RankWizard.saveManager();
			}
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public void resetAllPlayers() {
		List<RankPlayer> newPlayers = new ArrayList<RankPlayer>();
		for(int i = 0; i < players.size(); i++) {
			List<RankClass> classes = new ArrayList<RankClass>();
			if(instance.getRankManager() == null) {
				instance.getMessenger().sendServerMessage("RankManager is null");
			}
			if(instance.getRankManager().getDefaultClass() == null) {
				instance.getMessenger().sendServerMessage("Default class is null");
			}
			classes.add(instance.getRankManager().getDefaultClass());
			Rank rank = classes.get(0).getNextRank(null, true);
			
			RankPlayer rankPlayer = new RankPlayer(players.get(i).getName(), classes, rank);
			
			Player player = Bukkit.getPlayer(players.get(i).getName());
			if(player == null) {
				player = Bukkit.getOfflinePlayer(players.get(i).getName()).getPlayer();
			}
			if(player == null) {
				continue;
			}
			
			List<RankAction> rewards = rank.getRewards();
			List<String> msgs = new ArrayList<String>();
			msgs.add("You've ranked up to the rank " + ChatColor.YELLOW + rank.getName() + ChatColor.RESET + ".");
			
			msgs.add("What was given to you:");
			if(rewards.isEmpty()) {
				msgs.add("   - Nothing!");
			}
			for(RankAction action : rewards) {
				action.giveToPlayer(player);
				msgs.add("   - " + action.toString());
			}
			
			rankPlayer.setRank(rank);
			
			instance.getMessenger().sendMessages(player, msgs, "Rank Summary ");
			
			newPlayers.add(rankPlayer);
		}
		players = newPlayers;
		RankWizard.saveManager();
	}
	
	public void addPlayer(String name, List<RankClass> classes, Rank rank) {
		players.add(new RankPlayer(name, classes, rank));
		RankWizard.saveManager();
	}
}
