package com.github.myorama.bombermine.models;

import java.util.ArrayList;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Team {
	private String name;
	private int max;
	private Location spawn;
	private ArrayList<Player> players = new ArrayList<Player>();

	private Block flag;
	private Location flagLoc;
	private Material flagType;
	private ItemStack flagItem;

	/* Constructor */
	public Team(String n)
	{
		name = n;
	}
	
	/* Team constructor from Config */
	public Team(Map<String,Object> config)
	{
		// TODO initialize Team object properties
		try {
			//config.get("id");
			this.name = config.get("name").toString();
			this.max = Integer.parseInt(config.get("max").toString());
			this.flagType = Material.getMaterial(config.get("flagType").toString());
			
			/* on aura du mal pour lui, sauf si on référencie le Bombermine.getServer()
			String[] coords = config.get("spawn").toString().split("/");
			this.spawn = new Location();
					this.getServer().getWorlds()
					coords[1],coords[1],coords[1],coords[1];
			*/
		} catch (Exception e) {
			// TODO: handle exception
		}
		config.get("spawn");
	}

	/* Team methods */
	public void removePlayer(Player p) { players.remove(p); }
	public void addPlayer(Player p) { players.add(p); }
	public ArrayList<Player> getPlayers() { return players; }
	public boolean hasPlayer(Player p) { return players.contains(p); }
	
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