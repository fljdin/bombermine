package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;
import com.github.myorama.bombermine.models.Team;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

/**
 * @author nittero
 *
 * @category CTF Game
 * @version 0.2
 */
public class FlagListener implements Listener {

	private Bombermine plugin;

	public FlagListener(Bombermine instance) {
		this.plugin = instance;
	}

	/**
	 * Flag management on PlayerPickupItemEvent
	 *
	 * @param event
	 */
	@EventHandler
	public void pickUpFlag(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		Item item = event.getItem();
		ItemStack itemStack = item.getItemStack();
		if (itemStack.getType() == Material.WOOL) {
			Wool wool = (Wool) itemStack.getData();
			synchronized (this.plugin.getCtfGame()) {
				Team flagTeam = this.plugin.getCtfGame().getTeamByColor(wool.getColor().toString());
				Team playerTeam = this.plugin.getCtfGame().getPlayerTeam(player);
				if (playerTeam == null) {
					event.setCancelled(true);
					return;
				}
				if (flagTeam != null) {
					if (flagTeam.isLootableFlag()) {
						if (playerTeam == flagTeam) {
							flagTeam.retrieved();
							plugin.sendBroadcastMessage(String.format("%s has retrieved his %s flag !", player.getName(), playerTeam.getColor().toLowerCase()));
							item.remove();
							event.setCancelled(true);
						} else {
							flagTeam.setRunner(player);
							plugin.sendBroadcastMessage(String.format("%s has picked up the %s flag !", player.getName(), flagTeam.getColor().toLowerCase()));
						}
					}
				}
			}
		}
	}

	/**
	 * No flag break for owner team and spectator
	 *
	 * @param event
	 */
	@EventHandler
	public void preventFlagBreak(BlockBreakEvent event) {

		Player player = event.getPlayer();
		Block block = event.getBlock();

		Team team = plugin.getCtfGame().getPlayerTeam(player);

		if (!this.plugin.getCtfGame().isStarted()) {
			if (player.getGameMode() != GameMode.CREATIVE) {
				event.setCancelled(true);
				player.sendMessage(String.format("%sYou cannot interract while the game is stopped. Please start the game or get Creative mode", ChatColor.RED));
				return;
			}
		} else if (team == null) {
			if (player.getGameMode() != GameMode.CREATIVE) {
				event.setCancelled(true);
				player.sendMessage(String.format("%sYou cannot interract while in spectator. Please join a team or get Creative mode", ChatColor.RED));
				return;
			}
		}

		if (block.getType() == Material.WOOL) {
			Team flagTeam = this.plugin.getCtfGame().getTeamByFlag(block);
			if (flagTeam != null) {
				if (player.getGameMode() == GameMode.CREATIVE) {
					event.setCancelled(true);
					player.sendMessage(String.format("%sPlease use /bm team flag <color> to change flag position", ChatColor.RED));
				} else if (team == flagTeam) { // trying to break own team flag
					player.sendMessage(String.format("%sYou cannot break your own flag", ChatColor.RED));
					event.setCancelled(true);
				}
			} else { // No drop for wool blocks that are not a flag
				event.getBlock().setType(Material.AIR);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void changingGameMode(PlayerGameModeChangeEvent event) {
		if (event.getNewGameMode() == GameMode.SURVIVAL) {
			Player player = event.getPlayer();

			// Cleaning player inventory when changing to Survival mode
			ItemStack[] itemStacks = player.getInventory().getContents();
			for (ItemStack itemStack : itemStacks) {
				if (itemStack != null) {
					player.getInventory().remove(itemStack);
				}
			}

			this.plugin.getCtfGame().spawnPlayer(player);
		}
	}

	@EventHandler
	public void leavingRunner(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		this.plugin.getCtfGame().removePlayer(player);
	}

	@EventHandler
	public void tryWinFlags(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		this.plugin.getCtfGame().tryWinFlags(player, event.getTo());
	}

	@EventHandler
	public void preventFlagSwitch(PlayerItemHeldEvent event) {
		// TODO check if player doesnt delong to flag's team
	}
}
