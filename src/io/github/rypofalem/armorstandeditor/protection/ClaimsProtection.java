package io.github.rypofalem.armorstandeditor.protection;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import com.winthier.claims.Claims;
import com.winthier.claims.bukkit.BukkitClaimsPlugin;

public class ClaimsProtection implements Protection {
	Claims claims;
	BukkitClaimsPlugin plugin;
	
	public ClaimsProtection(BukkitClaimsPlugin bukkitClaimsPlugin){
		plugin = bukkitClaimsPlugin;
		claims = Claims.getInstance();
	}
	
	@Override
	public boolean canEdit(Player player, ArmorStand armorstand) {
		if(plugin == null || !plugin.isEnabled()) return true;
		if(claims == null){
			claims = Claims.getInstance();
			if(claims == null) return true;
		}
		return claims.canBuild(player.getUniqueId(), player.getWorld().getName(), armorstand.getLocation().getBlockX(), armorstand.getLocation().getBlockY(), armorstand.getLocation().getBlockZ());
	}
}
