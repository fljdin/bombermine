package com.github.myorama.bombermine.models;

import java.util.HashMap;
import java.util.Map;

public class CTFGame {
	
	private Map<String, Team> teams = new HashMap<String, Team>();
	
	/* Game management */
	public void start() { }
	
	public void stop() { }
	
	/* Teams management */
	public Team getTeamById(String id) { return teams.get(id); }
	public void addNewTeam(String id, Team team) { teams.put(id, team); }
}
