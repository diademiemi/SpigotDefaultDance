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
	public static void freezePlayer(Player player) {
		Location location = player.getLocation();
		World world = location.getWorld();

		ArmorStand container = (ArmorStand) world.spawnEntity(location.subtract(0, 1.13125, 0), EntityType.ARMOR_STAND);

		container.setGravity(false);
		container.setVisible(false);
		container.setInvulnerable(true);

		container.addPassenger(player);

		return;

	}

	public static EulerAngle ea(double dx, double dy, double dz) {
		EulerAngle eangle = new EulerAngle(dx, dy, dz);
		return eangle;
	}

	/**
	 * Bukkit scheduled task to animate armor stand
	 *
	 * @param as	Armor stand to animate
	 * @param p	Player to animate at
	 * @param i	Integer to count amount of times this has looped
	 */
	public static void danceSequence(ArmorStand as, Player p, Integer i) {
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
					case 2:
						as.setRightArmPose(ea(-100, -30, 0.0));
						as.setLeftArmPose(ea(-100, 30, 0.0));
						break;
					case 3:
						as.setRightArmPose(ea(-100, 30, 0));
						as.setLeftArmPose(ea(-100, -30, 0));
						break;
					default:
						active.remove(p.getUniqueId());
						break;

				}

				if (!(active.contains(p.getUniqueId()))) {

					if (p.getVehicle() != null && p.getVehicle() instanceof ArmorStand) {

						Entity vehicle = p.getVehicle();
						vehicle.eject();
						vehicle.remove();

					}

					as.remove();
					return;

				}

				danceSequence(as, p, i + 1);

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

			active.add(player.getUniqueId());

			danceSequence(actor, player, 0); 

	}
	/**
	 * Make this player default dance
	 * 
	 * @param player	Player to make dance
	 */
	public static void doDefaultDance(Player player) {

		if (active.contains(player.getUniqueId())) {

			Entity vehicle = player.getVehicle();

			active.remove(player.getUniqueId());

			vehicle.eject();
			vehicle.remove();			

			return;

		}

		if (!player.isOnGround()) {
			player.sendMessage("You need to be on the ground!");
			return;
		}

		freezePlayer(player);
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

		active.remove(player.getUniqueId());

		Entity vehicle = event.getDismounted();
		vehicle.remove();


	}

}
