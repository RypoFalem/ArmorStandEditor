package io.github.rypofalem.armorstandeditor.protection;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class GPProtection implements Protection{
	private GriefPrevention gpPlugin;
	
	public GPProtection(GriefPrevention gpPlugin) {
		this.gpPlugin = gpPlugin;
	}

	@Override
	public boolean canEdit(Player player, ArmorStand armorstand) {
		if(gpPlugin != null){
			Claim claim = GriefPrevention.instance.dataStore.getClaimAt(armorstand.getLocation(), false, null);
			if(claim != null 
					&& claim.allowBuild(player, Material.DIAMOND_BLOCK ) != null){ //claim.allowBuild returns null if player has permission to build
				return false;
			}
		}
		return true;
	}

}