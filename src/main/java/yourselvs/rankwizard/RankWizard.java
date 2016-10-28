package yourselvs.rankwizard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import yourselvs.rankwizard.commands.Cmd;
import yourselvs.rankwizard.commands.CommandParser;
import yourselvs.rankwizard.commands.RankManager;
import yourselvs.rankwizard.database.MongoDBStorage;
import yourselvs.rankwizard.database.MongoHandler;
import yourselvs.rankwizard.database.interfaces.IDatabase;
import yourselvs.rankwizard.database.interfaces.IMongo;
import yourselvs.rankwizard.utils.DateFormatter;
import yourselvs.rankwizard.utils.Messenger;

public class RankWizard extends JavaPlugin 
{
	public String version;
	private String prefix; //"[" + ChatColor.BLUE + ChatColor.BOLD + "RW" + ChatColor.RESET + "] "
	private String linkPrefix; //ChatColor.AQUA + "[" + ChatColor.BLUE + ChatColor.BOLD + "RW" + ChatColor.RESET + ChatColor.AQUA + "]" + ChatColor.RESET + " "
	private String rankTreeLink;
	private int maxSpecializationClasses;
	private double specializationModifier;
	private double specializationMultiplier;
	private String defaultClassName;
	
	private boolean dbConfigured = false;
	private IMongo mongo;
	private IDatabase db;
	private RankManager rankManager;	
	private DateFormatter formatter;
	private Messenger messenger;
	private CommandParser commandParser;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
    	FileConfiguration config = getConfig();
    	
    	prefix = config.getString("prefix");
    	linkPrefix = config.getString("linkPrefix");
    	rankTreeLink = config.getString("rankTreeLink");
    	maxSpecializationClasses = config.getInt("maxSpecializationClasses");
    	specializationModifier = config.getDouble("specializationModifier");
    	specializationMultiplier = config.getDouble("specializationModifier");
    	defaultClassName = config.getString("defaultClassName");
    	
    	formatter = new DateFormatter();
    	messenger = new Messenger(this, prefix, linkPrefix);
    	
    	String textUri = config.getString("textUri");
    	String dbName = config.getString("dbName");
    	String collectionName = config.getString("collectionName");
    	
    	if(textUri.equalsIgnoreCase("not configured"))
    		return;
    	if(dbName.equalsIgnoreCase("not configured"))
    		return;
    	if(collectionName.equalsIgnoreCase("not configured"))
    		return;
    	
    	try {
	    	mongo = new MongoDBStorage(textUri, dbName, collectionName);
	    	db = new MongoHandler(this, mongo);
	    } 
    	catch (Exception e) {
    		e.printStackTrace();
    		return;
    	}
    	
    	dbConfigured = true;
    	
    	rankManager = new RankManager(this);
    	rankManager.setDefaultClass(defaultClassName);
    	
    	commandParser = new CommandParser(this);
	}
	
	public String getRankTreeLink() {return rankTreeLink;}
	public int getMaxSpecializationClasses() {return maxSpecializationClasses;}
	public double getSpecializationModifier() {return specializationModifier;}
	public double getSpecializationMultiplier() {return specializationMultiplier;}
	
	public Messenger getMessenger() {return messenger;}
	public DateFormatter getFormatter() {return formatter;}
	public RankManager getRankManager() {return rankManager;}
	public IDatabase getDB() {return db;}
	public CommandParser getCommandParser() {return commandParser;}
	
	public boolean isDBConfigured() {return dbConfigured;}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!dbConfigured)
			sender.sendMessage(prefix + "Your plugin database is not properly configured. Ask your server administrators to fix this issue.");
		else
			commandParser.parseCommand(new Cmd(sender, command, label, args));
		return true;
	}
}