package io.github.rypofalem.armorstandeditor;

import io.github.rypofalem.armorstandmanipulation.editor.Axis;
import io.github.rypofalem.armorstandmanipulation.editor.EditMode;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEx implements CommandExecutor{
	ArmorStandEditorPlugin plugin;
	final String LISTMODE = ChatColor.GREEN + "/ase mode <NONE | INVISIBLE | SHOWARMS | GRAVITY | BASEPLATE | SIZE | HEAD | BODY | LEFTARM | RIGHTARM | LEFTLEG | RIGHTLEG>".toLowerCase();
	final String LISTAXIS = ChatColor.GREEN + "/ase axis <X|Y|Z>";

	public CommandEx(ArmorStandEditorPlugin armorStandEditorPlugin) {
		this.plugin = armorStandEditorPlugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player && checkPermission((Player)sender,"basic",true)){
			
			if(args.length == 0){
				sender.sendMessage(LISTMODE);
				sender.sendMessage(LISTAXIS);
				return true;
			}
			
			if(args.length > 0){
				switch(args[0].toLowerCase()){
				case "mode": 
					commandMode(((Player)sender), args);
					break;
				case "axis": 
					commandAxis(((Player)sender), args);
					break;
				default:
					sender.sendMessage(LISTMODE);
					sender.sendMessage(LISTAXIS);
				}
				return true;
			}
		}
		return false;
	}

	private void commandAxis(Player player, String[] args) {
		if(args.length <=1){
			player.sendMessage(ChatColor.GREEN + "You must specify an axis!");
			player.sendMessage(LISTAXIS);
		}
		
		if(args.length > 1){
			for(Axis axis : Axis.values()){
				if(axis.toString().toLowerCase().contentEquals(args[1].toLowerCase())){
					plugin.editor.getPlayerEditor(player.getUniqueId()).setAxis(axis);
					player.sendMessage(ChatColor.GREEN + "Setting axis to " + axis.toString());
					return;
				}
			}
		}
	}

	private void commandMode(Player player, String[] args) {
		if(args.length <=1){
			player.sendMessage(ChatColor.GREEN + "You must specify a mode!");
			player.sendMessage(LISTMODE);
		}
		
		if(args.length > 1){
			for(EditMode mode : EditMode.values()){
				if(mode.toString().toLowerCase().contentEquals(args[1].toLowerCase()) && checkPermission(player,args[1],true)){
					plugin.editor.getPlayerEditor(player.getUniqueId()).setMode(mode);
					player.sendMessage(ChatColor.GREEN + "Setting mode to " + mode.toString());
					return;
				}
			}
		}
	}
	
	private boolean checkPermission(Player player, String permName, boolean sendMessageOnInvalidation){
		if(player.hasPermission("asedit." + permName.toLowerCase())){
			return true;
		}else{
			if(sendMessageOnInvalidation){
				player.sendMessage(ChatColor.RED + "You do not have permission to use this!");
			}
			return false;
		}
	}

}
