package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;

/**
 * @author myorama
 * 
 * @category Gameplay
 * @version 0.2
 */
public class ItemListener implements Listener {

	private Bombermine plugin;

	public ItemListener(Bombermine instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void throwGrenade(PlayerEggThrowEvent event) {
		// TODO catch impact location et perform explosion
	}
}
