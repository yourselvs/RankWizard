package yourselvs.rankwizard.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import yourselvs.rankwizard.RankManager;
import yourselvs.rankwizard.RankWizard;
import yourselvs.rankwizard.actions.CommandAction;
import yourselvs.rankwizard.actions.GroupAction;
import yourselvs.rankwizard.actions.MoneyAction;
import yourselvs.rankwizard.actions.RankAction;
import yourselvs.rankwizard.objects.Rank;
import yourselvs.rankwizard.objects.RankClass;
import yourselvs.rankwizard.objects.RankPlayer;

public class CommandParser {
	RankWizard instance;
	
	public CommandParser(RankWizard instance) {
		this.instance = instance;
	}
	
	public void parseCommand(Cmd command) {
		switch(command.label) {
		case "rw": parseRw(command); break;
		case "rankup": parseRankup(command); break;
		case "chooseclass": parseChooseclass(command); break;
		case "rank": parseRank(command); break;
		case "ranks": parseRanks(command); break;
		case "class": parseClass(command); break;
		case "classes": parseClasses(command); break;
		}
	}

	private void parseRw(Cmd command) {		
		if(command.args.length < 1) {
			parseInfo(command); 
			return;
		}
		
		if(!command.sender.hasPermission("rankwizard.admin")) {
			instance.getMessenger().sendMessage(command.sender, "You do not have permission to do this.");
			return;
		}
		
		switch(command.args[0]) {
		case "reset": parseRwReset(command); break;
		case "rank": parseRwRank(command); break;
		case "class": parseRwClass(command); break;
		case "defaultclass": parseRwDefaultclass(command); break;
		case "help": parseRwHelp(command); break;
		case "backup": parseRwBackup(command); break;
		case "restore": parseRwRestore(command); break;
		case "resetplayers": parseRwResetPlayers(command); break;
		case "player": parseRwPlayer(command); break;
		case "health": parseRwHealth(command); break;
		case "repair": parseRwRepair(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseInfo(Cmd command) {
		List<String> msgs = new ArrayList<String>();
		
		msgs.add("" + ChatColor.YELLOW + ChatColor.BOLD + "RankWizard" + ChatColor.RESET + " plugin by " + ChatColor.YELLOW + "yourselvs" + ChatColor.RESET + ".");
		msgs.add("Version " + ChatColor.YELLOW + RankWizard.version);
		msgs.add("Type " + ChatColor.YELLOW + "/rw help" + ChatColor.RESET + " for a list of commands.");
		
		instance.getMessenger().sendMessages(command.sender, msgs, "Plugin Information ");
	}
	
	private void parseRwReset(Cmd command) {
		instance.resetManager();
		RankWizard.saveManager();
		instance.getMessenger().sendServerMessage("RankWizard has been completely reset.");
	}
	
	private void parseRwBackup(Cmd command) {
		RankWizard.backupManager();
		instance.getMessenger().sendServerMessage("RankWizard has been backed up.");
	}
	
	private void parseRwRestore(Cmd command) {
		instance.restoreManager();
		RankWizard.saveManager();
		instance.getMessenger().sendServerMessage("RankWizard has been restored to it's most recent backup.");
	}
	
	private void parseRwResetPlayers(Cmd command) {
		
		instance.getRankManager().resetAllPlayers();
	}
	
	private void parseRwHealth(Cmd command) {
		if(command.sender instanceof Player) {
			instance.getMessenger().sendMessage(command.sender, "You may not do this as a player.");
			return;
		}
		if(command.args.length < 2) {
			instance.getMessenger().sendMessage(command.sender, "You must include a player name.");
			return;
		}
		
		String playerStr = command.args[1];
		Player player = Bukkit.getPlayer(playerStr);
		
		if(player == null) {
			instance.getMessenger().sendMessage(command.sender, "Player does not exist or is offline.");
			return;
		}
		
		if(command.args.length < 3) {
			instance.getMessenger().sendMessage(command.sender, "You must include a number.");
			return;
		}
		
		String healthStr = command.args[2];
		
		if(!isParsableInteger(healthStr)) {
			instance.getMessenger().sendMessage(command.sender, ChatColor.YELLOW + healthStr + ChatColor.RESET + " is not an integer.");
			return;
		}
		
		int health = Integer.parseInt(healthStr);
		
		player.setMaxHealth(health);
		instance.getMessenger().sendMessage(command.sender, "Max health of " + ChatColor.YELLOW + playerStr + ChatColor.RESET + " set to " + ChatColor.YELLOW + healthStr + ChatColor.RESET + ".");
	}
	
	private void parseRwRepair(Cmd command) {
		if(command.sender instanceof Player) {
			instance.getMessenger().sendMessage(command.sender, "You may not do this as a player.");
			return;
		}
		
		if(command.args.length < 2) {
			instance.getMessenger().sendMessage(command.sender, "You must include a player name.");
			return;
		}
		
		String playerStr = command.args[1];
		Player player = Bukkit.getPlayer(playerStr);
		
		if(player == null) {
			instance.getMessenger().sendMessage(command.sender, "Player does not exist or is offline.");
			return;
		}
		
		if(command.args.length < 3) {
			instance.getMessenger().sendMessage(command.sender, "You must include a number.");
			return;
		}
		
		String tierStr = command.args[2];
		
		if(!isParsableInteger(tierStr)) {
			instance.getMessenger().sendMessage(command.sender, ChatColor.YELLOW + tierStr + ChatColor.RESET + " is not an integer.");
			return;
		}
		
		int tier = Integer.parseInt(tierStr);
		
		if(tier <= 0 || tier >= 5) {
			instance.getMessenger().sendMessage(command.sender, "The tier must be between 1 and 4, inclusive.");
			return;
		}
		
		instance.getRankManager().getItemRepairPlayers().put(playerStr, tier);
	}
	
	private void parseRwPlayer(Cmd command) {
		if(command.args.length < 2) {
			parseInfo(command); 
			return;
		}
			
		switch(command.args[1]) {
		case "reset": parseRwPlayerReset(command); break;
		case "list": parseRwPlayerList(command); break;
		case "view": parseRwPlayerView(command); break;
		default: parseInfo(command);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void parseRwPlayerReset(Cmd command) {
		if(command.args.length < 3) {
			instance.getMessenger().sendMessage(command.sender, "You must include a player name.");
			return;
		}
		
		String playerName = command.args[2];
		if(!instance.getRankManager().doesPlayerExist(playerName)) {
			instance.getMessenger().sendMessage(command.sender, "Player " + ChatColor.YELLOW + playerName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		Player player = Bukkit.getPlayer(playerName);
		if(player == null) {
			player = Bukkit.getOfflinePlayer(playerName).getPlayer();
		}
		
		instance.getRankManager().resetPlayer(player);
		instance.getMessenger().sendMessage(command.sender, "Player " + ChatColor.YELLOW + playerName + ChatColor.RESET + " has been reset.");
	}

	private void parseRwPlayerList(Cmd command) {
		List<String> msgs = new ArrayList<String>();
		
		msgs.add("All registered players:");
		for(RankPlayer player : instance.getRankManager().getPlayers()) {
			msgs.add(" - " + ChatColor.YELLOW + player.getName());
			msgs.add("   - Current rank: " + ChatColor.YELLOW + player.getRank().getName());
			msgs.add("   - Current class: " + ChatColor.YELLOW + player.getCurrentClass().getName());
			msgs.add("   - Main class: " + ChatColor.YELLOW + player.getMainClass().getName());
		}
	}

	private void parseRwPlayerView(Cmd command) {
		if(command.args.length < 3) {
			instance.getMessenger().sendMessage(command.sender, "You must include a player name.");
			return;
		}
		
		String playerName = command.args[2];
		
		if(!instance.getRankManager().doesPlayerExist(playerName)) {
			instance.getMessenger().sendMessage(command.sender, "Player " + ChatColor.YELLOW + playerName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		List<String> msgs = new ArrayList<String>();
		
		RankPlayer rankPlayer = instance.getRankManager().getRankPlayer(playerName);
		
		RankClass currClass = rankPlayer.getCurrentClass();
		RankClass mainClass = rankPlayer.getMainClass();
		Rank currRank = rankPlayer.getRank();
		List<RankClass> classes = rankPlayer.getClasses();
		
		msgs.add(" - Current class: " + ChatColor.YELLOW + currClass.getName());
		msgs.add(" - Current rank: " + ChatColor.YELLOW + currRank.getName());
		msgs.add(" - Main class: " + ChatColor.YELLOW + mainClass.getName());
		msgs.add(" - All classes:");
		for(RankClass classObj : classes) {
			msgs.add("   - " + ChatColor.YELLOW + classObj.getName());
		}
		
		instance.getMessenger().sendMessages(command.sender, msgs, playerName + " ");
	}

	private void parseRwRank(Cmd command) {
		if(command.args.length < 2) {
			parseInfo(command); 
			return;
		}
		
		switch(command.args[1]) {
		case "create": parseRwRankCreate(command); break;
		case "description": parseRwRankDescription(command); break;
		case "mainclass": parseRwRankMainclass(command); break;
		case "forcerankup": parseRwRankForcerankup(command); break;
		case "requirement": parseRwRankRequirement(command); break;
		case "reward": parseRwRankReward(command); break;
		case "rename": parseRwRankRename(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseRwRankCreate(Cmd command) {
		if(command.args.length < 3) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[2];
		
		if(instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " already exists.");
			return;
		}
		
		if(command.args.length < 4) {
			instance.getMessenger().sendMessage(command.sender, "You must include a class name.");
			return;
		}
		
		String className = command.args[3];
		
		if(!instance.getRankManager().doesClassExist(className)) {
			instance.getMessenger().sendMessage(command.sender, "Class " + ChatColor.YELLOW + className + ChatColor.RESET + " doesnt exist.");
			return;
		}
		
		instance.getRankManager().getClass(className).addRank(new Rank(rankName, className));
		
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " added to class " + ChatColor.YELLOW + className + ChatColor.RESET + ".");
	}
	
	private void parseRwRankDescription(Cmd command) {
		if(command.args.length < 3) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[2];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		if(command.args.length < 4) {
			instance.getMessenger().sendMessage(command.sender, "You must include a description.");
			return;
		}
		
		String description = command.args[3];
		
		for(int i = 4; i < command.args.length; i++) {
			description += " " + command.args[i];
		}
		
		instance.getRankManager().getRank(rankName).setDescription(description);
		
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " description set to " + ChatColor.YELLOW + description);
	}
	
	private void parseRwRankRename(Cmd command) {
		if(command.args.length < 3) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[2];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		if(command.args.length < 4) {
			instance.getMessenger().sendMessage(command.sender, "You must include a new rank name.");
			return;
		}
		
		String newName = command.args[3];
		
		if(rankName == newName) {
			instance.getMessenger().sendMessage(command.sender, "The new name must be different from the old one.");
		}
		
		if(instance.getRankManager().doesRankExist(newName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + newName + ChatColor.RESET + " already exists.");
			return;
		}
		
		instance.getRankManager().getRank(rankName).setName(newName);
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " renamed to " + ChatColor.YELLOW + newName + ChatColor.RESET + ".");
	}
	
	private void parseRwRankMainclass(Cmd command) {
		if(command.args.length < 3) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[2];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		if(command.args.length < 4) {
			instance.getMessenger().sendMessage(command.sender, "You must include a true/false value.");
			return;
		}
		
		String valueStr = command.args[3];
		
		if(!isParsableBoolean(valueStr)) {
			instance.getMessenger().sendMessage(command.sender, "You must use a true/false value, not " + ChatColor.YELLOW + valueStr);
			return;
		}
		
		boolean value = parseBoolean(valueStr);
		
		instance.getRankManager().getRank(rankName).setMainClassOnly(value);
		
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " mainclassonly set to " + ChatColor.YELLOW + value + ChatColor.RESET + ".");
	}
	
	private void parseRwRankForcerankup(Cmd command) {
		if(command.args.length < 3) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[2];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		if(command.args.length < 4) {
			instance.getMessenger().sendMessage(command.sender, "You must include a true/false value.");
			return;
		}
		
		String valueStr = command.args[3];
		
		if(isParsableBoolean(valueStr)) {
			instance.getMessenger().sendMessage(command.sender, "You must use a true/false value, not " + ChatColor.YELLOW + valueStr);
			return;
		}
		
		boolean value = parseBoolean(valueStr);
		
		instance.getRankManager().getRank(rankName).setForceRankupOnly(value);
		
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " forcerankuponly set to " + ChatColor.YELLOW + value + ChatColor.RESET + ".");
	}
	
	private void parseRwRankRequirement(Cmd command) {
		if(command.args.length < 3) {
			parseInfo(command); 
			return;
		}
		
		switch(command.args[2]) {
		case "list": parseRwRankRequirementList(command); break;
		case "add": parseRwRankRequirementAdd(command); break;
		case "remove": parseRwRankRequirementRemove(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseRwRankRequirementList(Cmd command) {
		List<String> msgs = new ArrayList<String>();
		
		if(command.args.length < 4) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[3];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		Rank rank = instance.getRankManager().getRank(rankName);
		
		if(rank.getRequirements().size() == 0) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " has no requirements.");
			return;
		}
		
		for(int i = 0; i < rank.getRequirements().size(); i++) {
			msgs.add("   - " + rank.getRequirements().get(i).toString());
		}
		
		instance.getMessenger().sendMessages(command.sender, msgs, rankName + " Requirements ");
	}
	
	private void parseRwRankRequirementAdd(Cmd command) {
		if(command.args.length < 4) {
			parseInfo(command); 
			return;
		}
		
		switch(command.args[3]) {
		case "money": parseRwRankRequirementAddMoney(command); break;
		case "item": parseRwRankRequirementAddItem(command); break;
		case "group": parseRwRankRequirementAddGroup(command); break;
		case "command": parseRwRankRequirementAddCommand(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseRwRankRequirementAddCommand(Cmd command) {
		if(command.args.length < 5) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[4];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		if(command.args.length < 6) {
			instance.getMessenger().sendMessage(command.sender, "You must include a command.");
			return;
		}
		
		String commandStr = command.args[5];
		
		for(int i = 6; i < command.args.length; i++) {
			commandStr += " " + command.args[i];
		}
		
		instance.getRankManager().getRank(rankName).addRequirement(new CommandAction(commandStr));
		
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " command added: " + ChatColor.YELLOW + commandStr);
	}
	
	private void parseRwRankRequirementAddMoney(Cmd command) {
		if(command.args.length < 5) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[4];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		if(command.args.length < 6) {
			instance.getMessenger().sendMessage(command.sender, "You must include an amount.");
			return;
		}
		
		String amountStr = command.args[5];
		
		if(!isParsableDouble(amountStr)) {
			instance.getMessenger().sendMessage(command.sender, "You must enter a valid number.");
			return;
		}
		
		double amount = Double.parseDouble(amountStr);
		
		RankAction action = new MoneyAction(instance, amount);
		
		if(instance.getRankManager().getRank(rankName).getRequirements().contains(action)) {
			for(RankAction requirement : instance.getRankManager().getRank(rankName).getRequirements()) {
				if(requirement instanceof MoneyAction) {
					MoneyAction mAction = (MoneyAction) requirement;
					instance.getMessenger().sendMessage(command.sender, ChatColor.YELLOW + instance.getEcon().format(amount) + ChatColor.RESET + " added to previous amount of " + ChatColor.YELLOW + instance.getEcon().format(mAction.getAmount()) + ChatColor.RESET + ".");	
					amount += mAction.getAmount();	
					
					action = new MoneyAction(instance, amount);
				}
			}
		}	
		
		instance.getRankManager().getRank(rankName).getRequirements().remove(new MoneyAction(instance, 0));
		instance.getRankManager().getRank(rankName).addRequirement(action);
		
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " money requirement set to " + ChatColor.YELLOW + instance.getEcon().format(amount));
	}
	
	private void parseRwRankRequirementAddItem(Cmd command) {
		// TODO add items
		
		instance.getMessenger().sendMessage(command.sender, "This feature is not yet available.");
	}
	
	private void parseRwRankRequirementAddGroup(Cmd command) {
		if(command.args.length < 5) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[4];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		if(command.args.length < 6) {
			instance.getMessenger().sendMessage(command.sender, "You must include a group name.");
			return;
		}
		
		String groupName = command.args[5];
		
		boolean groupExists = false;
		
		for(String group : instance.getPerms().getGroups()) {
			if(group.equals(groupName)) {
				groupExists = true;
			}
		}
		
		if(!groupExists) {
			instance.getMessenger().sendMessage(command.sender, "Group " + ChatColor.YELLOW + groupName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		RankAction action = new GroupAction(instance, groupName);
		
		if(instance.getRankManager().getRank(rankName).getRequirements().contains(action)) {
			for(RankAction requirement : instance.getRankManager().getRank(rankName).getRequirements()) {
				if(!(requirement instanceof GroupAction)) {
					continue;
				}
				
				GroupAction gAction = (GroupAction) requirement;
				
				if(gAction.getGroup() == ((GroupAction) action).getGroup()) {
					instance.getMessenger().sendMessage(command.sender, "This rank already has " + ChatColor.YELLOW + groupName + ChatColor.RESET + " as a requirement.");
					return;
				}
			}
		}	
		
		instance.getRankManager().getRank(rankName).addRequirement(action);
		
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " group requirement added: " + ChatColor.YELLOW + groupName);
	}
	
	private void parseRwRankRequirementRemove(Cmd command) {
		if(command.args.length < 4) {
			parseInfo(command); 
			return;
		}
		
		switch(command.args[3]) {
		case "money": parseRwRankRequirementRemoveMoney(command); break;
		case "item": parseRwRankRequirementRemoveItem(command); break;
		case "group": parseRwRankRequirementRemoveGroup(command); break;
		case "command": parseRwRankRequirementRemoveCommand(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseRwRankRequirementRemoveCommand(Cmd command) {
		if(command.args.length < 5) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[4];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		RankAction action = new CommandAction("");
		
		if(!instance.getRankManager().getRank(rankName).getRequirements().contains(action)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't have a command.");
			return;
		}
	
		while(instance.getRankManager().getRank(rankName).getRequirements().contains(action)) {
			instance.getRankManager().getRank(rankName).getRequirements().remove(action);
		}
		
		RankWizard.saveManager();
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " command requirements removed.");
	}
	
	private void parseRwRankRequirementRemoveMoney(Cmd command) {
		if(command.args.length < 5) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[4];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		RankAction action = new MoneyAction(instance, 0);
		
		if(!instance.getRankManager().getRank(rankName).getRequirements().contains(action)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't have a money requirement.");
			return;
		}	
		
		instance.getRankManager().getRank(rankName).getRequirements().remove(action);
		
		RankWizard.saveManager();
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " money requirement removed.");
	}
	
	private void parseRwRankRequirementRemoveItem(Cmd command) {
		instance.getMessenger().sendMessage(command.sender, "This feature is not yet available.");
		// TODO add items
	}
	
	private void parseRwRankRequirementRemoveGroup(Cmd command) {
		if(command.args.length < 5) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[4];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		RankAction action = new GroupAction(instance, "");
		
		if(!instance.getRankManager().getRank(rankName).getRequirements().contains(action)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't have a group requirement.");
			return;
		}	
		
		while(instance.getRankManager().getRank(rankName).getRequirements().contains(action)) {
			instance.getRankManager().getRank(rankName).getRequirements().remove(action);
		}
		
		RankWizard.saveManager();
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " group requirements removed.");
	}
	
	private void parseRwRankReward(Cmd command) {
		if(command.args.length < 3) {
			parseInfo(command); 
			return;
		}
		
		switch(command.args[2]) {
		case "list": parseRwRankRewardList(command); break;
		case "add": parseRwRankRewardAdd(command); break;
		case "remove": parseRwRankRewardRemove(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseRwRankRewardList(Cmd command) {
		List<String> msgs = new ArrayList<String>();
		
		if(command.args.length < 4) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[3];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		Rank rank = instance.getRankManager().getRank(rankName);
		
		for(RankAction reward : rank.getRewards()) {
			msgs.add("   - " + reward.toString());
		}
		
		instance.getMessenger().sendMessages(command.sender, msgs, rank.getName() + " Rewards ");
	}
	
	private void parseRwRankRewardAdd(Cmd command) {
		if(command.args.length < 4) {
			parseInfo(command); 
			return;
		}
		
		switch(command.args[3]) {
		case "money": parseRwRankRewardAddMoney(command); break;
		case "item": parseRwRankRewardAddItem(command); break;
		case "group": parseRwRankRewardAddGroup(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseRwRankRewardAddMoney(Cmd command) {
		if(command.args.length < 5) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[4];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		if(command.args.length < 6) {
			instance.getMessenger().sendMessage(command.sender, "You must include an amount.");
			return;
		}
		
		String amountStr = command.args[5];
		
		if(!isParsableDouble(amountStr)) {
			instance.getMessenger().sendMessage(command.sender, "You must enter a valid number.");
			return;
		}
		
		double amount = Double.parseDouble(amountStr);
		
		RankAction action = new MoneyAction(instance, amount);
		
		if(instance.getRankManager().getRank(rankName).getRequirements().contains(action)) {
			for(RankAction requirement : instance.getRankManager().getRank(rankName).getRequirements()) {
				if(requirement instanceof MoneyAction) {
					MoneyAction mAction = (MoneyAction) requirement;
					instance.getMessenger().sendMessage(command.sender, ChatColor.YELLOW + instance.getEcon().format(amount) + ChatColor.RESET + " added to previous reward amount of " + ChatColor.YELLOW + instance.getEcon().format(mAction.getAmount()) + ChatColor.RESET + ".");	
					amount += mAction.getAmount();	
					
					action = new MoneyAction(instance, amount);
				}
			}
		}	
		
		instance.getRankManager().getRank(rankName).getRequirements().remove(new MoneyAction(instance, 0));
		instance.getRankManager().getRank(rankName).addReward(action);
		
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " money reward set to " + ChatColor.YELLOW + instance.getEcon().format(amount));
	}
	
	private void parseRwRankRewardAddItem(Cmd command) {
		// TODO add items
		
		instance.getMessenger().sendMessage(command.sender, "This feature is not yet available.");
	}
	
	private void parseRwRankRewardAddGroup(Cmd command) {
		if(command.args.length < 5) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[4];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		if(command.args.length < 6) {
			instance.getMessenger().sendMessage(command.sender, "You must include a group name.");
			return;
		}
		
		String groupName = command.args[5];
		
		boolean groupExists = false;
		
		for(String group : instance.getPerms().getGroups()) {
			if(group.equals(groupName)) {
				groupExists = true;
			}
		}
		
		if(!groupExists) {
			instance.getMessenger().sendMessage(command.sender, "Group " + ChatColor.YELLOW + groupName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		RankAction action = new GroupAction(instance, groupName);
		
		if(instance.getRankManager().getRank(rankName).getRequirements().contains(action)) {
			for(RankAction requirement : instance.getRankManager().getRank(rankName).getRequirements()) {
				if(!(requirement instanceof GroupAction)) {
					continue;
				}
				
				GroupAction gAction = (GroupAction) requirement;
				
				if(gAction.getGroup() == ((GroupAction) action).getGroup()) {
					instance.getMessenger().sendMessage(command.sender, "This rank already has " + ChatColor.YELLOW + groupName + ChatColor.RESET + " as a requirement.");
					return;
				}
			}
		}	
		
		instance.getRankManager().getRank(rankName).addReward(action);
		
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " group reward set to " + ChatColor.YELLOW + groupName);
	}
	
	private void parseRwRankRewardRemove(Cmd command) {
		if(command.args.length < 4) {
			parseInfo(command); 
			return;
		}
		
		switch(command.args[3]) {
		case "money": parseRwRankRewardRemoveMoney(command); break;
		case "item": parseRwRankRewardRemoveItem(command); break;
		case "group": parseRwRankRewardRemoveGroup(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseRwRankRewardRemoveMoney(Cmd command) {
		if(command.args.length < 5) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[4];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		RankAction action = new MoneyAction(instance, 0);
		
		if(!instance.getRankManager().getRank(rankName).getRewards().contains(action)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't have a money reward.");
			return;
		}	
		
		instance.getRankManager().getRank(rankName).getRewards().remove(action);
		
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " money requirement removed.");
	}
	
	private void parseRwRankRewardRemoveItem(Cmd command) {
		// TODO add items
		
		instance.getMessenger().sendMessage(command.sender, "This feature is not yet available.");
	}
	
	private void parseRwRankRewardRemoveGroup(Cmd command) {
		if(command.args.length < 5) {
			instance.getMessenger().sendMessage(command.sender, "You must include a rank name.");
			return;
		}
		
		String rankName = command.args[4];
		
		if(!instance.getRankManager().doesRankExist(rankName)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		RankAction action = new GroupAction(instance, "");
		
		if(!instance.getRankManager().getRank(rankName).getRewards().contains(action)) {
			instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " doesn't have a group reward.");
			return;
		}	
		
		instance.getRankManager().getRank(rankName).getRewards().remove(action);
		
		instance.getMessenger().sendMessage(command.sender, "Rank " + ChatColor.YELLOW + rankName + ChatColor.RESET + " group reward removed.");
	}
	
	private void parseRwClass(Cmd command) {
		if(command.args.length < 2) {
			parseInfo(command); 
			return;
		}
		
		switch(command.args[1]) {
		case "create": parseRwClassCreate(command); break;
		case "description": parseRwClassDescription(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseRwClassCreate(Cmd command) {
		if(command.args.length < 3) {
			instance.getMessenger().sendMessage(command.sender, "You must include a name");
			return;
		}
		
		String className = command.args[2];
		
		if(instance.getRankManager().doesClassExist(className)) {
			instance.getMessenger().sendMessage(command.sender, "Class " + ChatColor.YELLOW + className + ChatColor.RESET + " already exists.");
			return;
		}
		
		instance.getRankManager().addClass(new RankClass(className));
		
		instance.getMessenger().sendMessage(command.sender, "Class " + ChatColor.YELLOW + className + ChatColor.RESET + " created.");
	}
	
	private void parseRwClassDescription(Cmd command) {
		if(command.args.length < 3) {
			instance.getMessenger().sendMessage(command.sender, "You must include a name");
			return;
		}
		
		String className = command.args[2];
		
		if(!instance.getRankManager().doesClassExist(className)) {
			instance.getMessenger().sendMessage(command.sender, "Class " + ChatColor.YELLOW + className + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		if(command.args.length < 4) {
			instance.getMessenger().sendMessage(command.sender, "You must include a description");
			return;
		}
		
		
		String description = command.args[3];
		
		for(int i = 4; i < command.args.length; i++) {
			description += " " + command.args[i];
		}
		
		instance.getRankManager().getClass(className).setDescription(description);
		
		instance.getMessenger().sendMessage(command.sender, "Class " + ChatColor.YELLOW + className + ChatColor.RESET + " description set to " + ChatColor.YELLOW + description);
	}
	
	private void parseRwDefaultclass(Cmd command) {
		if(command.args.length < 2) {
			instance.getMessenger().sendMessage(command.sender, "You must include a class.");
			return;
		}
		
		String className = command.args[1];
		
		if(!instance.getRankManager().doesClassExist(className)) {
			instance.getMessenger().sendMessage(command.sender, "Class " + ChatColor.YELLOW + className + ChatColor.RESET + " doesn't exist.");
			return;
		}
		
		instance.getRankManager().setDefaultClass(className);
		
		instance.getMessenger().sendMessage(command.sender, "Default class set to " + ChatColor.YELLOW + className + ChatColor.RESET + ".");
	}
	
	private void parseRwHelp(Cmd command) {
		// TODO
	}
	
	private void parseRankup(Cmd command) {
		if(!(command.sender instanceof Player)) {
			instance.getMessenger().sendMessage(command.sender, "You must be a player to do this.");
			return;
		}
		
		if(!command.sender.hasPermission("rankwizard.general")) {
			instance.getMessenger().sendMessage(command.sender, "You do not have permission to do this.");
			return;
		}
		
		Player player = (Player) command.sender;
		
		RankPlayer rankPlayer = instance.getRankManager().getRankPlayer(player.getName());
		
		RankClass currClass = rankPlayer.getCurrentClass();
		RankClass mainClass = rankPlayer.getMainClass();
		Rank currRank = rankPlayer.getRank();
		List<RankClass> classes = rankPlayer.getClasses();
		List<RankClass> possibleClasses = new ArrayList<RankClass>();
		
		for(RankClass classObj : instance.getRankManager().getClasses()) {
			if(!classes.contains(classObj)) {
				possibleClasses.add(classObj);
			}
		}
		
		Rank nextRank = null;
		boolean inMainClass = false;
		if(currClass.equals(mainClass)) {
			inMainClass = true;
		}
		
		nextRank = currClass.getNextRank(currRank.getName(), inMainClass);
		
		if(nextRank == null) {
			if(possibleClasses.isEmpty()) {
				instance.getMessenger().sendMessage(command.sender, "You have no more classes to rank up in.");
				return;
			}
			
			instance.getMessenger().sendMessage(command.sender, "You need to choose your next class first.");
			return;
		}
		
		boolean canRankUp = true;
		
		for(RankAction action : nextRank.getRequirements()) {
			if(!action.canTakeFromPlayer(player)) {
				instance.getMessenger().sendMessage(player, "You do not satisfy the requirement " + ChatColor.YELLOW + action.toString() + ChatColor.RESET + ". You have not ranked up.");
				canRankUp = false;
			}
		}
		
		for(RankAction action : nextRank.getRewards()) {
			if(!action.canGiveToPlayer(player)) {
				instance.getMessenger().sendMessage(player, "You are unable to receive the reward " + ChatColor.YELLOW + action.toString() + ChatColor.RESET + ". You have not ranked up.");
				canRankUp = false;
			}
		}
		
		if(canRankUp) {
			List<RankAction> requirements = nextRank.getRequirements();
			List<RankAction> rewards = nextRank.getRewards();
			List<String> msgs = new ArrayList<String>();
			msgs.add("You've ranked up to the rank " + ChatColor.YELLOW + nextRank.getName() + ChatColor.RESET + ".");
			
			msgs.add("What was taken from you:");
			if(requirements.isEmpty()) {
				msgs.add("   - Nothing!");
			}
			for(RankAction action : requirements) {
				action.takeFromPlayer(player);
				msgs.add("   - " + action.toString());
			}
			
			msgs.add("What was given to you:");
			if(rewards.isEmpty()) {
				msgs.add("   - Nothing!");
			}
			for(RankAction action : rewards) {
				action.giveToPlayer(player);
				msgs.add("   - " + action.toString());
			}
			
			rankPlayer.setRank(nextRank);
			
			instance.getMessenger().sendMessages(player, msgs, "Rank Summary ");
			RankWizard.saveManager();
		}
	}
	
	private void parseChooseclass(Cmd command) {
		if(!(command.sender instanceof Player)) {
			instance.getMessenger().sendMessage(command.sender, "You must be a player to do this.");
			return;
		}
		
		if(!command.sender.hasPermission("rankwizard.general")) {
			instance.getMessenger().sendMessage(command.sender, "You do not have permission to do this.");
			return;
		}
		
		Player player = (Player) command.sender;
		
		if(command.args.length < 1) {
			instance.getMessenger().sendMessage(command.sender, "You must include a class.");
			return;
		}
		
		RankPlayer rankPlayer = instance.getRankManager().getRankPlayer(player.getName());
		
		RankClass currClass = rankPlayer.getCurrentClass();
		RankClass mainClass = rankPlayer.getMainClass();
		Rank currRank = rankPlayer.getRank();
		List<RankClass> classes = rankPlayer.getClasses();
		List<RankClass> possibleClasses = new ArrayList<RankClass>();
		
		for(RankClass classObj : instance.getRankManager().getClasses()) {
			if(!classes.contains(classObj)) {
				possibleClasses.add(classObj);
			}
		}
		
		Rank nextRank = null;
		boolean inMainClass = false;
		if(currClass.equals(mainClass)) {
			inMainClass = true;
		}
		
		nextRank = currClass.getNextRank(currRank.getName(), inMainClass);
		
		if(nextRank != null) {
			instance.getMessenger().sendMessage(command.sender, "You cannot choose a new class until you have ranked up through your current one.");
			return;
		}
		
		if(possibleClasses.isEmpty()) {
			instance.getMessenger().sendMessage(command.sender, "You have no more classes to rank up in.");
			return;
		}
		
		String classStr = command.args[0];
		
		if(!instance.getRankManager().doesClassExist(classStr)) {
			instance.getMessenger().sendMessage(player, "There is no class called " + ChatColor.YELLOW + classStr);
			return;
		}
		
		RankClass nextClass = null;
		
		for(RankClass classObj : possibleClasses) {
			if(classObj.getName().equals(classStr)) {
				nextClass = classObj;
			}
		}
		
		if(nextClass == null || nextClass.getRanks().isEmpty()) {
			instance.getMessenger().sendMessage(player, "You can't choose this class.");
			return;
		}
		
		rankPlayer.addClass(nextClass);
		
		nextRank = nextClass.getNextRank(null, inMainClass);
		
		boolean canRankUp = true;
		
		for(RankAction action : nextRank.getRequirements()) {
			if(!action.canTakeFromPlayer(player)) {
				instance.getMessenger().sendMessage(player, "You do not satisfy the requirement " + ChatColor.YELLOW + action.toString() + ChatColor.RESET + ". You have not ranked up.");
				canRankUp = false;
			}
		}
		
		for(RankAction action : nextRank.getRewards()) {
			if(!action.canGiveToPlayer(player)) {
				instance.getMessenger().sendMessage(player, "You are unable to receive the reward " + ChatColor.YELLOW + action.toString() + ChatColor.RESET + ". You have not ranked up.");
				canRankUp = false;
			}
		}
		
		if(canRankUp) {
			List<RankAction> requirements = nextRank.getRequirements();
			List<RankAction> rewards = nextRank.getRewards();
			List<String> msgs = new ArrayList<String>();
			msgs.add("You've ranked up to the rank " + ChatColor.YELLOW + nextRank.getName() + ChatColor.RESET + ".");
			
			msgs.add("What was taken from you:");
			if(requirements.isEmpty()) {
				msgs.add("   - Nothing!");
			}
			for(RankAction action : requirements) {
				action.takeFromPlayer(player);
				msgs.add("   - " + action.toString());
			}
			
			msgs.add("What was given to you:");
			if(rewards.isEmpty()) {
				msgs.add("   - Nothing!");
			}
			for(RankAction action : rewards) {
				action.giveToPlayer(player);
				msgs.add("   - " + action.toString());
			}
			
			rankPlayer.setRank(nextRank);
			
			instance.getMessenger().sendMessages(player, msgs, "Rank Summary ");
			RankWizard.saveManager();
		}
	}
	
	private void parseRank(Cmd command) {
		if(!command.sender.hasPermission("rankwizard.general")) {
			instance.getMessenger().sendMessage(command.sender, "You do not have permission to do this.");
			return;
		}
		
		List<String> msgs = new ArrayList<String>();
		if(command.args.length < 1) {			
			if(!(command.sender instanceof Player)) {
				instance.getMessenger().sendMessage(command.sender, "You must be a player to do this.");
				return;
			}
			
			Player player = (Player) command.sender;
			
			RankPlayer rankPlayer = instance.getRankManager().getRankPlayer(player.getName());
			
			if(rankPlayer == null) {
				if(instance.getRankManager().getClasses().isEmpty()) {
					instance.getMessenger().sendMessage(command.sender, "You don't currently have a rank or class. No classes currently exist. Contact your administrator if this is in error.");
				}
				else if(instance.getRankManager().getDefaultClass() == null) {
					instance.getMessenger().sendMessage(command.sender, "You don't currently have a rank or class. No default class currently exists. Contact your administrator if this is in error.");
				}
				else if(instance.getRankManager().getDefaultClass().getRanks().isEmpty()) {
					instance.getMessenger().sendMessage(command.sender, "You don't currently have a rank. No ranks exist in the default class. Contact your administrator if this is in error.");
				}
				else {
					instance.getMessenger().sendMessage(command.sender, "You don't currently have a rank. Try logging off and on again. Contact your administrator if this does not fix anything.");
				}
				
				return;
			}
			
			RankClass currClass = rankPlayer.getCurrentClass();
			RankClass mainClass = rankPlayer.getMainClass();
			Rank currRank = rankPlayer.getRank();
			List<RankClass> classes = rankPlayer.getClasses();
			List<RankClass> possibleClasses = new ArrayList<RankClass>();
			
			for(RankClass classObj : instance.getRankManager().getClasses()) {
				if(!classes.contains(classObj)) {
					possibleClasses.add(classObj);
				}
			}
			
			Rank nextRank = null;
			boolean inMainClass = false;
			if(currClass.equals(mainClass)) {
				inMainClass = true;
			}
			
			nextRank = currClass.getNextRank(currRank.getName(), inMainClass);
			
			msgs.add("Your current rank is " + ChatColor.YELLOW + currRank.getName());
			
			
			if(nextRank == null) {
				if(possibleClasses.isEmpty()) {
					msgs.add("You have no more classes to rank up in. You are at the highest rank.");
				}
				else {
					msgs.add("You need to choose a class for your next rank.");
				}
			}
			else {
				msgs.add("Your next rank is " + ChatColor.YELLOW + nextRank.getName());
		
				for(RankAction action : nextRank.getRequirements()) {
					if(action.equals(new MoneyAction(instance, 0))) {
						MoneyAction mAction = (MoneyAction) action;
						msgs.add(ChatColor.YELLOW + nextRank.getName() + ChatColor.RESET + " costs " + ChatColor.YELLOW + instance.getEcon().format(mAction.getAmount()) + ChatColor.RESET + ".");
					}
					else if(action.equals(new GroupAction(instance, ""))) {
						GroupAction gAction = (GroupAction) action;
						msgs.add(ChatColor.YELLOW + nextRank.getName() + ChatColor.RESET + " requires you to be in group " + ChatColor.YELLOW + gAction.getGroup() + ChatColor.RESET + ".");

						PermissionUser user = PermissionsEx.getUser(player);
						
						if(user.inGroup(gAction.getGroup())) {
							msgs.add("You " + ChatColor.GREEN + "are" + ChatColor.RESET + " in group " + ChatColor.YELLOW + gAction.getGroup() + ChatColor.RESET + ".");
						}
						else {
							msgs.add("   - You are " + ChatColor.RED + "not" + ChatColor.RESET + " in group " + ChatColor.YELLOW + gAction.getGroup() + ChatColor.RESET + ".");
						}
					}
				}
				
				msgs.add("Use " + ChatColor.YELLOW + "/class" + ChatColor.RESET + " for more information.");
				
		
			}
		}
		else {			
			String rankStr = command.args[0];
			
			if(!instance.getRankManager().doesRankExist(rankStr)) {
				instance.getMessenger().sendMessage(command.sender, "This rank doesn't exist. Use " + ChatColor.YELLOW + "/ranks" + ChatColor.RESET + " to see all ranks.");
				return;
			}
			
			Rank rank = instance.getRankManager().getRank(rankStr);
			
			
			msgs.add("Rank name: " + ChatColor.YELLOW + rank.getName());
			msgs.add("Description: " + ChatColor.YELLOW + rank.getDescription());
			msgs.add("Class: " + ChatColor.YELLOW + rank.getClassStr());
			msgs.add("Main class only: " + ChatColor.YELLOW + rank.isMainClassOnly());
			msgs.add("Requirements:");
			
			List<RankAction> requirements = rank.getRequirements();
			
			if(requirements.isEmpty()) {
				msgs.add("   - No requirements");
			}
			else {
				for(RankAction requirement : requirements) {
					msgs.add("   - " + ChatColor.YELLOW + requirement.toString());
				}
			}
			
			msgs.add("Rewards:");
			
			List<RankAction> rewards = rank.getRewards();
			
			if(rewards.isEmpty()) {
				msgs.add("   - No rewards");
			}
			else {
				for(RankAction reward : rewards) {
					msgs.add("   - " + ChatColor.YELLOW + reward.toString());
				}
			}
			
			List<RankPlayer> players = instance.getRankManager().getRankPlayers(rank);
			msgs.add("Total # of players in this rank: " + players.size());
			
			if(!players.isEmpty()) {
				msgs.add("Players in this rank:");
				
				for(RankPlayer player : players) {
					msgs.add("   - " + ChatColor.YELLOW + player.getName());
				}
			}
		}
		
		instance.getMessenger().sendMessages(command.sender, msgs, "Rank Summary ");
	}
	
	private void parseRanks(Cmd command) {
		if(!command.sender.hasPermission("rankwizard.general")) {
			instance.getMessenger().sendMessage(command.sender, "You do not have permission to do this.");
			return;
		}
		
		List<String> msgs = new ArrayList<String>();
		
		List<Rank> ranks = instance.getRankManager().getRanks();
		
		msgs.add("Total # of ranks: " + ranks.size());
		
		if(!ranks.isEmpty()) {
			msgs.add("Ranks: ");
			
			for(Rank rank : ranks) {
				if(rank.getDescription().isEmpty()) {
					msgs.add("   - " + ChatColor.YELLOW + rank.getName() + ChatColor.RESET + " : " + rank.getClassStr());
				}
				else {
					msgs.add("   - " + ChatColor.YELLOW + rank.getName() + ChatColor.RESET + " : " + rank.getClassStr() + " : " + rank.getDescription());
				}
			}
		}
		
		instance.getMessenger().sendMessages(command.sender, msgs, "All Ranks ");
	}
	
	private void parseClass(Cmd command) {
		if(!command.sender.hasPermission("rankwizard.general")) {
			instance.getMessenger().sendMessage(command.sender, "You do not have permission to do this.");
			return;
		}
		
		List<String> msgs = new ArrayList<String>();
		
		if(command.args.length < 1) {
			if(!(command.sender instanceof Player)) {
				instance.getMessenger().sendMessage(command.sender, "You must be a player to do this.");
				return;
			}
			
			Player player = (Player) command.sender;
			
			RankPlayer rankPlayer = instance.getRankManager().getRankPlayer(player.getName());
			RankClass currClass = rankPlayer.getCurrentClass();
			
			List<RankClass> classes = rankPlayer.getClasses();
			List<RankClass> possibleClasses = new ArrayList<RankClass>();
			
			for(RankClass classObj : instance.getRankManager().getClasses()) {
				if(!classes.contains(classObj)) {
					possibleClasses.add(classObj);
				}
			}
			
			
			msgs.add("You are currently in class " + ChatColor.YELLOW + currClass.getName() + ChatColor.RESET + ".");
			
			if(possibleClasses.size() == 0) {
				msgs.add("You have no more classes to rank up in.");
			}
			else {
				msgs.add("These are the classes you have to choose from: ");
				
				for(RankClass nextClass : possibleClasses) {
					msgs.add("   - " + ChatColor.YELLOW + nextClass.getName());
				}
			}
			
			msgs.add("Use " + ChatColor.YELLOW + "/classes" + ChatColor.RESET + " to see all classes.");
		}
		else {
			String classStr = command.args[0];
			
			if(!instance.getRankManager().doesClassExist(classStr)) {
				instance.getMessenger().sendMessage(command.sender, "This class doesn't exist. Use " + ChatColor.YELLOW + "/classes" + ChatColor.RESET + " to see all classes.");
				return;
			}
			
			RankClass classObj = instance.getRankManager().getClass(classStr);
			
			msgs.add("Class name: " + ChatColor.YELLOW + classObj.getName());
			msgs.add("Description: " + ChatColor.YELLOW + classObj.getDescription());		
			
			int numPlayers = 0;
			RankManager manager = instance.getRankManager();
			for(RankPlayer player : manager.getPlayers()) {
				if(player.getCurrentClass().equals(classObj)) {
					numPlayers++;
				}
			}
			
			msgs.add("Total # of ranks: " + ChatColor.YELLOW + classObj.getRanks().size());
			for(Rank rank : classObj.getRanks()) {
				msgs.add("   - " + ChatColor.YELLOW + rank.getName() + ChatColor.RESET + " : " + ChatColor.ITALIC + rank.getDescription());
			}
			
			msgs.add("Total # of players in this class: " + ChatColor.YELLOW + numPlayers);
			for(RankPlayer player : instance.getRankManager().getClassPlayers(classObj)) {
				msgs.add("   - " + ChatColor.YELLOW + player.getName());
			}
		}
		instance.getMessenger().sendMessages(command.sender, msgs, "Class Summary ");
	}
	
	private void parseClasses(Cmd command) {
		if(!command.sender.hasPermission("rankwizard.general")) {
			instance.getMessenger().sendMessage(command.sender, "You do not have permission to do this.");
			return;
		}
		
		List<String> msgs = new ArrayList<String>();
		
		List<RankClass> classes = instance.getRankManager().getClasses();
		RankClass defaultClass = instance.getRankManager().getDefaultClass();
		
		msgs.add("Total # of classes: " + ChatColor.YELLOW + classes.size());
		
		if(classes.size() != 0) {
			if(defaultClass != null) {
				msgs.add("Default class: " + ChatColor.YELLOW + defaultClass.getName());
			}
			else {
				msgs.add("There is no default class.");
			}
			msgs.add("Classes: ");
			for(RankClass classObj : classes) {
				if(classObj.getDescription().isEmpty()) {
					msgs.add("   - " + ChatColor.YELLOW + classObj.getName());
				}
				else {
					msgs.add("   - " + ChatColor.YELLOW + classObj.getName() + ChatColor.RESET + " : " + ChatColor.ITALIC + classObj.getDescription());
				}
			}
		}
		
		instance.getMessenger().sendMessages(command.sender, msgs, "All Classes ");
	}
	
	private boolean isParsableInteger(String input) {
		try {
			Integer.parseInt(input);
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
	
	private boolean isParsableBoolean(String input) {
		return input.equals("true") || input.equals("false");
	}
	
	private boolean parseBoolean(String input) {
		return input.equalsIgnoreCase("true");
	}
	
	private boolean isParsableDouble(String input) {
		try {
			Double.parseDouble(input);
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
}
