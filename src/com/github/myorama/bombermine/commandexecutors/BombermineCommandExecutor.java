/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.myorama.bombermine.commandexecutors;

import com.github.myorama.bombermine.Bombermine;
import com.github.myorama.bombermine.models.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Nittero
 */
public class BombermineCommandExecutor implements CommandExecutor {

	private Bombermine plugin;

	public BombermineCommandExecutor(Bombermine instance) {
		this.plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO show help for command and manage sub commands
		// TODO register permissions for each command
		// TODO test if sender is console or player
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		ChatColor msgColor = ChatColor.GREEN;

		// TODO a simplifier (permissions user/moderator/admin)
		
		if (args.length == 0) {
			if (player != null) {
				if (player.hasPermission("bombermine.moderator.join")) {
					player.sendMessage(msgColor + "/bm join [player] <team>");
				} else if (player.hasPermission("bombermine.player.join")) {
					player.sendMessage(msgColor + "/bm join <team>");
				}
				if (player.hasPermission("bombermine.moderator.leave")) {
					player.sendMessage(msgColor + "/bm leave [player]");
				} else if (player.hasPermission("bombermine.player.leave")) {
					player.sendMessage(msgColor + "/bm leave");
				}

				if (player.hasPermission("bombermine.moderator.start")
						|| player.hasPermission("bombermine.moderator.stop")
						|| player.hasPermission("bombermine.moderator.restart")) {
					player.sendMessage(msgColor + "/bm start|stop|restart");
				}

				if (player.hasPermission("bombermine.admin.setspawn")) {
					player.sendMessage(msgColor + "/bm setspawn <team>");
				}
			} else {
				sender.sendMessage(msgColor + "/bm join <player> <team>");
				sender.sendMessage(msgColor + "/bm leave <player>");
				sender.sendMessage(msgColor + "/bm start|stop|restart");
			}
		} else if (args.length > 0) {
			if (args[0].equals("join")) {
				if (args.length == 2) {
					if(player == null){
						sender.sendMessage(msgColor + "You must be a player to join a team");
					} else {
						if(player.hasPermission("bombermine.player.join") || player.hasPermission("bombermine.moderator.join")){
							Team team = plugin.getCtfGame().getTeamById(args[2]);
							if(team == null){
								sender.sendMessage(msgColor + "Team does not exist");
							}
						} else {
							sender.sendMessage(msgColor + "You don't have right to join a team");
						}
					}
					return true;
				} else if (args.length == 3) {
					
				}
			}
		}
		return true;
	}
}
