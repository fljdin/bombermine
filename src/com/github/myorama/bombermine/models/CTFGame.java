package com.github.myorama.bombermine.models;

import com.github.myorama.bombermine.Bombermine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CTFGame {

	private Bombermine plugin;
	private Map<String, Team> teams = new HashMap<String, Team>();
	private Map<String, Long> cooldown = new HashMap<String, Long>();
	private final Object teamsLock = new Object();
	private Integer maxTeamPlayers = null;
	private World world = null;
	private Long seconds;

	public CTFGame(Bombermine instance) {
		plugin = instance;
		seconds = plugin.getConfig().getLong("bombermine.ctfgame.respawn_cooldown");
	}

	public Bombermine getPlugin() {
		return this.plugin;
	}

	/*
	 * Game management
	 */
	public void start() {
	}

	public void stop() {
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

	public void addNewTeam(String color, Team team) {
		teams.put(color.toUpperCase(), team);
	}
	
	public Location getDefaultSpawn() {
		return this.world.getSpawnLocation();
	}

	/**
	 * Read and parse teams configuration
	 */
	public void initialize() {
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
	 * Add the player to the min sized team
	 * @param player to add
	 * @return true if set or false if teams are full
	 */
	public boolean setPlayerTeam(Player player) {
		return this.setPlayerTeam(player, null);
	}
	
	/**
	 * Add a player to the specific team
	 * @param player to add
	 * @param team team
	 * @return true if added, false if team is full
	 */
	public boolean setPlayerTeam(Player player, Team team){
		synchronized(teamsLock){
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
		}
		return false;
	}
	
	/**
	 * Get player current team
	 * @param player
	 * @return team or null if not in a team
	 */
	public Team getPlayerTeam(Player player){
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
	
	public boolean removePlayer(Player player){
		synchronized(teamsLock){
			Team currentTeam = this.getPlayerTeam(player);
			this.plugin.sendBroadcastMessage(player.getName() + " has left his team (" + currentTeam.getName() + ")");
			return currentTeam.removePlayer(player);
		}
	}
	
	/**
	 * Start counting for player cooldown
	 * @param player who waiting
	 */
	public void addCooldown(Player p) {
		cooldown.put(p.getName(), System.currentTimeMillis());
		p.sendMessage(ChatColor.BLUE+"You're dead. You have to wait "+seconds+" seconds.");
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
	 * Get max players by team
	 * @return max players
	 */
	public Integer getMax(){
		return this.maxTeamPlayers;
	}
	
	/**
	 * Set default spawn for players who are not in a team
	 * @param location 
	 */
	public void setDefaultSpawn(Location loc){
		this.world.setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
	public void saveTeamConfig(String id, String key, String value){
		List<Map<String, Object>> configTeams = plugin.getConfig().getMapList("bombermine.teams");
		for (Map<String, Object> teamConfigMap : configTeams) {
			if (teamConfigMap instanceof Map) {
				String cfgId = (String) teamConfigMap.get("id");
				if (id != null) {
					if(cfgId.equals(id)){
						teamConfigMap.remove(key);
						teamConfigMap.put(key, value);
					}
				}
			}
		}
		this.plugin.getConfig().set("bombermine.teams", configTeams);
		this.plugin.saveConfig();
	}
}