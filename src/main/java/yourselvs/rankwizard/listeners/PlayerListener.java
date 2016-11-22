package yourselvs.rankwizard.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import yourselvs.rankwizard.RankWizard;
import yourselvs.rankwizard.actions.RankAction;
import yourselvs.rankwizard.objects.Rank;
import yourselvs.rankwizard.objects.RankClass;
import yourselvs.rankwizard.objects.RankPlayer;

public class PlayerListener implements Listener{
	private RankWizard instance;
	
	public PlayerListener(RankWizard instance){
		this.instance = instance;
		instance.getServer().getPluginManager().registerEvents(this, instance);
		instance.getMessenger().sendServerMessage("Command listener enabled");
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(instance.getRankManager().getRankPlayer(event.getPlayer().getName()) == null) {
			List<RankClass> classes = new ArrayList<RankClass>();
			classes.add(instance.getRankManager().getDefaultClass());
			Rank rank = classes.get(0).getNextRank(null, true);
			
			RankPlayer player = new RankPlayer(event.getPlayer().getName(), classes, rank);
			
			List<RankAction> rewards = rank.getRewards();
			List<String> msgs = new ArrayList<String>();
			msgs.add("You've ranked up to the rank " + ChatColor.YELLOW + rank.getName() + ChatColor.RESET + ".");
			
			msgs.add("What was given to you:");
			if(rewards.isEmpty()) {
				msgs.add("   - Nothing!");
			}
			for(RankAction action : rewards) {
				action.giveToPlayer(event.getPlayer());
				msgs.add("   - " + action.toString());
			}
			
			player.setRank(rank);
			
			instance.getMessenger().sendMessages(event.getPlayer(), msgs, "Rank Summary ");
			
			instance.getRankManager().getPlayers().add(player);
			RankWizard.saveManager();
			instance.getLogger().info("Player \"" + event.getPlayer().getName() + "\" joined and was automatically placed in rank \"" + rank.getName() + "\".");
		}
	}
	
	@EventHandler
	public void onPlayerItemDamage(PlayerItemDamageEvent event) {
		Player player = event.getPlayer();
		if(instance.getRankManager().getItemRepairPlayers().containsKey(player.getName())) {
			Random rand = new Random(System.currentTimeMillis());
			int playerTier = instance.getRankManager().getItemRepairPlayers().get(player.getName());
			
			if(instance.getRepairVals().get(playerTier) * 100 >= rand.nextInt(100)) {
				event.setDamage(0);
			}
		}
	}
}
