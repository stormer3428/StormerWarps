package fr.stormer3428.warps;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Manager.OMCLogger;

public class Warps extends OMCPlugin{

	WarpsCommandManager commandManager = new WarpsCommandManager();

	public static Warps i;
	
	public Warps() {
		Warps.i = this;
	}
	
	@Override
	public void loadLangFromConfig() {
		Lang.loadFromConfig();
	}

	@Override
	public void registerPluginTied() {
		registerPluginTied(commandManager);
	}

	@Override
	public void onObsidianEnable() {
		Warp.loadFromConfig();
	}

	@Override
	public void onObsidianDisable() {}

	@Override
	public OMCLogger instantiateLogger() {
		return new OMCLogger(Lang.PREFIX_COMMAND.toString(), Lang.PREFIX_ERROR.toString());
	}

	@Override
	public void reload() {
		Warp.loadFromConfig();
		super.reload();
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

}
