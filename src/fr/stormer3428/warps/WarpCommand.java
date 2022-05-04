package fr.stormer3428.warps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import utils.Message;

public class WarpCommand implements CommandExecutor {

	@SuppressWarnings("unused")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("warps")) {
				listWarps(p);
				return true;
			}else if(cmd.getName().equalsIgnoreCase("warp")) {
				if(args.length == 0) {
					Message.error(p, "You need to specify the name of the warp you wish to teleport to");
					return false;
				}
				Warp warp = Warp.findWarp(args[0]);
				if(warp != null) {
					if(warp.isOpOnly() && !p.isOp()) {
						Message.error(p, "This warp is only accessible to operators");
						return false;
					}
					warp.warp(p);
					return true;
				}
				Message.error(p, "No warp with such name : " + args[0]);
				return false;
			}else if(cmd.getName().equalsIgnoreCase("setwarp")) {
				if(!p.isOp()) {
					Message.error(p, "Only an operator may run this command");
					return false;
				}
				if(args.length == 0) {
					Message.error(p, "You need to specify the name of the warp you wish to create");
					return true;
				}
				Warp warp = Warp.findWarp(args[0]);
				if(warp != null) {
					Message.error(p, "A Warp with this name already exists!");
					return false;
				}
				boolean opOnly = false;
				if(args.length != 1) {
					try {
						opOnly = Boolean.parseBoolean(args[1]);
					} catch (Exception e) {
						Message.error("invalid argument opOnly, expected a boolean but received : " + args[1]);
						return false;
					}
				}
				
				new Warp(p.getLocation(), args[0], opOnly);
				Message.normal(p, "Warp "+args[0]+" set");
				return true;
			}else if(cmd.getName().equalsIgnoreCase("delwarp")) {
				if(!p.isOp()) {
					Message.error(p, "Only an operator may run this command");
					return false;
				}
				if(args.length == 0) {
					Message.error(p, "You need to specify a name");
					Message.error(p, "Usage : delwarp <name>");
					return false;
				}
				Warp warp = Warp.findWarp(args[0]);
				if(warp == null) {
					Message.error(p, "No warp with such name : " + args[0]);
					return false;
				}
				warp.delete();
				Message.normal(p, "Warp " + warp.getName() + " removed!");
				return true;
			}else if(cmd.getName().equalsIgnoreCase("swreload")) {
				if(p.isOp()) {
					Warps.i.reload();
					Message.normal(p, "Successfully relaoded the plugin");
					return true;
				}
				Message.error(p, "You do not have the permission to use this command");
				return false;
			}
		}else if(sender instanceof BlockCommandSender){
			BlockCommandSender cmdBlock = (BlockCommandSender) sender;
			if(cmd.getName().equalsIgnoreCase("warp")) {
				if(args.length == 0) {
					Message.error(cmdBlock, "You need to specify the name of the warp you wish to teleport to");
					return false;
				}
				if(args.length == 1) {
					Message.error(cmdBlock, "You need to specify a player to teleport");
					return false;
				}
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:tp " + args);
				return false;
			}
		}
		return false;
	}

	private static void listWarps(Player p) {
		Message.normal(p, "<===========(Warps)===========>");
		for(Warp warp : Warp.getWarps(p.isOp())) {
			Message.normal(p, (warp.isOpOnly() ? ChatColor.GOLD : "" ) + " - " + warp.getName() + " - ");
		}
		Message.normal(p, "<===========(Warps)===========>");
	}



}
