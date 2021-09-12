package me.diademiemi.defaultdance;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.EulerAngle;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Runner for Default Dance
 *
 * @author : diademiemi
 * @created : 2021-09-12
**/
public class Runner implements Listener {

	/**
	 * Set that contains players currently dancing
	 */
	private static final Set<UUID> active = new HashSet<>();
	
	/**
	 * Hides or unhides a player
	 * 
	 * @param player	Player to hide/show
	 * @param hidden	Boolean for if player should be hidden
	 */
	public static void hidePlayer(Player player, boolean hidden) {
		if (hidden = true) {

			for (Player pl : Bukkit.getOnlinePlayers()) {
				player.showPlayer(DefaultDance.getInstance(), pl);
			}

			Location location = player.getLocation();
			World world = location.getWorld();

			ArmorStand container = (ArmorStand) world.spawnEntity(location.subtract(0, 1.13125, 0), EntityType.ARMOR_STAND);

			container.setGravity(false);
			container.setVisible(false);
			container.setInvulnerable(true);

			container.addPassenger(player);

			active.add(player.getUniqueId());

			return;

		} else {

			for (Player pl : Bukkit.getOnlinePlayers()) {
				player.hidePlayer(DefaultDance.getInstance(), pl);
			}

			if (active.contains(player.getUniqueId())) {

				active.remove(player.getUniqueId());

				if (player.getVehicle() != null && player.getVehicle() instanceof ArmorStand) {

					Entity vehicle = player.getVehicle();
					vehicle.eject();
					vehicle.remove();

				}

			}

			return;

		}

	}

	public static EulerAngle ea(double dx, double dy, double dz) {
		EulerAngle eangle = new EulerAngle(dx, dy, dz);
		return eangle;
	}

	/**
	 * Bukkit scheduled task to animate armor stand
	 *
	 * @param as	Armor stand to animate
	 * @param id	UUID of player
	 * @param i	Integer to count amount of times this has looped
	 */
	public static void danceSequence(ArmorStand as, UUID id, Integer i) {
		Bukkit.getServer().getScheduler().runTaskLater(DefaultDance.getInstance(), new Runnable(){
			public void run() {

				switch (i) {
					case 0:
						as.setRightArmPose(ea(-45.0, 45.0, 0.0));
						as.setLeftArmPose(ea(-45.0, -45.0, 0.0));
						break;
					case 1:
						as.setRightArmPose(ea(-100, 30, 0));
						as.setLeftArmPose(ea(-100, -30, 0));
						break;

				}

				if (active.contains(id)) {

					if (i < 2) {

						danceSequence(as, id, i + 1);

					} else {

						as.eject();
						as.remove();
						active.remove(id);

					}

				}

			}

		},5L);

	}


	/**
	 * Creates an armor stand at this players location which will dance
	 *
	 * @param player	Player to run at
	 */
	public static void dance(Player player) {

			Location location = player.getLocation();
			World world = location.getWorld();

			ArmorStand actor = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);

			actor.setGravity(false);
			actor.setVisible(true);
			actor.setInvulnerable(true);
			actor.setArms(true);

			danceSequence(actor, player.getUniqueId(), 0); 

	}
	/**
	 * Make this player default dance
	 * 
	 * @param player	Player to make dance
	 */
	public static void doDefaultDance(Player player) {

		if (active.contains(player.getUniqueId())) {
			
			active.remove(player.getUniqueId());

			Entity vehicle = player.getVehicle();

			vehicle.eject();
			vehicle.remove();
			
			return;

		}

		if (!player.isOnGround()) {
			player.sendMessage("You need to be on the ground!");
			return;
		}

		hidePlayer(player, true);
		dance(player);

	}

	/**
	 * Listen to entity dismount events to cancel dancing
	 *
	 * @param event	Event of entity dismount
	 */
	@EventHandler
	public void onDismount(EntityDismountEvent event) {
		Entity entity = event.getEntity();

		if (!(entity instanceof Player)) {
			return;
		}

		Player player = (Player) entity;

		if (!(event.getDismounted() instanceof ArmorStand)) {
			return;
		}

		if (!active.contains(player.getUniqueId())) {
			return;
		}

		Entity vehicle = event.getDismounted();
		vehicle.eject();
		vehicle.remove();
		active.remove(player.getUniqueId());

	}

}
