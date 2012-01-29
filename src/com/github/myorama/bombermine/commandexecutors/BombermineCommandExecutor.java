/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.myorama.bombermine.commandexecutors;

import com.github.myorama.bombermine.Bombermine;
import com.github.myorama.bombermine.models.Team;
import org.bukkit.Bukkit;
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
	private final String ERROR_PLAYER_ONLY = ChatColor.RED + "You must be a player to do this.";
	private final String ERROR_UNAUTHORIZED = ChatColor.RED + "You have not the right to do this";

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
		ChatColor errColor = ChatColor.RED;
		//ChatColor bcColor = ChatColor.GOLD;

		// TODO a simplifier (permissions user/moderator/admin)
		
		if (args.length == 0) {
			if (player != null) {
				if(hasModRights(player)){
					player.sendMessage(msgColor + "/bm join | join [player] <team> | leave [player]");
					player.sendMessage(msgColor + "/bm leave [player]");
					player.sendMessage(msgColor + "/bm start|stop|restart");
				}else if(hasPlayerRights(player)){
					player.sendMessage(msgColor + "/bm join [team]");
					player.sendMessage(msgColor + "/bm leave");
				}
				if (hasAdminRights(player)) {
					player.sendMessage(msgColor + "/bm team spawn <team>");
					player.sendMessage(msgColor + "/bm home");
				}
			} else {
				sender.sendMessage(msgColor + "/bm join <player> <team>");
				sender.sendMessage(msgColor + "/bm leave <player>");
				sender.sendMessage(msgColor + "/bm start|stop|restart");
			}
		} else if (args.length > 0) {
			if (args[0].equals("join")) {
				if(args.length == 1){ // join
					if(player != null){
						if(hasPlayerRights(player)){
							if(!this.plugin.getCtfGame().setPlayerTeam(player)){
								sender.sendMessage("Teams are full");
							}
						}
						else{
							sender.sendMessage(ERROR_UNAUTHORIZED);
						}
					}
					else{
						sender.sendMessage(ERROR_PLAYER_ONLY);
					}
				}
				else if (args.length == 2) { // join <team>
					if(player != null){
						if(hasPlayerRights(player)){
							Team team = plugin.getCtfGame().getTeamByColor(args[1]);
							if(team == null){
								sender.sendMessage(errColor + "Team does not exist");
							}else{
								if(!this.plugin.getCtfGame().setPlayerTeam(player, team)){
									sender.sendMessage(errColor + args[1] + "Team is full");
								}
							}
						}
						else{
							sender.sendMessage(ERROR_UNAUTHORIZED);
						}
					}
					else{
						sender.sendMessage(ERROR_PLAYER_ONLY);
					}
				} else if (args.length == 3) { // join <player> <team>
					if(hasModRights(player)){
						Team team = plugin.getCtfGame().getTeamByColor(args[2]);
						if(team != null){
							Player tPlayer = Bukkit.getServer().getPlayer(args[1]);
							if(tPlayer != null){
								if(!this.plugin.getCtfGame().setPlayerTeam(tPlayer, team)){
									sender.sendMessage(errColor + "Team is full");
								}
							}else{
								sender.sendMessage(errColor + "Player " + args[1] + " is not online");
							}
						}else{
							sender.sendMessage(errColor + "Team id \"" + args[2] + "\" does not exist");
						}
					}
					else{
						sender.sendMessage(ERROR_UNAUTHORIZED);
					}
				} else {
					if(player != null){
						if(hasModRights(player)){
							player.sendMessage(msgColor + "/bm join | join [player] <team>");
						}else if(hasPlayerRights(player)){
							player.sendMessage(msgColor + "/bm join [team]");
						}else{
							sender.sendMessage(ERROR_UNAUTHORIZED);
						}
					} else {
						sender.sendMessage(msgColor + "/bm join <player> <team>");
					}
				}
			}
			else if (args[0].equals("leave")) {
				if(args.length == 1){ // leave
					if(player != null){
						if(hasPlayerRights(player)){
							this.plugin.getCtfGame().removePlayer(player);
						}
					}else{
						sender.sendMessage(errColor + ERROR_PLAYER_ONLY);
					}
				}else if(args.length == 2){ // leave <player>
					Player tPlayer = Bukkit.getServer().getPlayer(args[1]);
					if(tPlayer != null){
						if(hasModRights(player)){
							this.plugin.getCtfGame().removePlayer(player);
						}
					}else{
						sender.sendMessage(errColor + "Player " + args[1] + " is not online");
					}
				}
			}
			else if (args[0].equals("team")) {
				if(args.length > 1){
					if(args[1].equals("spawn")){
						if(player != null){
							if(hasAdminRights(player)){
								if(args.length == 3){ // team spawn <team>
									Team team = this.plugin.getCtfGame().getTeamByColor(args[2]);
									if(team != null){
										team.setSpawnLoc(player.getLocation());
										plugin.getCtfGame().saveTeamConfig(team.getColor(), "spawn", team.getSpawnCoords());
										sender.sendMessage(msgColor + "Team spawn location set for " + args[2] + " team");
									}else{
										sender.sendMessage(errColor + " Team \"" + args[2] + "\" does not exist");
									}
								}else{
									sender.sendMessage(msgColor + "/bm team spawn <team>");
								}
							}else{
								sender.sendMessage(errColor + ERROR_UNAUTHORIZED);
							}
						}else{
							sender.sendMessage(errColor + ERROR_PLAYER_ONLY);
						}
					}
				}else{
					sender.sendMessage(msgColor + "/bm team spawn <team>");
				}
			}else if (args[0].equals("home")) {
				if(args.length == 1){ // home
					if(player != null){
						if(hasAdminRights(player)){
							this.plugin.getCtfGame().setDefaultSpawn(player.getLocation());
							sender.sendMessage(msgColor + "Default spawn location set");
						}else{
							sender.sendMessage(errColor + ERROR_UNAUTHORIZED);
						}
					}else{
						sender.sendMessage(errColor + ERROR_PLAYER_ONLY);
					}
				}else{
					sender.sendMessage(msgColor + "/bm home");
				}
			}
			
		}
		return true;
	}
	
	private boolean hasPlayerRights(Player player){
		if(player == null){
			return true;
		}else{
			return player.hasPermission("bombermine.player") || hasModRights(player);
		}
	}
	
	private boolean hasModRights(Player player){
		if(player == null){
			return true;
		}else{
			return player.hasPermission("bombermine.moderator") || hasAdminRights(player);
		}
	}
	
	private boolean hasAdminRights(Player player){
		if(player == null){
			return true;
		}else{
			return player.hasPermission("bombermine.admin");
		}
	}
}
