package io.github.rypofalem.armorstandeditor.protection;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WGProtection implements ASEProtection {
	private WorldGuardPlugin wgPlugin;
	
	public WGProtection(WorldGuardPlugin wgplugin){
		this.wgPlugin = wgplugin;
	}

	@Override
	public boolean canEdit(Player player, ArmorStand armorstand) {
		if(!wgPlugin.canBuild(player, armorstand.getLocation())){
			return false;
		}
		return true;
	}

}
