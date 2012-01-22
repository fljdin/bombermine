package com.github.myorama.bombermine.models;

import com.github.myorama.bombermine.Bombermine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CTFGame {
	
	private Map<String, Team> teams = new HashMap<String, Team>();
	private Bombermine plugin;

	public CTFGame(Bombermine instance)
	{
		plugin = instance;
	}
	
	/* Game management */
	public void start() { }
	
	public void stop() { }
	
	/* Teams management */
	public Team getTeamById(String id) { return teams.get(id); }
	public void addNewTeam(String id, Team team) { teams.put(id, team); }
	
	public void initialize()
	{
		// Reading teams from config file
		this.teams.clear();
		List<Map<String,Object>> configTeams = plugin.getConfig().getMapList("bombermine.teams");
		for (Map<String,Object> teamConfigMap : configTeams) {
			if(teamConfigMap instanceof Map) {
				String id = (String)teamConfigMap.get("id");
				if(id != null)
				{
					Team cTeam = new Team(this);
					if(cTeam.initialize(teamConfigMap)) {
						this.teams.put(id, cTeam);
					}
					else {
						Bombermine.log.warning(String.format("Team with id %s failed to load.", id));
					}
				}
			}
		}
		
		Bombermine.log.info(String.format("Loading %d teams from config file.", teams.size()));
	}
	
	public Bombermine getPlugin()
	{
		return this.plugin;
	}
}
