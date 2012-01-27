package com.github.myorama.bombermine.models;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.material.Wool;

public class Team {
	private CTFGame ctfGame;
	
	private String color = null;
	private String name = null;
	private Location spawn = null;
	private ArrayList<Player> players = null;

	private Wool flag = null;
	private Location flagLoc = null;

	/**
	 * Constructor with CTFGame instance
	 * @param ctfGame instance
	 */
	public Team(CTFGame ctfGame)
	{
		this.ctfGame = ctfGame;
	}
	
	/**
	 * Initialize the Team from config
	 * @param config map
	 * @return true if initialized, false otherwise
	 */
	public boolean initialize(Map<String,Object> config)
	{
		if(config != null){
			try {
				this.color = ((String)config.get("color")).toUpperCase();
				this.name = (String)config.get("name");

				String[] coords = config.get("spawn").toString().split("/");
				if(coords.length > 0) {
					World world = this.ctfGame.getPlugin().getServer().getWorld(coords[0]);
					if(world != null){
						if(coords.length == 4) {
							this.spawn = new Location(world, Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Double.parseDouble(coords[3]));
						}
						else if(coords.length == 6) {
							this.spawn = new Location(world, Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Double.parseDouble(coords[3]), Float.parseFloat(coords[4]), Float.parseFloat(coords[5]));
						}
					}
				}
				
				coords = config.get("flag_loc").toString().split("/");
				if(coords.length > 0) {
					World world = this.ctfGame.getPlugin().getServer().getWorld(coords[0]);
					if(world != null){
						if(coords.length == 4) {
							this.flagLoc = new Location(world, Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Double.parseDouble(coords[3]));
						}
					}
				}
				
				// Checking if config has been read successfully
				if(this.name != null && this.spawn != null && flagLoc != null) {
					this.players = new ArrayList<Player>();
					// TODO initialize this.flag, this.flagItem
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	/**
	 * Remove the specified player from tis team
	 * @param player
	 * @return true if added
	 */
	public boolean removePlayer(Player player) {
		return players.remove(player);
	}
	
	/**
	 * Add a player to this team. It does not check if player is already in
	 * @param player
	 * @return true if added
	 */
	public boolean addPlayer(Player p) {
		return players.add(p);
	}
	
	public ArrayList<Player> getPlayers() { return players; }
	public boolean hasPlayer(Player p) { return players.contains(p); }
	public void spawnPlayers() {
		for(Player p : players) {
			p.teleport(this.spawn);
		}
	}
	
	public boolean isTeamFlag(Wool wool) {
		return wool.getColor().toString().equals(color);
	}
	
	/* Accessors */
	public String getColor() { return color; }
	public String getName() { return name; }

	public Location getSpawnLoc() { return spawn; }
	public void setSpawnLoc(Location l) { spawn = l; }
	
	public Wool getFlag() { return flag; }
	public void setFlag(Wool w) { flag = w; }

	public Location getFlagLoc() { return flagLoc; }
	public void setFlagLoc(Location fl) { flagLoc = fl;	}
}