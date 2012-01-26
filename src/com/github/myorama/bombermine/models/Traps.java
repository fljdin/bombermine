package com.github.myorama.bombermine.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Location;

import com.github.myorama.bombermine.Bombermine;

/**
 * @author myorama
 * 
 * @category Gameplay
 * @version 0.1
 */
public class Traps {
	
	private File file;
	private String traps;
	private float power = 2F;
	
	public Traps(File f) {
		this.file = f;
		load(f);
		
		if (traps == null) {
			traps = "#";
		}
	}
	
	/**
	 * Get power explosion value
	 * @return power value
	 */
	public float getPower() {
		return power;
	}
		
	/**
	 * Load traps coordinates from specific file
	 * @param source file
	 */
	protected void load(File f) {
		try {
			if (!f.exists()) {
				f.createNewFile();
				Bombermine.log.info("[Bombermine] Traps file does not exist, creating...");
			}
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			traps = br.readLine();
			br.close();
		} catch (IOException e) {
			Bombermine.log.warning("[Bombermine] "+e.getMessage());
		}
	}
	
	/**
	 * Save traps coordinates to specific file
	 * @param target file
	 */
	protected void save(File f) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
			bw.write(traps);
			bw.close();
		} catch (IOException e) {
			Bombermine.log.warning("[Bombermine] "+e.getMessage());
		}
	}
	
	/**
	 * Save traps coordinates to default file
	 */
	protected void save() {
		save(file);
	}	
	
	protected String formatLocation(Location loc, String team) {
		return formatLocation(loc) + team;
	}
	
	protected String formatLocation(Location loc) {
		return String.format(";%s/%s/%s/%s/",
				loc.getWorld().getName(),
				loc.getBlockX(),
				loc.getBlockY(),
				loc.getBlockZ());
	}
	
	/**
	 * Method to know if the block is trapped
	 * @param Location of block
	 * @return True or False
	 */
	public boolean isTrapped(Location loc) {
		return (traps.indexOf(formatLocation(loc)) != -1);
	}
	
	/**
	 * Memorize trap coordinates
	 * @param trap location
	 */
	public boolean addTrap(Location loc) {
		if (!isTrapped(loc)) {
			traps += formatLocation(loc);
			save();
			return true;
		}
		return false;
	}
	
	/**
	 * Remove trap coordinates from all existing
	 * @param trap location
	 */
	public boolean removeTrap(Location loc) {
		if (isTrapped(loc)) {
			traps = traps.replace(formatLocation(loc), "");
			save();
			return true;
		}
		return false;
	}
	
	/**
	 * Create an explosion to location
	 * @param location
	 */
	public void explode(Location loc) {
		loc.getWorld().createExplosion(loc, power);
		removeTrap(loc);
	}
}
