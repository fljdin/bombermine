package com.github.myorama.bombermine.models;

import com.github.myorama.bombermine.Bombermine;
import java.util.ArrayList;
import java.util.Map;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Wool;

public class Team {
	private CTFGame ctfGame;
	
	private String color = null;
	private String name = null;
	private Location spawn = null;
	private ArrayList<Player> players = null;

	private Block flag = null;
	private Wool flagData = null;
	/**
	 * Player running with the flag or null
	 */
	private Player runner = null;
	private final Object flagLock = new Object();

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
				try{
					flagData = new Wool(DyeColor.valueOf(color));
				}
				catch(IllegalArgumentException iae){
					flagData = null;
					Bombermine.log.warning(String.format("%s is not a valid color.", this.color));
					return false;
				}
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
							Location flagLoc = new Location(world, Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Double.parseDouble(coords[3]));
							this.flag = flagLoc.getBlock();
						}
					}
				}
				
				// Checking if config has been read successfully
				if(this.name != null && this.spawn != null && this.flag != null && this.flagData != null) {
					this.players = new ArrayList<Player>();
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
	
	/**
	 * Test if parameter is this team flag
	 * @param block
	 * @return true or false
	 */
	public boolean isTeamFlag(Block block) {
		return (block.getWorld() == this.flag.getWorld()
				&& block.getX() == this.flag.getX()
				&& block.getY() == this.flag.getY()
				&& block.getZ() == this.flag.getZ());
	}
	
	public String getColor() { return color; }
	
	public String getName() { return name; }

	public Location getSpawnLoc() { return spawn; }
	
	public void setSpawnLoc(Location l) {
		this.spawn = l;
		String configSpawn = String.format("%s/%s/%s/%s/%s/%s", 
				spawn.getWorld().getName(),
				spawn.getBlockX(),
				spawn.getBlockY(),
				spawn.getBlockZ(),
				spawn.getYaw(),
				spawn.getPitch());
		this.saveConfig("spawn", configSpawn);
	}
	
	public Wool getFlagData() { return flagData; }
	
	/**
	 * Remove old flag and set the new flag block
	 * @param flagBlock 
	 */
	public void setFlag(Block flagBlock) {
		synchronized(flagLock){
			// Remove old flag
			if(this.flag != null){
				if(this.flag.getType() == Material.WOOL){
					this.flag.setType(Material.AIR);
				}
			}
			// Set new flag
			this.flag = flagBlock;
			this.flag.setType(Material.WOOL);
			this.flag.setData(flagData.getData());

			Location flagLoc = flagBlock.getLocation();
			String configFlagLoc = String.format("%s/%s/%s/%s", 
					flagLoc.getWorld().getName(),
					flagLoc.getBlockX(),
					flagLoc.getBlockY(),
					flagLoc.getBlockZ());;
			this.saveConfig("flag_loc", configFlagLoc);
		}
	}
	
	/**
	 * Get current runner if there is one
	 * @return Player that runs with the flag or null
	 */
	public Player getRunner(){
		return this.runner;
	}
	
	private void saveConfig(String key, String value){
		ctfGame.saveTeamConfig(color, key, value);
	}
}