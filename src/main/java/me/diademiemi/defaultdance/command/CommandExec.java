package me.diademiemi.defaultdance.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.diademiemi.defaultdance.DefaultDance;
import me.diademiemi.defaultdance.Runner;

/**
 * Command class for listening to defaultdance command
 *
 * @author : diademiemi
 * @created : 2021-09-12
**/
public class CommandExec implements CommandExecutor {

	/**
	 * Method to handle commands
	 *
	 * @param sender	Entity sending the command
	 * @param command	Command
	 * @param label	Command label used
	 * @param args	List of arguments
	 * @return	Boolean of if command was successful
	 */
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (label.equalsIgnoreCase("defaultdance")) {

			if (sender instanceof Player) {
				Player player = (Player) sender;

				if (args.length == 0) {

					if (player.hasPermission("defaultdance.use")) {
						
						Runner.doDefaultDance(player);

					} else player.sendMessage("You are not permitted to execute this command!");

				} else player.sendMessage("Unknown arguments!");

			} else sender.sendMessage("Please run this command as a player!");

		}

		return true;

	}

}
