package fr.stormer3428.warps;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.stormer3428.obsidianMC.OMCCommand;
import fr.stormer3428.obsidianMC.OMCCommandManager;
import fr.stormer3428.obsidianMC.OMCLang;
import fr.stormer3428.obsidianMC.OMCLogger;
import fr.stormer3428.obsidianMC.OMCVariable;

public class WarpsCommandManager extends OMCCommandManager{

	@Override
	protected void registerVariables() {
		OMCCommand.registerVariable(new OMCVariable("%WP%") {
			
			@Override
			protected ArrayList<String> complete(CommandSender sender, String incomplete) {
				ArrayList<String> list = new ArrayList<>();
				final String lower = incomplete.trim().toLowerCase();
				OMCLogger.debug("Variable completing '" + incomplete +"' with variable " + signature);
				for(Warp warp : Warp.getWarps(sender.isOp())) {
					OMCLogger.debug("Variable completing check '" + incomplete +"' '" + warp.getName().toLowerCase() + "'");
					if(lower.isEmpty() || warp.getName().toLowerCase().startsWith(lower)) {
						OMCLogger.debug("Success");
						list.add(warp.getName());
					}else {
						OMCLogger.debug("Failed");
					}
				}
				return list;
			}
		});
	}

	@Override
	protected void registerCommands() {
		COMMANDS.add(new OMCCommand("warps") {
			@Override
			public boolean execute(CommandSender sender, ArrayList<String> vars) {
				OMCLogger.normal(sender, Lang.WARPS_HEADER.toString());
				for(Warp warp : Warp.getWarps(sender.isOp())) OMCLogger.normal(sender, (warp.isOpOnly() ? Lang.WARPS_LIST_WARP_OP.toString() : Lang.WARPS_LIST_WARP.toString()).replace("<%WARP>", warp.getName()));
				return OMCLogger.normal(sender, Lang.WARPS_FOOTER.toString());
			}
		});

		COMMANDS.add(new OMCCommand("warp") {
			@Override
			public boolean execute(CommandSender sender, ArrayList<String> vars) {
				return OMCLogger.error(sender, Lang.ERROR_NOARG_WARP.toString());
			}
		});

		COMMANDS.add(new OMCCommand("warp %WP%") {
			@Override
			public boolean execute(CommandSender sender, ArrayList<String> vars) {
				if(!(sender instanceof Player p)) return OMCLogger.error(sender, OMCLang.ERROR_PLAYERONLY.toString());
				Warp warp = Warp.fromName(vars.get(0));
				if(warp == null) return OMCLogger.error(sender, Lang.ERROR_NO_WARP.toString().replace("<%WARP>", vars.get(0)));
				if(warp.isOpOnly() && !p.isOp()) return OMCLogger.error(sender, Lang.ERROR_OPONLY.toString());
				warp.warp(p);
				return true;
			}
		});
		
		COMMANDS.add(new OMCCommand("setwarp") {
			@Override
			public boolean execute(CommandSender sender, ArrayList<String> vars) {
				return OMCLogger.error(sender, Lang.ERROR_NOARG_WARP.toString());
			}
		});

		COMMANDS.add(new OMCCommand("setwarp %V%") {
			@Override
			public boolean execute(CommandSender sender, ArrayList<String> vars) {
				if(!(sender instanceof Player p)) return OMCLogger.error(sender, OMCLang.ERROR_PLAYERONLY.toString());
				if(Warp.fromName(vars.get(0)) != null) return OMCLogger.error(sender, Lang.ERROR_ALREADY_WARP.toString());
				final Warp newWarp = new Warp(p.getLocation(), vars.get(0), false);
				return OMCLogger.normal(p, Lang.WARP_SET.toString().replace("<%WARP>", newWarp.getName()));
			}
		});

		COMMANDS.add(new OMCCommand("setwarp %V% %B%") {
			@Override
			public boolean execute(CommandSender sender, ArrayList<String> vars) {
				if(!(sender instanceof Player p)) return OMCLogger.error(sender, OMCLang.ERROR_PLAYERONLY.toString());
				if(Warp.fromName(vars.get(0)) != null) return OMCLogger.error(sender, Lang.ERROR_ALREADY_WARP.toString());
				boolean opOnly = Boolean.parseBoolean(vars.get(1));
				final Warp newWarp = new Warp(p.getLocation(), vars.get(0), opOnly);
				return OMCLogger.normal(p, Lang.WARP_SET.toString().replace("<%WARP>", newWarp.getName()));
			}
		});
		
		COMMANDS.add(new OMCCommand("delwarp") {
			@Override
			public boolean execute(CommandSender sender, ArrayList<String> vars) {
				return OMCLogger.error(sender, Lang.ERROR_NOARG_WARP.toString());
			}
		});
		
		COMMANDS.add(new OMCCommand("delwarp %WP%") {
			@Override
			public boolean execute(CommandSender sender, ArrayList<String> vars) {
				if(!(sender instanceof Player p)) return OMCLogger.error(sender, OMCLang.ERROR_PLAYERONLY.toString());
				Warp warp = Warp.fromName(vars.get(0));
				if(warp == null) return OMCLogger.error(sender, Lang.ERROR_NO_WARP.toString().replace("<%WARP>", vars.get(0)));
				if(warp.isOpOnly() && !p.isOp()) return OMCLogger.error(sender, Lang.ERROR_OPONLY.toString());
				warp.delete();
				return OMCLogger.normal(p, Lang.WARP_REMOVED.toString().replace("<%WARP>", warp.getName()));
			}
		});

		COMMANDS.add(new OMCCommand("stormerwarps reload") {
			@Override
			public boolean execute(CommandSender sender, ArrayList<String> vars) {
				Warps.i.reload();
				return OMCLogger.normal(sender, Lang.WARPS_RELOAD.toString());
			}
		});
	}

}
