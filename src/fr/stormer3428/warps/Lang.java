package fr.stormer3428.warps;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.stormer3428.obsidianMC.Manager.OMCLogger;

public enum Lang {

	PREFIX_COMMAND("§l$f[$6WARPS$f]$r$7"),
	PREFIX_ERROR("§l$f[$cERROR$f]$r$c"),

	WARP("Going to <%WARP>"),
	WARP_SET("Warp <%WARP> set"),
	WARP_REMOVED("Warp <%WARP> removed"),
	WARPS_HEADER("<===========(Warps)===========>"),
	WARPS_FOOTER("<===========(Warps)===========>"),
	WARPS_LIST_WARP(" - <%WARP> - "),
	WARPS_LIST_WARP_OP(ChatColor.GOLD + " - <%WARP> - "),
	WARPS_RELOAD("Successfully reloaded the plugin"),
	
	
	ERROR_OPONLY("This warp is only accessible to operators"),
	ERROR_NOARG_WARP("You need to specify a warp name"),
	ERROR_NO_WARP("No warp with such name <%WARP>"),
	ERROR_ALREADY_WARP("A Warp with this name already exists!"), 
	;

	private String path;
	private String def;
	private static YamlConfiguration LANG;

	Lang(String d){
		this.path = this.name();
		this.def = d;
	}

	public static void setFile(YamlConfiguration config) {
		LANG = config;
	}

	@Override
	public String toString() {
		return 
				ChatColor.translateAlternateColorCodes('$', 
				ChatColor.translateAlternateColorCodes('&', 
				ChatColor.translateAlternateColorCodes('§', 
				LANG.getString(this.path, this.def)
				)));
	}

	public String getPath() {
		return this.path;
	}

	public String getDef() {
		return this.def;
	}

	public static void loadFromConfig() {
		File lang = new File(Warps.i.getDataFolder(), "lang.yml");
		YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(lang);
		if(!lang.exists()) try {
			langConfig.save(lang);
		}catch (IOException e) {
			e.printStackTrace();
			OMCLogger.systemError("Failed to create language file");
			OMCLogger.systemError("Disabling...");
			Warps.i.getServer().getPluginManager().disablePlugin(Warps.i);
		}
		for(Lang l : Lang.values()) if(langConfig.getString(l.getPath()) == null) langConfig.set(l.getPath(), l.getDef());
		Lang.setFile(langConfig);
		try {
			langConfig.save(lang);
		}catch (IOException e) {
			e.printStackTrace();
			OMCLogger.systemError("Failed to save language file");
			OMCLogger.systemError("Disabling...");
			Warps.i.getServer().getPluginManager().disablePlugin(Warps.i);
		}
	}
}
