package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

@Deprecated
public class BomberminePlayerListener extends PlayerListener {

	private Bombermine plugin;
	
	protected double acceleration;
	protected List<Integer> magicBlocs = new ArrayList<Integer>();

	public BomberminePlayerListener(Bombermine instance) {
		plugin = instance;
		
		// Faster sprint initialize
		acceleration = plugin.getConfig().getDouble("bombermine.faster.acceleration");
		List<String> blocs = plugin.getConfig().getStringList("bombermine.faster.materials");
		
		for (String b : blocs) {
			Material bloc = Material.getMaterial(b);
			if (bloc != null)
				magicBlocs.add(bloc.getId());
		}
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		
		Location loc = event.getTo().add(0, -0.5, 0);
		Player player = event.getPlayer();
		
		/**
		 * Trap triggered event
		 */
		if (plugin.getTraps().isTrapped(loc)) {
			plugin.getTraps().explode(loc);
			plugin.getTraps().removeTrap(loc);
		}
		
		/**
		 * Faster sprint
		 */
		if (magicBlocs.contains(loc.getBlock().getTypeId()) && player.isSprinting()) {
			Vector vec = player.getLocation().getDirection().multiply(acceleration);
            vec.setY(0.1); // reduce super jump
            player.setVelocity(vec);
		}
		
		/**
		 * Player freeze respawn
		 */
		if (plugin.getCtfGame().isCooldowned(player)) {
			event.setTo(event.getFrom());
		}
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		/**
		 * Trap drop event
		 */
		if (event.getMaterial() == Material.SULPHUR && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block clickedBlock = event.getClickedBlock();
			// player can only place a trap on uncovered bloc
			if (clickedBlock.getRelative(BlockFace.UP, 1).getType() == Material.AIR) {
				// new trap location
				boolean trapped = plugin.getTraps().addTrap(clickedBlock.getLocation());
				if (trapped) {
					// remove one sulphur on hand
					int amount = event.getPlayer().getItemInHand().getAmount();
					event.getPlayer().setItemInHand(new ItemStack(Material.SULPHUR, amount - 1));
					event.getPlayer().sendMessage(ChatColor.RED + "You drop an explosive trap!");					
				}
			}
		}
	}

	@Override
	public void onItemHeldChange(PlayerItemHeldEvent event) {
	}
}
