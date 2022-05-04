package fr.stormer3428.warps;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import utils.Message;

public class Warp {

	private Location location;
	private String name;
	private boolean opOnly;

	static List<Warp> all = new ArrayList<>();

	public Warp(@Nonnull Location loc, String n) {
		this(loc, n, false);
	}
	
	public Warp(@Nonnull Location loc, String n, boolean opOnly) {
		for(Warp warp : all) {
			if(warp.name == n) {
				warp.setLocation(loc);
				warp.setName(n);
				warp.setOpOnly(opOnly);
				createWarp(warp);
				return;
			}
		}
		this.location = loc;
		this.name = n;
		this.opOnly = opOnly;
		createWarp(this);
		all.add(this);
	}
	
	private static void createWarp(Warp warp) {
		String path = "warps." + warp.name + ".";
		Warps.i.getConfig().set(path + "x", warp.location.getX());
		Warps.i.getConfig().set(path + "y", warp.location.getY());
		Warps.i.getConfig().set(path + "z", warp.location.getZ());
		Warps.i.getConfig().set(path + "yaw", warp.location.getYaw());
		Warps.i.getConfig().set(path + "pitch", warp.location.getPitch());
		Warps.i.getConfig().set(path + "world", warp.location.getWorld().getName());
		Warps.i.getConfig().set(path + "opOnly", warp.opOnly);
		Warps.i.loadConfig();
	}
	
	public static void deleteWarp(Warp warp) {
		String path = "warps." + warp.name;
		Warps.i.getConfig().set(path, null);
		Warps.i.loadConfig();
		all.remove(warp);
	}
	
	public void delete() {
		deleteWarp(this);
	}
	
	public static Warp findWarp(String name) {
		for(Warp warp : all) {
			if(warp.getName().equals(name)) return warp;
		}
		return null;
	}
	
	public static List<Warp> getWarps(boolean isOP) {
		if(isOP) return all;
		List<Warp> warps = new ArrayList<>();
		for(Warp warp : all) {
			if(!warp.opOnly) warps.add(warp);
		}
		return warps;
	}
	
	public void warp(Player p) {
		Message.normal(p, "Going to " + getName());
		p.teleport(getLocation());
	}
	
	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	@Override
	public String toString() {
		return "[Warp {"+this.location.toString()+","+this.name+"}]";
	}

	public boolean isOpOnly() {
		return this.opOnly;
	}

	public void setOpOnly(boolean opOnly) {
		this.opOnly = opOnly;
	}



}
