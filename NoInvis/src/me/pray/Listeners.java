package me.pray;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class Listeners implements Listener {

	private NoInvis noe;

	public Listeners(NoInvis noe) {
		this.noe = noe;
	}

	@EventHandler
	public void onPotionSplah(ProjectileLaunchEvent event) {
		if (event.getEntity() instanceof ThrownPotion) {
			ThrownPotion potion = (ThrownPotion) event.getEntity();
			if (potion.getItem().getType() == Material.SPLASH_POTION) {
				Collection<PotionEffect> effects = potion.getEffects();

				for (PotionEffect effect : effects) {
					if (potion.getShooter() instanceof Player) {
						Player player = (Player) potion.getShooter();

						if (!inPvp(player)) {
							return;
						}

						if (player.hasPermission("invis.bypass")) {
							player.sendMessage(format("&cIt appears you bypass the anti-invis!"));
							return;
						}

						if (effect.getType().equals(PotionEffectType.INVISIBILITY)) {
							if (noe.getConfig().getBoolean("no-invis-in-pvp.enabled")) {
								event.setCancelled(true);
								player.sendMessage(format(noe.getConfig().getString("no-invis-in-pvp.message")));
							}
						} else if (effect.getType().equals(PotionEffectType.HARM)) {
							if (noe.getConfig().getBoolean("no-harm-in-pvp.enabled")) {
								event.setCancelled(true);
								player.sendMessage(format(noe.getConfig().getString("no-harm-in-pvp.message")));
							}
						}

					}
				}
			}
		}

	}

	@EventHandler
	public void onPotionDrink(PlayerItemConsumeEvent event) {
		Player player = (Player) event.getPlayer();

		if (!inPvp(player)) {
			return;
		}

		if (player.hasPermission("invis.bypass")) {
			player.sendMessage(format("&cIt appears you bypass the anti-invis!"));
			return;
		}

		if (event.getItem().getItemMeta() instanceof PotionMeta) {
			PotionMeta meta = (PotionMeta) event.getItem().getItemMeta();
			PotionData data = meta.getBasePotionData();
			if (data.getType() == PotionType.INVISIBILITY) {
				if (noe.getConfig().getBoolean("no-invis-in-pvp.enabled")) {
					event.setCancelled(true);
					player.sendMessage(format(noe.getConfig().getString("no-invis-in-pvp.message")));
				}
			}

		}

	}

	public boolean inPvp(Player player) {
		Location pl = player.getLocation();

		com.sk89q.worldedit.util.Location loc = new com.sk89q.worldedit.util.Location(
				BukkitAdapter.adapt(player.getWorld()), pl.getX(), pl.getY(), pl.getZ());
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionQuery query = container.createQuery();
		ApplicableRegionSet set = query.getApplicableRegions(loc);

		LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		if (set.testState(localPlayer, Flags.PVP)) {
			return true;
		}

		return false;
	}

	public String format(String string) {
		return string.replace("&", "§");
	}

}
