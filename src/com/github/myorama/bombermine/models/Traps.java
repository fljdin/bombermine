package com.github.myorama.bombermine.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Location;

import com.github.myorama.bombermine.Bombermine;

/*
 * Cette classe gère la localisation des piéges posés dans le jeu
 * à travers la lecture/écriture dans un fichier
 * 
 * Deuxième version : stockage fichier plat
 */
public class Traps {
	
	private File file;
	private String traps;
	
	public Traps(File f) {
		this.file = f;
		load(f);
		
		if (traps == null) {
			traps = "#";
		}
	}
		
	// Lecture de la première ligne et mise en mémoire
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
	
	// Sauvegarde dans le fichier
	protected void save(File f) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
			bw.write(traps);
			bw.close();
		} catch (IOException e) {
			Bombermine.log.warning("[Bombermine] "+e.getMessage());
		}
	}

	protected void save() {
		save(file);
	}	
	
	/* traps management */
	private String formatLocation(Location loc) {
		return String.format(";%s/%s/%s/%s",
				loc.getWorld().getName(),
				loc.getBlockX(),
				loc.getBlockY(),
				loc.getBlockZ());
	}
	
	public boolean isTrapped(Location loc) {
		return (traps.indexOf(formatLocation(loc)) != -1);
	}
	
	public void addTrap(Location loc) {
		// si le piége n'est pas encore posé, on l'ajoute
		if (!isTrapped(loc)) {
			traps += formatLocation(loc);
			save();
		}
	}
	
	public void removeTrap(Location loc) {
		// si le piége existe, on le supprime
		if (isTrapped(loc)) {
			traps.replace(formatLocation(loc), "");
			save();
		}
	}
}
