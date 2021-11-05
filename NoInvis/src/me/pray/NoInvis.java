package me.pray;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.session.SessionManager;

public class NoInvis extends JavaPlugin {

	ConsoleCommandSender console = getServer().getConsoleSender();;

	@Override
	public void onEnable() {
		console.sendMessage(ChatColor.GREEN + "NoInvis started successfully! " + ChatColor.DARK_PURPLE
				+ "Made by Pray on 11/5/2021, Version: 1.0");
		getConfig().options().copyDefaults();
		saveDefaultConfig();

		getConfig().createSection("no-invis-in-pvp");
		getConfig().set("no-invis-in-pvp.enabled", true);
		getConfig().set("no-invis-in-pvp.message", "&e(!) &cInvisibility is disabled in pvp!");

		getConfig().createSection("no-harm-in-pvp");
		getConfig().set("no-harm-in-pvp.enabled", true);
		getConfig().set("no-harm-in-pvp.message", "&e(!) &cInstant Harming is disabled in pvp!");

		saveConfig();

		initCmds();
		initEvents();

		SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
		sessionManager.registerHandler(CustomHandler.FACTORY, null);
	}

	public void initCmds() {
		getCommand("noinvisreload").setExecutor(new Reload(this));
	}

	public void initEvents() {
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
	}

}
