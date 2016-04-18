package io.github.rypofalem.armorstandeditor.protection;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;

public class TownyProtection implements ASEProtection {
	Towny towny;
	
	public TownyProtection(Towny towny){
		this.towny = towny;
	}

	@Override
	public boolean canEdit(Player player, ArmorStand armorstand) {
		return PlayerCacheUtil.getCachePermission(player, armorstand.getLocation(), Material.ARMOR_STAND.getId(),(byte) 0, TownyPermission.ActionType.BUILD);
	}
}
