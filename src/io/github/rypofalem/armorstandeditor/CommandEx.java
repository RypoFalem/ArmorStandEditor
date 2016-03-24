package io.github.rypofalem.armorstandeditor;

import io.github.rypofalem.armorstandeditor.modes.AdjustmentMode;
import io.github.rypofalem.armorstandeditor.modes.Axis;
import io.github.rypofalem.armorstandeditor.modes.EditMode;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEx implements CommandExecutor{
	ArmorStandEditorPlugin plugin;
	final String LISTMODE = ChatColor.GREEN + "/ase mode <" + Util.getEnumList(EditMode.class) + ">";
	final String LISTAXIS = ChatColor.GREEN + "/ase axis <" + Util.getEnumList(Axis.class) + ">";
	final String LISTADJUSTMENT = ChatColor.GREEN + "/ase adj <" + Util.getEnumList(AdjustmentMode.class) + ">";
	final String LISTSLOT =  ChatColor.GREEN + "/ase slot <1-9>";

	public CommandEx(ArmorStandEditorPlugin armorStandEditorPlugin) {
		this.plugin = armorStandEditorPlugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			if(sender instanceof Player && checkPermission((Player)sender,"basic",true)){
				if(args.length == 0){
					sender.sendMessage(LISTMODE);
					sender.sendMessage(LISTAXIS);
					sender.sendMessage(LISTSLOT);
					sender.sendMessage(LISTADJUSTMENT);
					return true;
				}

				if(args.length > 0){
					switch(args[0].toLowerCase()){
					case "mode": commandMode( ( (Player) sender), args);
					break;
					case "axis": commandAxis( ( (Player) sender), args);
					break;
					case "adj": commandAdj( ( (Player) sender), args);
					break;
					case "slot": commandSlot( ( (Player) sender), args);
					break;
					default:
						sender.sendMessage(LISTMODE);
						sender.sendMessage(LISTAXIS);
						sender.sendMessage(LISTSLOT);
						sender.sendMessage(LISTADJUSTMENT);
					}
					return true;
				}
			}
		}catch(Exception exception){
			plugin.logError(exception);
		}catch(Error error){
			plugin.logError(error);
		}
		return true;
	}

	private void commandSlot(Player player, String[] args) {

		if(args.length <=1){
			player.sendMessage(plugin.getLang().getMessage("noslotnumcom", "warn"));
			player.sendMessage(LISTSLOT);
		}

		if(args.length > 1){
			try{
				byte slot = (byte) (Byte.parseByte(args[1]) - 0b1);
				if(slot >= 0 && slot < 9){
					plugin.editorManager.getPlayerEditor(player.getUniqueId()).setCopySlot(slot);
				}else{
					player.sendMessage(LISTSLOT);
				}
				
			}catch(NumberFormatException nfe){
				player.sendMessage(LISTSLOT);
			}
		}
	}

	private void commandAdj(Player player, String[] args) {
		if(args.length <=1){
			player.sendMessage(plugin.getLang().getMessage("noadjcom", "warn"));
			player.sendMessage(LISTADJUSTMENT );
		}

		if(args.length > 1){
			for(AdjustmentMode adj : AdjustmentMode.values()){
				if(adj.toString().toLowerCase().contentEquals(args[1].toLowerCase())){
					plugin.editorManager.getPlayerEditor(player.getUniqueId()).setAdjMode(adj);
					return;
				}
			}
			player.sendMessage(LISTADJUSTMENT);
		}
	}

	private void commandAxis(Player player, String[] args) {
		if(args.length <=1){
			player.sendMessage(plugin.getLang().getMessage("noaxiscom", "warn"));
			player.sendMessage(LISTAXIS);
		}

		if(args.length > 1){
			for(Axis axis : Axis.values()){
				if(axis.toString().toLowerCase().contentEquals(args[1].toLowerCase())){
					plugin.editorManager.getPlayerEditor(player.getUniqueId()).setAxis(axis);
					return;
				}
			}
			player.sendMessage(LISTAXIS);
		}
	}

	private void commandMode(Player player, String[] args) {
		if(args.length <=1){
			player.sendMessage(plugin.getLang().getMessage("nomodecom", "warn"));
			player.sendMessage(LISTMODE);
		}

		if(args.length > 1){
			for(EditMode mode : EditMode.values()){
				if(mode.toString().toLowerCase().contentEquals(args[1].toLowerCase()) && checkPermission(player,args[1],true)){
					plugin.editorManager.getPlayerEditor(player.getUniqueId()).setMode(mode);
					return;
				}
			}
		}
	}

	private boolean checkPermission(Player player, String permName, boolean sendMessageOnInvalidation){
		if(permName.toLowerCase().equals("paste")){
			permName = "copy";
		}
		if(player.hasPermission("asedit." + permName.toLowerCase())){
			return true;
		}else{
			if(sendMessageOnInvalidation){
				player.sendMessage(plugin.getLang().getMessage("noperm", "warn"));
			}
			return false;
		}
	}
}