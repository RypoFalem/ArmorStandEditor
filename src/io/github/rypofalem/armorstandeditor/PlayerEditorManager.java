package io.github.rypofalem.armorstandeditor;

import io.github.rypofalem.armorstandeditor.menu.ASEHolder;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

//Manages PlayerEditors and Player Events related to editing armorstands
public class PlayerEditorManager implements Listener{
	ArmorStandEditorPlugin plugin;
	HashMap<UUID, PlayerEditor> players;
	private ASEHolder pluginHolder= new ASEHolder();

	public PlayerEditorManager(ArmorStandEditorPlugin plugin){
		this.plugin = plugin;
		players = new HashMap<UUID, PlayerEditor>();
	}

	//Stop players from damaging armorstands with tool in their hands and then tries to edit it.
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled=false)
	void onArmorStandLeftClick(EntityDamageByEntityEvent e){
		try{
			if(e.getEntityType() == EntityType.ARMOR_STAND){
				if(e.getDamager() instanceof Player){
					Player player = (Player) e.getDamager();
					if(player.getItemInHand().getType() == plugin.editTool){
						e.setCancelled(true);
						getPlayerEditor(player.getUniqueId()).cancelOpenMenu();
						getPlayerEditor(player.getUniqueId()).editArmorStand((ArmorStand)e.getEntity());
					}
				}
			}
		}
		catch(Exception exception){
			plugin.logError(exception);
		}catch(Error error){
			plugin.logError(error);
		}
	}

	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled=false)
	void onArmorStandRightClick(PlayerArmorStandManipulateEvent e){
		try {
			Player player = (Player) e.getPlayer();
			if(player.getItemInHand().getType() == plugin.editTool){
				e.setCancelled(true);
				getPlayerEditor(player.getUniqueId()).cancelOpenMenu();
				getPlayerEditor(player.getUniqueId()).reverseEditArmorStand(e.getRightClicked());
			}
		} catch(Exception exception){
			plugin.logError(exception);
		}catch(Error error){
			plugin.logError(error);
		}
	}

	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled=false)
	void onRightClickTool(PlayerInteractEvent e){
		try {
			Player player = (Player) e.getPlayer();
			if(player.getItemInHand() != null && player.getItemInHand().getType() == plugin.editTool){
				e.setCancelled(true);
				getPlayerEditor(player.getUniqueId()).openMenu();
			}
		}catch(Exception exception){
			plugin.logError(exception);
		}catch(Error error){
			plugin.logError(error);
		}
	}

	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled=false)
	void onScrollNCrouch(PlayerItemHeldEvent e){
		try {
			Player player = e.getPlayer();
			if(player.isSneaking()){
				if(player.getInventory().getItem(e.getPreviousSlot()) != null && player.getInventory().getItem(e.getPreviousSlot()).getType() == plugin.editTool){
					if(e.getNewSlot() == e.getPreviousSlot() +1 || (e.getNewSlot() == 0 && e.getPreviousSlot() == 8)){
						getPlayerEditor(player.getUniqueId()).cycleAxis(1);
					}else{
						if(e.getNewSlot() == e.getPreviousSlot() - 1 || (e.getNewSlot() == 8 && e.getPreviousSlot() == 0)){
							getPlayerEditor(player.getUniqueId()).cycleAxis(-1);
						}
					}
					e.setCancelled(true);
				}
			}
		}catch(Exception exception){
			plugin.logError(exception);
		}catch(Error error){
			plugin.logError(error);
		}
	}

	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled=false)
	void onPlayerMenuSelect(InventoryClickEvent e){
		try {
			if(e.getInventory()!= null && e.getInventory().getHolder() != null && e.getInventory().getHolder() instanceof ASEHolder ) {
				e.setCancelled(true);
				ItemStack item = e.getCurrentItem();
				if(item!= null && item.hasItemMeta() && item.getItemMeta().hasLore()
						&& !item.getItemMeta().getLore().isEmpty() 
						&& item.getItemMeta().getLore().get(0).startsWith(Util.encodeHiddenLore("ase"))){
					Player player = (Player) e.getWhoClicked();
					String command = Util.decodeHiddenLore(item.getItemMeta().getLore().get(0));
					player.performCommand(command);
				}
			}
		} catch(Exception exception){
			plugin.logError(exception);
		}catch(Error error){
			plugin.logError(error);
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

	//returns the inventoryholder that owns all the menu inventories
	public ASEHolder getPluginHolder() {
		return pluginHolder;
	}
}
