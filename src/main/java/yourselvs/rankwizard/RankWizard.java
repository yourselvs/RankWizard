package yourselvs.rankwizard;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import yourselvs.rankwizard.commands.Cmd;
import yourselvs.rankwizard.commands.CommandParser;
import yourselvs.rankwizard.listeners.PlayerListener;
import yourselvs.rankwizard.utils.DateFormatter;
import yourselvs.rankwizard.utils.Messenger;

//TODO highlight classes youve already joined in /classes

public class RankWizard extends JavaPlugin 
{
	public static final	String fileName = "plugins/RankWizard/rankwizard.ser";
	public static final	String backupFileName = "plugins/RankWizard/backup.ser";
	public static final String version = "0.1";
	
	private final static Object managerLock = new Object();
	private final static Object commandLock = new Object();
	
	private String prefix = "[" + ChatColor.BLUE + ChatColor.BOLD + "RW" + ChatColor.RESET + "] ";
	private String linkPrefix = ChatColor.AQUA + "[" + ChatColor.BLUE + ChatColor.BOLD + "RW" + ChatColor.RESET + ChatColor.AQUA + "]" + ChatColor.RESET + " ";
	private String rankTreeLink;
	private int maxSpecializationClasses;
	private double specializationModifier;
	private double specializationMultiplier;
	
	private static RankManager rankManager;	
	private DateFormatter formatter;
	private Messenger messenger;
	private CommandParser commandParser;
	private Permission perms = null;
	private Economy econ = null;
	
	private Map<Integer, Double> repairVals;
	
	@SuppressWarnings("unused")
	private PlayerListener listener;
    
	@Override
	public void onEnable() {
		saveDefaultConfig();
    	FileConfiguration config = getConfig();
    	
    	rankTreeLink = config.getString("rankTreeLink");
    	maxSpecializationClasses = config.getInt("maxSpecializationClasses");
    	specializationModifier = config.getDouble("specializationModifier");
    	specializationMultiplier = config.getDouble("specializationMultiplier");
    	repairVals = new HashMap<Integer, Double>();
    	repairVals.put(1, config.getDouble("tierOneRepair"));
    	repairVals.put(2, config.getDouble("tierTwoRepair"));
    	repairVals.put(3, config.getDouble("tierThreeRepair"));
    	repairVals.put(4, config.getDouble("tierFourRepair"));
    	
    	formatter = new DateFormatter();
    	messenger = new Messenger(this, prefix, linkPrefix, ChatColor.YELLOW);
    	
    	setupEconomy();
    	setupPermissions();

    	rankManager = readManager(fileName);
    	
    	commandParser = new CommandParser(this);
    	
    	listener = new PlayerListener(this);
	}
	
	public String getRankTreeLink() {return rankTreeLink;}
	public int getMaxSpecializationClasses() {return maxSpecializationClasses;}
	public double getSpecializationModifier() {return specializationModifier;}
	public double getSpecializationMultiplier() {return specializationMultiplier;}
	
	public Messenger getMessenger() {return messenger;}
	public DateFormatter getFormatter() {return formatter;}
	public RankManager getRankManager() {return rankManager;}
	public CommandParser getCommandParser() {return commandParser;}
	public Permission getPerms() {return perms;}
	public Economy getEcon() {return econ;}
	
	public Map<Integer, Double> getRepairVals() {return repairVals;}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Thread t = new Thread(new Runnable() {
	        public void run(){
	        	synchronized(commandLock) {
	        		commandParser.parseCommand(new Cmd(sender, command, label, args));
	        	}
	        }
	    });
		
		t.setName("RankWizard Command Processor");
		t.start();
		
		return true;
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    
    public void resetManager() {
    	rankManager = new RankManager(this);
    }
    
    public static void saveManager() {
    	saveManager(fileName);
    }
    
    public static void backupManager() {
    	saveManager(backupFileName);
    }
    
    public void restoreManager() {
    	rankManager = readManager(backupFileName);
    }
    
    private RankManager readManager(String fileName) {    	
    	try {
			FileInputStream fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			rankManager = (RankManager) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			rankManager = new RankManager(this);
			saveManager(fileName);
			e.printStackTrace();
		}
    	
    	rankManager.setInstance(this);
    	
		return rankManager;
    }
    
    public static void saveManager(String fileName) {
    	Thread t = new Thread(new Runnable() {
	        public void run(){
	        	synchronized(managerLock) {
		        	try {
		    			FileOutputStream fileStream = new FileOutputStream(fileName);
		    			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
		    			
		    			objectStream.writeObject(rankManager);
		    			objectStream.close();
		    		} catch (IOException e) {
		    			e.printStackTrace();
		    		}
	        	}
	        }
	    });
    	
    	t.setName("RankWizard Data Writer");
    	t.start();
    }

}