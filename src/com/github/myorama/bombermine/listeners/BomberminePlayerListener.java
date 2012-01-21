package com.github.myorama.bombermine.listeners;

import org.bukkit.event.player.*;

import com.github.myorama.bombermine.Bombermine;

public class BomberminePlayerListener extends PlayerListener {
	public Bombermine plugin;
	
	public BomberminePlayerListener(Bombermine instance) {
		plugin = instance;
	}
	
	public void onPlayerMove(PlayerMoveEvent event) {
		
	}
}
