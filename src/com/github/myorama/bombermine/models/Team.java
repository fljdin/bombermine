package com.github.myorama.bombermine.models;

import java.util.ArrayList;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Team {
	private CTFGame ctfGame;
	
	private String name = null;
	private Location spawn = null;
	private ArrayList<Player> players = null;
	private final Object playersLock = new Object();

	private Block flag = null;
	private Location flagLoc = null;
	private Material flagType = null;
	private ItemStack flagItem = null;

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
				this.name = (String)config.get("name");
				String cFlagType = config.get("flagType").toString();
				this.flagType = Material.getMaterial(cFlagType);

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
				
				// Checking if config has been read successfully
				if(this.name != null && this.flagType != null && this.spawn != null) {
					this.players = new ArrayList<Player>();
					// TODO initialize this.flag, this.flagLoc, this.flagItem ?
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	/* Team methods */
	public void removePlayer(Player p) {
		synchronized(playersLock){
			players.remove(p);
		}
	}
	
	/**
	 * Add a player to this team
	 * @param player
	 * @return true if added false if team is full
	 */
	public boolean addPlayer(Player p) {
		synchronized(playersLock){
			// TODO Test if player is already in a team
			if(this.players.size() >= this.ctfGame.getMax()){
				players.add(p);
				return true;
			}
			return false;
		}
	}
	public ArrayList<Player> getPlayers() { return players; }
	public boolean hasPlayer(Player p) { return players.contains(p); }
	public void spawnPlayers() {
		for(Player p : players) {
			p.teleport(this.spawn);
		}
	}
	
	/* Accessors */
	public String getName() { return name; }

	public Location getSpawnLoc() { return spawn; }
	public void setSpawnLoc(Location s) { spawn = s; }
	
	public Block getFlag() { return flag; }
	public void setFlag(Block b) { flag = b; }

	public ItemStack getFlagItem() { return flagItem; }
	public void setFlagItem(ItemStack f) { flagItem = f; }

	public Location getFlagLoc() { return flagLoc; }
	public void setFlagLoc(Location fl) { flagLoc = fl;	}

	public Material getFlagType() { return flagType; }
	public void setFlagType(Material ft) { flagType = ft; }

}