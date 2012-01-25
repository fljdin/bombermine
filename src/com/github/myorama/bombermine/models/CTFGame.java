package com.github.myorama.bombermine.models;

import com.github.myorama.bombermine.Bombermine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;

public class CTFGame {

	private Map<String, Team> teams = new HashMap<String, Team>();
	private Bombermine plugin;
	/**
	 * Max player by team
	 */
	private Integer max = null;

	public CTFGame(Bombermine instance) {
		plugin = instance;
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
	 * Get a team by its id
	 *
	 * @param id
	 * @return Team or null if id is not found
	 */
	public Team getTeamById(String id) {
		return teams.get(id);
	}

	public void addNewTeam(String id, Team team) {
		teams.put(id, team);
	}

	public void initialize() {
		// Max team size
		Object cMax = plugin.getConfig().get("bombermine.max");
		if(cMax instanceof Integer) {
			this.max = (Integer)cMax;
		}
		else if(cMax instanceof String) {
			try {
				this.max = new Integer(Integer.parseInt((String)cMax));
			}
			catch(NumberFormatException nfe) {
				this.max = null;
			}
		}
		if(this.max == null){
			this.max = plugin.getConfig().getDefaults().getInt("bombermine.max");
		}
		
		// Reading teams from config file
		this.teams.clear();
		List<Map<String, Object>> configTeams = plugin.getConfig().getMapList("bombermine.teams");
		for (Map<String, Object> teamConfigMap : configTeams) {
			if (teamConfigMap instanceof Map) {
				String id = (String) teamConfigMap.get("id");
				if (id != null) {
					Team cTeam = new Team(this);
					if (cTeam.initialize(teamConfigMap)) {
						this.teams.put(id, cTeam);
					} else {
						Bombermine.log.warning(String.format("Team with id %s failed to load.", id));
					}
				}
			}
		}

		Bombermine.log.info(String.format("Loading %d teams from config file.", teams.size()));
	}

	/**
	 * Add the player to the min sized team
	 * @param player to add
	 * @return Team in wich player has been added or null if 
	 */
	public Team addPlayer(Player player) {
		Team minSizedTeam = null;;
		for (Map.Entry<String, Team> teamEntry : teams.entrySet()) {
			if (minSizedTeam == null) {
				minSizedTeam = teamEntry.getValue();
			} else {
				if(minSizedTeam.getPlayers().size() > teamEntry.getValue().getPlayers().size()){
					minSizedTeam = teamEntry.getValue();
				}
			}
		}
		if(minSizedTeam != null){
			if(!minSizedTeam.addPlayer(player)){
				minSizedTeam = null;
			}
		}
		return minSizedTeam;
	}
	
	/**
	 * Get max players by team
	 * @return max players
	 */
	public Integer getMax(){
		return this.max;
	}
}
