package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

/**
 * @author myorama
 * 
 * @category Gameplay
 * @version 0.1
 */
public class AccelerationListener implements Listener {

	private Bombermine plugin;
	private double acceleration;
	private List<Integer> magicBlocs = new ArrayList<Integer>();

	public AccelerationListener(Bombermine instance) {
		plugin = instance;
		
		// Initialize from config file
		acceleration = plugin.getConfig().getDouble("bombermine.faster.acceleration");
		List<String> blocs = plugin.getConfig().getStringList("bombermine.faster.materials");
		for (String b : blocs) {
			Material bloc = Material.getMaterial(b);
			if (bloc != null) {
				magicBlocs.add(bloc.getId());
			}
		}
	}

	/**
	 * Accelerate Player sprint on configured materials on PlayerMoveEvent
	 * @param event 
	 */
	@EventHandler
	public void accelerate(PlayerMoveEvent event) {
		Location loc = event.getTo().add(0, -0.5, 0);
		Player player = event.getPlayer();

		if (magicBlocs.contains(loc.getBlock().getTypeId()) && player.isSprinting()) {
			Vector vec = player.getLocation().getDirection().multiply(acceleration);
			vec.setY(0.1); // reduce super jump
			player.setVelocity(vec);
		}
	}
}
