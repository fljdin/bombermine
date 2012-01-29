package com.github.myorama.bombermine;

import com.github.myorama.bombermine.commandexecutors.BombermineCommandExecutor;
import com.github.myorama.bombermine.listeners.*;
import com.github.myorama.bombermine.models.CTFGame;
import com.github.myorama.bombermine.models.Traps;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Bombermine extends JavaPlugin {

	public static final Logger log = Logger.getLogger(Bombermine.class.getName());
	private CTFGame ctfGame = null;
	private CommandExecutor cmdExec = null;
	private Traps traps = null;
	
	public Traps getTraps() {
		return traps;
	}

	@Override
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		
		// Register listeners
		pm.registerEvents(new AccelerationListener(this), this);
		pm.registerEvents(new DeathListener(this), this);
		pm.registerEvents(new TrapListener(this), this);
		pm.registerEvents(new SpawnListener(this), this);
		pm.registerEvents(new ItemListener(this), this);
		pm.registerEvents(new FlagListener(this), this);
				
		// Copying and merging default config.yml file to plugin folder
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		// Initialize CTF game
		this.ctfGame = new CTFGame(this);
		this.ctfGame.initialize();
		
		// Instanting CommandExecutor
		this.cmdExec = new BombermineCommandExecutor(this);
		this.getCommand("bm").setExecutor(cmdExec);

		// Initialize Traps feature
		this.traps = new Traps(new File("./plugins/Bombermine/traps.coord"));
		
		log.info("Bombermine plugin has been enabled.");
	}
	
	@Override
	public void onDisable() {
		log.info("Bombermine plugin has been disabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return false;
	}
	
	public CTFGame getCtfGame(){
		return this.ctfGame;
	}
	
	public void sendBroadcastMessage(String msg){
		Bukkit.getServer().getConsoleSender().sendMessage(msg);
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			player.sendMessage(ChatColor.GOLD + msg);
		}
	}
}
