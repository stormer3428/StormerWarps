package fr.stormer3428.warps;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import utils.Message;

public class Warps extends JavaPlugin{

	public static Warps i;

	@Override
	public void onEnable() {
		i = this;
		getCommand("warp").setExecutor(new WarpCommand());
		getCommand("warp").setTabCompleter(new WarpTabCompleter());
		getCommand("setwarp").setExecutor(new WarpCommand());
		getCommand("setwarp").setTabCompleter(new WarpTabCompleter());
		getCommand("delwarp").setExecutor(new WarpCommand());
		getCommand("delwarp").setTabCompleter(new WarpTabCompleter());
		getCommand("warps").setExecutor(new WarpCommand());
		getCommand("swreload").setExecutor(new WarpCommand());

		reload();

		super.onEnable();		
	}

	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	@SuppressWarnings("unused")
	public void reload() {
		Warp.all.clear();
		Set<String> keys = getConfig().getKeys(true);
		Set<String> warps = new HashSet<>();

		for(String s : keys) {
			String[] args = s.split("\\.");
			if(args.length > 1 && args[0].equals("warps")) {
				if(!warps.contains(args[1])) warps.add(args[1]);
			}
		}

		for(String warp : warps) {
			String path = "warps." + warp + ".";

			Message.systemNormal("Attempting to load warp ("+path+")");

			String sx = getConfig().getString(path + "x");
			String sy = getConfig().getString(path + "y");
			String sz = getConfig().getString(path + "z");
			String syaw = getConfig().getString(path + "yaw");
			String spitch = getConfig().getString(path + "pitch");
			String sworld = getConfig().getString(path + "world");
			String sOpOnly = getConfig().getString(path + "opOnly");
			
			double x;
			try {
				x = Double.parseDouble(sx);
			} catch (Exception e) {
				Message.systemError("invalid configuration for warp : (" + path + ")");
				Message.systemError("invalid x");
				continue;
			}
			
			double y;
			try {
				y = Double.parseDouble(sy); 
			} catch (Exception e) {
				Message.systemError("invalid configuration for warp : (" + path + ")");
				Message.systemError("invalid y");
				continue;
			}
			
			double z;
			try {
				z = Double.parseDouble(sz);
			} catch (Exception e) {
				Message.systemError("invalid configuration for warp : (" + path + ")");
				Message.systemError("invalid z");
				continue;
			}
			
			float yaw;
			try {
				yaw = Float.parseFloat(syaw);
			} catch (Exception e) {
				Message.systemError("invalid configuration for warp : (" + path + ")");
				Message.systemError("invalid yaw");
				continue;
			}
			
			float pitch;
			try {
				pitch = Float.parseFloat(spitch);
			} catch (Exception e) {
				Message.systemError("invalid configuration for warp : (" + path + ")");
				Message.systemError("invalid pitch");
				continue;
			}
			
			boolean opOnly;
			try {
				opOnly = Boolean.parseBoolean(sOpOnly);
			} catch (Exception e) {
				Message.systemError("invalid configuration for warp : (" + path + ")");
				Message.systemError("invalid opOnly");
				continue;
			}
			
			World world;
			try {
				world = Bukkit.getWorld(sworld);

				if(world == null) Message.systemError("invalid world name for warp : (" + path + ")");
				else {
					Warp h = new Warp(new Location(world, x, y, z, yaw, pitch), warp, opOnly);
					Message.systemNormal("Created warp");
					Message.systemNormal(h.toString());
				}
			} catch (Exception e) {
				Message.systemError("invalid configuration for warp : (" + path + ")");
				Message.systemError("invalid world");
				continue;
			}
		}
	}
}
