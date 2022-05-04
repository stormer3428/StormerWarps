package fr.stormer3428.warps;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class WarpTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		List<String> list = new ArrayList<>();

		String s = "";
		int depth = 0;

		if(sender instanceof Player) {

			Player p = (Player) sender;

			if(cmd.getName().equalsIgnoreCase("warp") || cmd.getName().equalsIgnoreCase("delwarp") && p.isOp()) {
				for(Warp warp : Warp.getWarps(p.isOp())) {
					s = warp.getName(); if(args.length == depth || (args.length == depth + 1 && s.toLowerCase().startsWith(args[depth].toLowerCase()))) list.add(s);
				}
			}
		}
		return list;
	}
}
