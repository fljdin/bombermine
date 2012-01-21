package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

public class BomberminePlayerListener extends PlayerListener {
	public Bombermine plugin;
	
	public BomberminePlayerListener(Bombermine instance) {
		plugin = instance;
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		
	}
}
