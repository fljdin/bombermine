/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.myorama.bombermine.commandexecutors;

import com.github.myorama.bombermine.Bombermine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		// TODO show help for command and manage sub commands
		// TODO register permissions for each command
		return false;
	}
	
}
