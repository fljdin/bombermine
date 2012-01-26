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
	
	private String id = null;
	private String name = null;
	private Location spawn = null;
	private ArrayList<Player> players = null;

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
				this.id = (String)config.get("id");
				this.name = (String)config.get("name");
				String cFlagType = config.get("flag_type").toString();
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
				if(this.id != null && this.name != null && this.flagType != null && this.spawn != null && flagLoc != null) {
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
	
	/* Accessors */
	public String getName() { return name; }

	public Location getSpawnLoc() { return spawn; }
	
	public void setSpawnLoc(Location location) {
		this.spawn = location;
		
		// Location config format: world/x/y/z/yaw/pitch
		StringBuilder sb = new StringBuilder();
		sb.append(location.getWorld().getName());
		sb.append("/");
		sb.append(location.getX());
		sb.append("/");
		sb.append(location.getY());
		sb.append("/");
		sb.append(location.getZ());
		sb.append("/");
		sb.append(location.getYaw());
		sb.append("/");
		sb.append(location.getPitch());
		
		this.ctfGame.saveTeamConfig(this.id, "spawn", sb.toString());
	}
	public Block getFlag() { return flag; }
	public void setFlag(Block b) { flag = b; }

	public ItemStack getFlagItem() { return flagItem; }
	public void setFlagItem(ItemStack f) { flagItem = f; }

	public Location getFlagLoc() { return flagLoc; }
	public void setFlagLoc(Location fl) { flagLoc = fl;	}

	public Material getFlagType() { return flagType; }
	public void setFlagType(Material ft) { flagType = ft; }
}