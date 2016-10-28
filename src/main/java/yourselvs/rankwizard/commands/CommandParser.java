package yourselvs.rankwizard.commands;

import net.md_5.bungee.api.ChatColor;
import yourselvs.rankwizard.RankWizard;

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
		case "class": parseClass(command); break;
		}
	}

	private void parseRw(Cmd command) {
		if(command.args.length < 1) {
			parseInfo(command); 
			return;
		}
		
		switch(command.args[0]) {
		case "rank": parseRwRank(command); break;
		case "class": parseRwClass(command); break;
		case "defaultclass": parseRwDefaultclass(command); break;
		case "help": parseRwHelp(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseInfo(Cmd command) {
		// TODO
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
		default: parseInfo(command);
		}
	}
	
	private void parseRwRankCreate(Cmd command) {
		
	}
	
	private void parseRwRankDescription(Cmd command) {
		
	}
	
	private void parseRwRankMainclass(Cmd command) {
		
	}
	
	private void parseRwRankForcerankup(Cmd command) {
		
	}
	
	private void parseRwRankRequirement(Cmd command) {
		if(command.args.length < 3) {
			parseInfo(command); 
			return;
		}
		
		switch(command.args[2]) {
		case "list": parseRwRankRequirementList(command); break;
		case "set": parseRwRankRequirementSet(command); break;
		case "remove": parseRwRankRequirementRemove(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseRwRankRequirementList(Cmd command) {
		
	}
	
	private void parseRwRankRequirementSet(Cmd command) {
		if(command.args.length < 4) {
			parseInfo(command); 
			return;
		}
		
		switch(command.args[3]) {
		case "money": parseRwRankRequirementSetMoney(command); break;
		case "item": parseRwRankRequirementSetItem(command); break;
		case "group": parseRwRankRequirementSetGroup(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseRwRankRequirementSetMoney(Cmd command) {
		
	}
	
	private void parseRwRankRequirementSetItem(Cmd command) {
		
	}
	
	private void parseRwRankRequirementSetGroup(Cmd command) {
		
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
		default: parseInfo(command);
		}
	}
	
	private void parseRwRankRequirementRemoveMoney(Cmd command) {
		
	}
	
	private void parseRwRankRequirementRemoveItem(Cmd command) {
		
	}
	
	private void parseRwRankRequirementRemoveGroup(Cmd command) {
		
	}
	
	private void parseRwRankReward(Cmd command) {
		if(command.args.length < 3) {
			parseInfo(command); 
			return;
		}
		
		switch(command.args[2]) {
		case "list": parseRwRankRewardList(command); break;
		case "set": parseRwRankRewardSet(command); break;
		case "remove": parseRwRankRewardRemove(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseRwRankRewardList(Cmd command) {
		
	}
	
	private void parseRwRankRewardSet(Cmd command) {
		if(command.args.length < 4) {
			parseInfo(command); 
			return;
		}
		
		switch(command.args[3]) {
		case "money": parseRwRankRewardSetMoney(command); break;
		case "item": parseRwRankRewardSetItem(command); break;
		case "group": parseRwRankRewardSetGroup(command); break;
		default: parseInfo(command);
		}
	}
	
	private void parseRwRankRewardSetMoney(Cmd command) {
		
	}
	
	private void parseRwRankRewardSetItem(Cmd command) {
		
	}
	
	private void parseRwRankRewardSetGroup(Cmd command) {
		
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
		
	}
	
	private void parseRwRankRewardRemoveItem(Cmd command) {
		
	}
	
	private void parseRwRankRewardRemoveGroup(Cmd command) {
		
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
		
	}
	
	private void parseRwClassDescription(Cmd command) {
		
	}
	
	private void parseRwDefaultclass(Cmd command) {
		
	}
	
	private void parseRwHelp(Cmd command) {
		
	}
	
	private void parseRankup(Cmd command) {
		
	}
	
	private void parseChooseclass(Cmd command) {
		
	}
	
	private void parseRank(Cmd command) {
		
	}
	
	private void parseClass(Cmd command) {
		
	}
}
