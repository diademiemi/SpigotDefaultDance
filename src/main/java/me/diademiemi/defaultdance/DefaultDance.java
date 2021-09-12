package me.diademiemi.defaultdance;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.permissions.Permission;

import me.diademiemi.defaultdance.command.CommandExec;


/**
 * Default Dance plugin
 *
 * @author : diademiemi
 * @created : 2021-09-12
**/
public class DefaultDance extends JavaPlugin {

	/**
	 * Plugin instance
	 */
	private static DefaultDance plugin;

	/**
	 * Plugin manager
	 */
	private static PluginManager pm;

	/**
	 * Run on startup
	 */
	@Override
	public void onEnable() {
		plugin = this;

		pm = getServer().getPluginManager();

		pm.addPermission(new Permission("defaultdance.use"));

		getCommand("defaultdance").setExecutor(new CommandExec());
	}

	/**
	 * Disable plugin
	 */
	@Override
	public void onDisable() {
		plugin = null;
	}

	/**
	 * Get plugin instance
	 *
	 * @return	Plugin instance
	 */
	public static DefaultDance getInstance() {
		return plugin;
	}

}
