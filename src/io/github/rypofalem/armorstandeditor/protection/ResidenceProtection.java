package io.github.rypofalem.armorstandeditor.protection;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;

public class ResidenceProtection implements ASEProtection {
	Residence residencePlugin;
	
	public ResidenceProtection(Residence res){
		residencePlugin = res;
	}
	
	@Override
	public boolean canEdit(Player player, ArmorStand armorstand) {
		if(residencePlugin == null || !residencePlugin.isEnabled()) return true;
		if(Residence.getResidenceManager() == null) return true;
		Location loc = armorstand.getLocation();
		ClaimedResidence res = Residence.getResidenceManager().getByLoc(loc);
		FlagPermissions perms = Residence.getPermsByLoc(loc);
		if(perms == null) return true;
		if(res == null) return true;
		return res.getPermissions().getOwnerUUID().equals(player.getUniqueId()) ||
				(perms.playerHas(player.getName(), loc.getWorld().getName(), "destroy", false) && perms.playerHas(player.getName(), loc.getWorld().getName(), "destroy", false));
	}
}