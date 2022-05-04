package utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Message {

	private static final String PLUGIN_NAME = "Stormer's Warps";
	private static final ChatColor BRACKETS_COLOR = ChatColor.LIGHT_PURPLE;
	private static final ChatColor NAME_COLOR = ChatColor.RED;
	private static final ChatColor MESSAGE_COLOR = ChatColor.GREEN;
	private static final ChatColor MESSAGE_ERROR_COLOR = ChatColor.RED;
	
	public static void normal(CommandSender p,String strg){
		String m = (BRACKETS_COLOR + "[" + NAME_COLOR + PLUGIN_NAME + BRACKETS_COLOR + "] "  + MESSAGE_COLOR + strg);
		p.sendMessage(m);
	}
	
	public static void normal(String strg){
		for(Player p : Bukkit.getOnlinePlayers()){
			normal(p, strg);
		}
	}
	
	public static void normal(String strg, List<String> p){
		for(Player pls : Bukkit.getOnlinePlayers()){
			if(p.contains(pls.getName())) normal(pls, strg);
		}
	}

	public static void error(CommandSender p, String strg){
		String m = (BRACKETS_COLOR + "[" + NAME_COLOR + "Error" + BRACKETS_COLOR + "] "  + MESSAGE_ERROR_COLOR + strg);
		p.sendMessage(m);
	}
	
	public static void error(String strg){
		for(Player p : Bukkit.getOnlinePlayers()){
			error(p, strg);
		}
	}
	
	public static void error(String strg, List<String> p){
		for(Player pls : Bukkit.getOnlinePlayers()){
			if(p.contains(pls.getName())) error(pls, strg);
		}
	}

	public static void systemNormal(String strg){
		String m = (BRACKETS_COLOR + "[" + NAME_COLOR + PLUGIN_NAME + BRACKETS_COLOR + "] "  + MESSAGE_COLOR + strg);
		Bukkit.getConsoleSender().sendMessage(m);
	}
	
	public static void systemError(String strg){
		String m = (BRACKETS_COLOR + "[" + NAME_COLOR + PLUGIN_NAME + BRACKETS_COLOR + "] "  + MESSAGE_ERROR_COLOR + strg);
		Bukkit.getConsoleSender().sendMessage(m);
	}

}
