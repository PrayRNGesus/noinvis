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
		createFiles();

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
	
	 private void createFiles() {
         configf = new File(getDataFolder(), "config.yml");

        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        config = new YamlConfiguration();

        try {
            config.load(configf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

}
