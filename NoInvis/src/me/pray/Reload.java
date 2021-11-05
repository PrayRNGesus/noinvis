package me.pray;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor {

	private NoInvis noe;

	public Reload(NoInvis noe) {
		this.noe = noe;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender.isOp()) {
			noe.reloadConfig();
			sender.sendMessage("Reloaded NoInvis.");
		}

		return false;
	}

}
