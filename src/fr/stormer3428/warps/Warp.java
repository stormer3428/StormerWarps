package fr.stormer3428.warps;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.stormer3428.obsidianMC.Util.OMCLogger;

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
				saveToConfig(warp);
				return;
			}
		}
		this.location = loc;
		this.name = n;
		this.opOnly = opOnly;
		saveToConfig(this);
		all.add(this);
	}

	private static void saveToConfig(Warp warp) {
		OMCLogger.debug("saving to config : " + warp);
		String path = "warps." + warp.name + ".";
		FileConfiguration config = Warps.i.getConfig();
		OMCLogger.debug("config : " + config);
		config.set(path + "x", warp.location.getX());
		config.set(path + "y", warp.location.getY());
		config.set(path + "z", warp.location.getZ());
		config.set(path + "yaw", warp.location.getYaw());
		config.set(path + "pitch", warp.location.getPitch());
		config.set(path + "world", warp.location.getWorld().getName());
		config.set(path + "opOnly", warp.opOnly);
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

	public static Warp fromName(String name) {
		for(Warp warp : all) if(warp.getName().equals(name)) return warp;
		for(Warp warp : all) if(warp.getName().equalsIgnoreCase(name)) return warp;
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
		OMCLogger.normal(p, Lang.WARP.toString().replace("<%WARP>", getName()));
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

	public static void loadFromConfig() {
		OMCLogger.debug("loading warps from config");
		Warp.all.clear();
		FileConfiguration config = Warps.i.getConfig();
		
		OMCLogger.debug("config : " + config);
		
		ConfigurationSection warpsSection = config.getConfigurationSection("warps");
		if(warpsSection == null) {
			OMCLogger.debug("no warps section found");
			return;
		}
		OMCLogger.debug("looping through keys");
		for(String warp : warpsSection.getKeys(false)) {
			String path = "warps." + warp + ".";
			try {

				OMCLogger.debug("Attempting to load warp ("+path+")");

				String sx = config.getString(path + "x");
				String sy = config.getString(path + "y");
				String sz = config.getString(path + "z");
				String syaw = config.getString(path + "yaw");
				String spitch = config.getString(path + "pitch");
				String sworld = config.getString(path + "world");
				String sOpOnly = config.getString(path + "opOnly");

				double x;
				try {
					x = Double.parseDouble(sx);
				} catch (Exception e) {
					OMCLogger.systemError("invalid configuration for warp : (" + path + ")");
					OMCLogger.systemError("invalid x");
					continue;
				}

				double y;
				try {
					y = Double.parseDouble(sy); 
				} catch (Exception e) {
					OMCLogger.systemError("invalid configuration for warp : (" + path + ")");
					OMCLogger.systemError("invalid y");
					continue;
				}

				double z;
				try {
					z = Double.parseDouble(sz);
				} catch (Exception e) {
					OMCLogger.systemError("invalid configuration for warp : (" + path + ")");
					OMCLogger.systemError("invalid z");
					continue;
				}

				float yaw;
				try {
					yaw = Float.parseFloat(syaw);
				} catch (Exception e) {
					OMCLogger.systemError("invalid configuration for warp : (" + path + ")");
					OMCLogger.systemError("invalid yaw");
					continue;
				}

				float pitch;
				try {
					pitch = Float.parseFloat(spitch);
				} catch (Exception e) {
					OMCLogger.systemError("invalid configuration for warp : (" + path + ")");
					OMCLogger.systemError("invalid pitch");
					continue;
				}

				boolean opOnly;
				try {
					opOnly = Boolean.parseBoolean(sOpOnly);
				} catch (Exception e) {
					OMCLogger.systemError("invalid configuration for warp : (" + path + ")");
					OMCLogger.systemError("invalid opOnly");
					continue;
				}

				World world;
				try {
					world = Bukkit.getWorld(sworld);

					if(world == null) OMCLogger.systemError("invalid world name for warp : (" + path + ")");
					else {
						Warp h = new Warp(new Location(world, x, y, z, yaw, pitch), warp, opOnly);
						OMCLogger.debug("Created warp");
						OMCLogger.debug(h.toString());
					}
				} catch (Exception e) {
					OMCLogger.systemError("invalid configuration for warp : (" + path + ")");
					OMCLogger.systemError("invalid world");
					continue;
				}
			}catch (Exception e) {
				OMCLogger.systemError("failed to read warp : (" + path + ")");
			}
		}
	}



}
