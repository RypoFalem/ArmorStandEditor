package io.github.rypofalem.armorstandeditor;

import org.bukkit.ChatColor;

public abstract class Util {
	
	//inserts a formatting character between every character, making it invisible when displayed as lore
	public static String encodeHiddenLore(String lore){
		for(int i = 0; i < lore.length(); i+=2){
			lore = lore.substring(0, i) + ChatColor.COLOR_CHAR + lore.substring(i, lore.length());
		}
		return lore;
	}
	
	//removes the formatting characters
	public static String decodeHiddenLore(String lore){
		while(lore.contains(ChatColor.COLOR_CHAR + "")){
			lore =lore.substring(0, lore.indexOf(ChatColor.COLOR_CHAR)) + lore.substring(lore.indexOf(ChatColor.COLOR_CHAR)+1, lore.length());
		}
		return lore;
	}
}
