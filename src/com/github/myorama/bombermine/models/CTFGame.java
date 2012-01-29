package com.github.myorama.bombermine.models;

import com.github.myorama.bombermine.Bombermine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class CTFGame {

	private Bombermine plugin;
	private Map<String, Team> teams = new HashMap<String, Team>();
	private Map<String, Long> cooldown = new HashMap<String, Long>();
	private final Object teamsLock = new Object();
	private Integer maxTeamPlayers = null;
	private World world = null;
	private Long seconds;
	private boolean friendlyFire = false;
	private boolean started = false;

	public CTFGame(Bombermine instance) {
		plugin = instance;
		seconds = plugin.getConfig().getLong("bombermine.ctfgame.respawn_cooldown");
		friendlyFire = plugin.getConfig().getBoolean("bombermine.ctfgame.friendly_fire");
	}

	public Bombermine getPlugin() {
		return this.plugin;
	}
	
	/**
	 * Get max players by team
	 * @return max players
	 */
	public Integer getMax(){
		return this.maxTeamPlayers;
	}
	
	/**
	 * Know if game is started or not
	 * @return true or false
	 */
	public boolean isStarted() {
		return this.started;
	}
	
	public boolean isFriendlyFire() {
		return this.friendlyFire;
	}

	/**
	 * Start the game
	 */
	public void start() {
		synchronized(teamsLock){
			plugin.getTraps().clear();
			this.started = true;
			this.plugin.sendBroadcastMessage("The game has been started");
		}
	}

	/**
	 * Stop the game
	 */
	public void stop() {
		synchronized(teamsLock){
			plugin.getTraps().clear();
			this.started = false;
			this.plugin.sendBroadcastMessage("The game has been stopped");
		}
	}
	
	/**
	 * Restart the game
	 */
	public void restart() {
		synchronized(teamsLock){
			plugin.getTraps().clear();
			this.started = true;
			this.plugin.sendBroadcastMessage("The game has been restarted");
		}
	}

	/**
	 * Get a team by its color
	 *
	 * @param color
	 * @return Team or null if color is not found
	 */
	public Team getTeamByColor(String color) {
		return teams.get(color.toUpperCase());
	}

	/**
	 * Add a team object index by color
	 * 
	 * @param color
	 * @param team object
	 */
	public void addNewTeam(String color, Team team) {
		teams.put(color.toUpperCase(), team);
	}
	
	/**
	 * Set default spawn for players who are not in a team
	 * @param location 
	 */
	public void setDefaultSpawn(Location loc){
		this.world.setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
	/**
	 * Get default spawn of world
	 * @return Location
	 */
	public Location getDefaultSpawn() {
		return this.world.getSpawnLocation();
	}

	/**
	 * Read and parse teams configuration
	 */
	public synchronized void  initialize() {
		// Max team size
		Object cMax = plugin.getConfig().get("bombermine.ctfgame.max_team_players");
		if(cMax instanceof Integer) {
			this.maxTeamPlayers = (Integer)cMax;
		}
		else if(cMax instanceof String) {
			try {
				this.maxTeamPlayers = new Integer(Integer.parseInt((String)cMax));
			}
			catch(NumberFormatException nfe) {
				this.maxTeamPlayers = null;
			}
		}
		if(this.maxTeamPlayers == null){
			this.maxTeamPlayers = plugin.getConfig().getDefaults().getInt("bombermine.ctfgame.max_team_players");
		}
		
		// Reading teams from config file
		this.teams.clear();
		List<Map<String, Object>> configTeams = plugin.getConfig().getMapList("bombermine.teams");
		for (Map<String, Object> teamConfigMap : configTeams) {
			if (teamConfigMap instanceof Map) {
				String color = (String) teamConfigMap.get("color");
				if (color != null) {
					Team cTeam = new Team(this);
					if (cTeam.initialize(teamConfigMap)) {
						this.teams.put(color, cTeam);
					} else {
						Bombermine.log.warning(String.format("Team with color %s failed to load.", color));
					}
				}
			}
		}
		
		// Initialize home world
		String homeWorld = this.plugin.getConfig().getString("bombermine.ctfgame.home_world");
		this.world = plugin.getServer().getWorld(homeWorld);
		if (world == null) {
			world = plugin.getServer().getWorlds().get(0);
			this.plugin.getConfig().set("bombermine.ctfgame.home_world", world.getName());
			this.plugin.saveConfig();
		}

		Bombermine.log.info(String.format("Loading %d teams from config file.", teams.size()));
	}

	/**
	 * Save key/value for a team in config.yml
	 * @param color
	 * @param key
	 * @param value
	 */
	public synchronized void saveTeamConfig(String color, String key, String value){
		List<Map<String, Object>> configTeams = plugin.getConfig().getMapList("bombermine.teams");
		for (Map<String, Object> teamConfigMap : configTeams) {
			if (teamConfigMap instanceof Map) {
				String cfgColor = (String) teamConfigMap.get("color");
				if (color != null) {
					if(cfgColor.equalsIgnoreCase(color)){
						teamConfigMap.remove(key);
						teamConfigMap.put(key, value);
					}
				}
			}
		}
		this.plugin.getConfig().set("bombermine.teams", configTeams);
		this.plugin.saveConfig();
	}

	/**
	 * Add the player to the min sized team
	 * @param player to add
	 * @return true if set or false if teams are full
	 */
	public synchronized boolean setPlayerTeam(Player player) {
		return this.setPlayerTeam(player, null);
	}
	
	/**
	 * Add a player to the specific team
	 * @param player to add
	 * @param team team
	 * @return true if added, false if team is full
	 */
	public synchronized boolean setPlayerTeam(Player player, Team team){
		Team currentPlayerTeam = this.getPlayerTeam(player);
		if(team == null){
			// Try to find the best team to join
			for (Map.Entry<String, Team> candidate : teams.entrySet()) {
				if (team == null) {
					team = candidate.getValue();
				} else {
					int tSize1 = team.getPlayers().size();
					int tSize2 = candidate.getValue().getPlayers().size();
					if(team == currentPlayerTeam){
						tSize1--;
					}
					else if(candidate.getValue() == currentPlayerTeam){
						tSize2--;
					}
					if(tSize1 > tSize2){ // Team candidate is better
						team = candidate.getValue();
					}
				}
			}
		}

		if(team != null && team.getPlayers().size() < this.maxTeamPlayers){
			if(currentPlayerTeam != null){
				currentPlayerTeam.removePlayer(player);
			}
			if(team.addPlayer(player)){
				if(currentPlayerTeam != null){
					this.plugin.sendBroadcastMessage(player.getName() + " moved from team " + currentPlayerTeam.getName() + " to team " + team.getName());
				}else{
					this.plugin.sendBroadcastMessage(player.getName() + " has joined team " + team.getName());
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Get player current team
	 * @param player
	 * @return team or null if not in a team
	 */
	public synchronized Team getPlayerTeam(Player player){
		Team playerTeam = null;
		for (Map.Entry<String, Team> teamEntry : teams.entrySet()) {
			for(Player tPlayer : teamEntry.getValue().getPlayers()){
				if(tPlayer == player){
					playerTeam = teamEntry.getValue();
					break;
				}
			}
		}
		return playerTeam;
	}
	
	public synchronized boolean removePlayer(Player player){
		Team currentTeam = this.getPlayerTeam(player);
		// TODO Manage if player had a flag
		boolean ret = currentTeam.removePlayer(player);
		if(ret){
			this.plugin.sendBroadcastMessage(player.getName() + " has left his team (" + currentTeam.getName() + ")");
		}
		return ret;
	}
	
	/**
	 * Start counting for player cooldown during game
	 * @param player who waiting
	 */
	public void addCooldown(Player p) {
		if (started) {
			cooldown.put(p.getName(), System.currentTimeMillis());
			p.sendMessage(ChatColor.BLUE+"You're dead. You have to wait "+seconds+" seconds.");
		}
	}
	
	/**
	 * Check if player is still immobilized
	 * @param player
	 * @return true or false
	 */
	public boolean isCooldowned(Player p) {

		try {
			Long time = cooldown.get(p.getName());
			Long now = System.currentTimeMillis();
			
			if ((now - time) < seconds*1000) {
				return true;
			}
			cooldown.remove(p.getName());
			
		} catch (NullPointerException e) { }
		
		return false;
	}
	
	/**
	 * Return Team corresponding to a flag Block
	 * @param block
	 * @return Team or null if not found
	 */
	public synchronized Team getTeamByFlag(Block block){
		for (Map.Entry<String, Team> entry : teams.entrySet()) {
			Team team = entry.getValue();
			if(team.isTeamFlag(block)){
				return team;
			}
		}
		return null;
	}
	
	/**
	 * Test if picked up item is a flag.
	 * @param player
	 * @param item
	 * @return true if item is a flag, false otherwise
	 */
	public synchronized void pickUpFlag(Player player, Item item){
		ItemStack itemStack = item.getItemStack();
		if(itemStack.getType() == Material.WOOL){
			Wool wool = (Wool) itemStack.getData();
			Team flagTeam = this.getTeamByColor(wool.getColor().toString());
			Team playerTeam = this.getPlayerTeam(player);
			if(playerTeam == null){
				return;
			}
			if(flagTeam != null){
				if(flagTeam.isLootableFlag()){
					if(playerTeam == flagTeam){
						flagTeam.setRetrieved();
						plugin.sendBroadcastMessage(String.format("%s has retrieved his %s flag !", player.getName(), playerTeam.getColor().toLowerCase()));
					}else{
						flagTeam.setRunner(player);
						plugin.sendBroadcastMessage(String.format("%s has picked up the %s flag !", player.getName(), flagTeam.getColor().toLowerCase()));
					}
				}
			}
			item.remove();
		}
	}
}