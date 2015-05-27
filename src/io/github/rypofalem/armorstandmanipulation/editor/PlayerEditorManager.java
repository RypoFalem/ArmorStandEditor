package io.github.rypofalem.armorstandmanipulation.editor;

import java.util.HashMap;
import java.util.UUID;

import io.github.rypofalem.armorstandeditor.ArmorStandEditorPlugin;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

//Manages PlayerEditors and Player Events related to editing armorstands
public class PlayerEditorManager implements Listener{
	ArmorStandEditorPlugin plugin;
	HashMap<UUID, PlayerEditor> players;

	public PlayerEditorManager(ArmorStandEditorPlugin plugin){
		this.plugin = plugin;
		players = new HashMap<UUID, PlayerEditor>();
	}

	//Opens the edit menu
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled=false)
	void onRightClickEditTool(PlayerInteractEntityEvent e){
		if(e.getPlayer().getItemInHand().getType() == plugin.editTool){
		}
	}

	//Stop players from damaging armorstands with tool in their hands and then tries to edit it.
	@EventHandler (priority = EventPriority.LOW, ignoreCancelled=false)
	void onArmorStandDamage(EntityDamageByEntityEvent e){
		if(e.getEntityType() == EntityType.ARMOR_STAND){
			if(e.getDamager() instanceof Player){
				Player player = (Player) e.getDamager();
				if(player.getItemInHand().getType() == plugin.editTool){
					e.setCancelled(true);
					//plugin.print(player.getName() + " tried to destroy an armorstand with his tool.");
					getPlayerEditor(player.getUniqueId()).editArmorStand((ArmorStand)e.getEntity());
				}
			}
		}
	}
	
	public PlayerEditor getPlayerEditor(UUID uuid){
		return players.containsKey(uuid) ? players.get(uuid) : addPlayerEditor(uuid);
	}
	
	PlayerEditor addPlayerEditor(UUID uuid){
		PlayerEditor pe = new PlayerEditor(uuid, plugin);
		players.put(uuid, pe);
		return pe;
	}
	
	void removePlayerEditor(UUID uuid){
		players.remove(uuid);
	}
	
	//Stop tracking player when he leaves
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled=false)
	void onPlayerLogOut(PlayerQuitEvent e){
		removePlayerEditor(e.getPlayer().getUniqueId());
	}
}
