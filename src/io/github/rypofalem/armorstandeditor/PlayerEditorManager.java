package io.github.rypofalem.armorstandeditor;

import io.github.rypofalem.armorstandeditor.menu.ASEHolder;
import io.github.rypofalem.armorstandeditor.menu.EquipmentMenu;
import io.github.rypofalem.armorstandeditor.menu.Menu;

import java.util.HashMap;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

//Manages PlayerEditors and Player Events related to editing armorstands
public class PlayerEditorManager implements Listener{
	ArmorStandEditorPlugin plugin;
	HashMap<UUID, PlayerEditor> players;
	private ASEHolder pluginHolder= new ASEHolder(); //Inventory holder that owns all menu inventories for the plugin
	double coarseAdj;
	double fineAdj;
	double coarseMov;
	double fineMov;
	boolean ignoreNextInteract = false;

	public PlayerEditorManager(ArmorStandEditorPlugin plugin){
		this.plugin = plugin;
		players = new HashMap<UUID, PlayerEditor>();
		coarseAdj = Util.FULLCIRCLE / plugin.coarseRot;
		fineAdj = Util.FULLCIRCLE / plugin.fineRot;
		coarseMov = 1;
		fineMov = .03125; // 1/32
	}

	@EventHandler (priority = EventPriority.NORMAL, ignoreCancelled=false)
	void onArmorStandDamage(EntityDamageByEntityEvent event){
		if(!(event.getEntity() instanceof ArmorStand)) return;
		if(!(event.getDamager() instanceof Player))return;
		ArmorStand as = (ArmorStand)event.getEntity();
		Player player = (Player) event.getDamager();
		if(player.getInventory().getItemInMainHand().getType() != plugin.editTool) return;
		getPlayerEditor(player.getUniqueId()).cancelOpenMenu();
		event.setCancelled(true);
		ignoreNextInteract = true;
		PlayerInteractAtEntityEvent interactEvent = new PlayerInteractAtEntityEvent(player, as, as.getLocation().toVector());
		Bukkit.getServer().getPluginManager().callEvent(interactEvent);
		ignoreNextInteract = false;
		if(!interactEvent.isCancelled()) applyLeftTool(player, as);
	}

	@EventHandler (priority = EventPriority.NORMAL, ignoreCancelled=true)
	void onArmorStandInteract(PlayerInteractAtEntityEvent e){
		if(ignoreNextInteract) return;
		if(e.getHand() != EquipmentSlot.HAND) return;
		Player player =  e.getPlayer();
		if(!(e.getRightClicked() instanceof ArmorStand)) return;
		if(player.getInventory().getItemInMainHand() == null) return;
		if(player.getInventory().getItemInMainHand().getType() == plugin.editTool){
			e.setCancelled(true);
			applyRightTool(player, (ArmorStand)e.getRightClicked());
			return;
		}

		//Nametag stuff
		if(player.getInventory().getItemInMainHand().getType() == Material.NAME_TAG){
			ItemStack nameTag = player.getInventory().getItemInMainHand(); 
			if(nameTag.hasItemMeta() && nameTag.getItemMeta().hasDisplayName()){
				ArmorStand as = (ArmorStand)e.getRightClicked();
				String name = nameTag.getItemMeta().getDisplayName();
				name = name.replace('&', ChatColor.COLOR_CHAR);
				if((as.getCustomName() != null && !as.getCustomName().equals(name)) // armorstand has name and that name is not the same as the nametag
						|| (as.getCustomName() == null && (!name.equals(""))) ){ // armorstand doesn't have name and nametag is not blank
					e.setCancelled(true); //nametag NOT given to armorstand
					as.setCustomName(name);
					as.setCustomNameVisible(true);

					if((player.getGameMode() == GameMode.CREATIVE)) return;
					if(nameTag.getAmount() > 1){
						nameTag.setAmount(nameTag.getAmount() - 1);
					}else{
						nameTag = new ItemStack(Material.AIR);
					}
					player.getInventory().setItemInMainHand(nameTag);
				}
			}
		} //end Nametag stuff
	}

	void applyLeftTool(Player player, ArmorStand as){
		getPlayerEditor(player.getUniqueId()).cancelOpenMenu();
		getPlayerEditor(player.getUniqueId()).editArmorStand(as);
	}

	void applyRightTool(Player player, ArmorStand as){
		getPlayerEditor(player.getUniqueId()).cancelOpenMenu();
		getPlayerEditor(player.getUniqueId()).reverseEditArmorStand(as);
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled=false)
	void onRightClickTool(PlayerInteractEvent e){
		if( !(e.getAction() == Action.LEFT_CLICK_AIR 
				|| e.getAction() == Action.RIGHT_CLICK_AIR
				|| e.getAction() == Action.LEFT_CLICK_BLOCK
				|| e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		Player player = e.getPlayer();
		if(player.getInventory().getItemInMainHand() == null)  return;
		if(player.getInventory().getItemInMainHand().getType() != plugin.editTool) return;
		e.setCancelled(true);
		getPlayerEditor(player.getUniqueId()).openMenu();
	}

	@EventHandler (priority = EventPriority.NORMAL, ignoreCancelled=true)
	void onScrollNCrouch(PlayerItemHeldEvent e){
		Player player = e.getPlayer();
		if(!player.isSneaking()) return;
		if(!(player.getInventory().getItem(e.getPreviousSlot()) != null 
				&& player.getInventory().getItem(e.getPreviousSlot()).getType() == plugin.editTool)) return;

		e.setCancelled(true);
		if(e.getNewSlot() == e.getPreviousSlot() +1 || (e.getNewSlot() == 0 && e.getPreviousSlot() == 8)){
			getPlayerEditor(player.getUniqueId()).cycleAxis(1);
		}else if(e.getNewSlot() == e.getPreviousSlot() - 1 || (e.getNewSlot() == 8 && e.getPreviousSlot() == 0)){
			getPlayerEditor(player.getUniqueId()).cycleAxis(-1);
		}
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled=false)
	void onPlayerMenuSelect(InventoryClickEvent e){
		if(e.getInventory() == null) return;
		if(e.getInventory().getHolder() == null) return;
		if(!(e.getInventory().getHolder() instanceof ASEHolder)) return;
		if(e.getInventory().getName().equals(Menu.getName())){
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			if(item!= null && item.hasItemMeta() && item.getItemMeta().hasLore()
					&& !item.getItemMeta().getLore().isEmpty() 
					&& item.getItemMeta().getLore().get(0).startsWith(Util.encodeHiddenLore("ase"))){
				Player player = (Player) e.getWhoClicked();
				String command = Util.decodeHiddenLore(item.getItemMeta().getLore().get(0));
				player.performCommand(command);
				return;
			}
		}
		if(e.getInventory().getName().equals(EquipmentMenu.getName())){
			ItemStack item = e.getCurrentItem();
			if(item == null) return;
			if(item.getItemMeta() == null ) return;
			if(item.getItemMeta().getLore() == null) return;
			if(item.getItemMeta().getLore().contains(Util.encodeHiddenLore("ase icon"))){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled=true)
	void onPlayerMenuClose(InventoryCloseEvent e){
		if(e.getInventory() == null) return;
		if(e.getInventory().getHolder() == null) return;
		if(!(e.getInventory().getHolder() instanceof ASEHolder)) return;
		if(e.getInventory().getName().equals(EquipmentMenu.getName())){
			PlayerEditor pe = players.get(e.getPlayer().getUniqueId());
			pe.equipMenu.equipArmorstand();
		}
	}

	//Stop tracking player when he leaves
	@EventHandler (priority = EventPriority.MONITOR)
	void onPlayerLogOut(PlayerQuitEvent e){
		removePlayerEditor(e.getPlayer().getUniqueId());
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

	public ASEHolder getPluginHolder() {
		return pluginHolder;
	}
}