/*
 * ArmorStandEditor: Bukkit plugin to allow editing armor stand attributes
 * Copyright (C) 2016  RypoFalem
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package io.github.rypofalem.armorstandeditor;

import io.github.rypofalem.armorstandeditor.menu.ASEHolder;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

//Manages PlayerEditors and Player Events related to editing armorstands
public class PlayerEditorManager implements Listener{
	private ArmorStandEditorPlugin plugin;
	private HashMap<UUID, PlayerEditor> players;
	private ASEHolder menuHolder = new ASEHolder(); //Inventory holder that owns the main ase menu inventories for the plugin
	private ASEHolder equipmentHolder = new ASEHolder(); //Inventory holder that owns the equipment menu
	double coarseAdj;
	double fineAdj;
	double coarseMov;
	double fineMov;
	private boolean ignoreNextInteract = false;
	private TickCounter counter;


	PlayerEditorManager(ArmorStandEditorPlugin plugin){
		this.plugin = plugin;
		players = new HashMap<>();
		coarseAdj = Util.FULLCIRCLE / plugin.coarseRot;
		fineAdj = Util.FULLCIRCLE / plugin.fineRot;
		coarseMov = 1;
		fineMov = .03125; // 1/32
		counter = new TickCounter();
		Bukkit.getServer().getScheduler().runTaskTimer(plugin, counter, 0, 1);
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled=false)
	void onArmorStandDamage(EntityDamageByEntityEvent event){
		if(!(event.getDamager() instanceof Player))return;
		Player player = (Player) event.getDamager();
		if(!plugin.isEditTool(player.getInventory().getItemInMainHand())) return;
		if(!(event.getEntity() instanceof ArmorStand)){
			event.setCancelled(true);
			getPlayerEditor(player.getUniqueId()).openMenu();
			return;
		}
		ArmorStand as = (ArmorStand)event.getEntity();
		getPlayerEditor(player.getUniqueId()).cancelOpenMenu();
		event.setCancelled(true);
		if(canEdit(player, as)) applyLeftTool(player, as);
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled=false)
	void onArmorStandInteract(PlayerInteractAtEntityEvent event){
		if(ignoreNextInteract) return;
		if(event.getHand() != EquipmentSlot.HAND) return;
		Player player =  event.getPlayer();
		if(!(event.getRightClicked() instanceof ArmorStand)) return;
		final ArmorStand as = (ArmorStand)event.getRightClicked();

		if(!canEdit(player, as)) return;
		if(plugin.isEditTool(player.getInventory().getItemInMainHand())){
			getPlayerEditor(player.getUniqueId()).cancelOpenMenu();
			event.setCancelled(true);
			applyRightTool(player, as);
			return;
		}

		//Attempt rename
		if(player.getInventory().getItemInMainHand().getType() == Material.NAME_TAG && player.hasPermission("asedit.rename")){
			ItemStack nameTag = player.getInventory().getItemInMainHand();
			final String name;
			if(nameTag.getItemMeta() != null && nameTag.getItemMeta().hasDisplayName()){
				name = nameTag.getItemMeta().getDisplayName().replace('&', ChatColor.COLOR_CHAR);
			} else {
				name = null;
			}

			if(name == null){
				as.setCustomName(null);
				as.setCustomNameVisible(false);
				event.setCancelled(true);
			} else if(!name.equals("")){ // nametag is not blank
				event.setCancelled(true);

				if((player.getGameMode() != GameMode.CREATIVE)){
					if(nameTag.getAmount() > 1){
						nameTag.setAmount(nameTag.getAmount() - 1);
					}else{
						nameTag = new ItemStack(Material.AIR);
					}
					player.getInventory().setItemInMainHand(nameTag);
				}

				//minecraft will set the name after this event even if the event is cancelled.
				//change it 1 tick later to apply formatting without it being overwritten
				Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    as.setCustomName(name);
                    as.setCustomNameVisible(true);
                }, 1);
			}
		}
	}

	@EventHandler (priority = EventPriority.LOW, ignoreCancelled=true)
	public void onSwitchHands(PlayerSwapHandItemsEvent event){
		if(!plugin.isEditTool(event.getOffHandItem())) return; //event assumes they are already switched
		event.setCancelled(true);
		Player player = event.getPlayer();
		getPlayerEditor(event.getPlayer().getUniqueId()).setTarget(getTargets(player));
	}

	private ArrayList<ArmorStand> getTargets(Player player){
		Location eyeLaser = player.getEyeLocation();
		Vector direction = player.getLocation().getDirection();
		ArrayList<ArmorStand> armorStands = new ArrayList<>();

		final double STEPSIZE = .5;
		final Vector STEP = direction.multiply(STEPSIZE);
		final double RANGE = 10;
		final double LASERRADIUS = .3;
		List<Entity> nearbyEntities = player.getNearbyEntities(RANGE, RANGE, RANGE);
		if(nearbyEntities.isEmpty()) return null;

		for(double i = 0; i<RANGE; i+= STEPSIZE){
			List<Entity> nearby = (List<Entity>) player.getWorld().getNearbyEntities(eyeLaser, LASERRADIUS, LASERRADIUS, LASERRADIUS);
			if(!nearby.isEmpty()){
				boolean endLaser = false;
				for(Entity e : nearby){
					if(e instanceof ArmorStand){
						if(canEdit(player, (ArmorStand)e)){
							armorStands.add((ArmorStand)e);
							endLaser = true;
						}
					}
				}
				if(endLaser) break;
			}
			if(eyeLaser.getBlock().getType().isSolid()) break;
			eyeLaser.add(STEP);
		}
		return armorStands;
	}

	boolean canEdit(Player player, ArmorStand as){
		ignoreNextInteract = true;
		ArrayList<Event> events = new ArrayList<>();
		events.add(new PlayerInteractEntityEvent(player, as, EquipmentSlot.HAND));
		events.add(new PlayerInteractAtEntityEvent(player, as, as.getLocation().toVector(), EquipmentSlot.HAND));
		//events.add(new PlayerArmorStandManipulateEvent(player, as, player.getEquipment().getItemInMainHand(), as.getItemInHand(), EquipmentSlot.HAND));
		for(Event event : events){
			if(!(event instanceof Cancellable)) continue;
			try{
				plugin.getServer().getPluginManager().callEvent(event);
			} catch(IllegalStateException ise){
				ise.printStackTrace();
				ignoreNextInteract = false;
				return false; //Something went wrong, don't allow edit just in case
			}
			if(((Cancellable)event).isCancelled()){
				ignoreNextInteract = false;
				return false;
			}
		}
		ignoreNextInteract = false;
		return true;
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
		if(!plugin.isEditTool(player.getInventory().getItemInMainHand())) return;
		if(!player.hasPermission("asedit.basic")) return;
		e.setCancelled(true);
		getPlayerEditor(player.getUniqueId()).openMenu();
	}

	@EventHandler (priority = EventPriority.NORMAL, ignoreCancelled=true)
	void onScrollNCrouch(PlayerItemHeldEvent e){
		Player player = e.getPlayer();
		if(!player.isSneaking()) return;
		if(!plugin.isEditTool(player.getInventory().getItem(e.getPreviousSlot()))) return;

		e.setCancelled(true);
		if(e.getNewSlot() == e.getPreviousSlot() +1 || (e.getNewSlot() == 0 && e.getPreviousSlot() == 8)){
			getPlayerEditor(player.getUniqueId()).cycleAxis(1);
		}else if(e.getNewSlot() == e.getPreviousSlot() - 1 || (e.getNewSlot() == 8 && e.getPreviousSlot() == 0)){
			getPlayerEditor(player.getUniqueId()).cycleAxis(-1);
		}
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled=false)
	void onPlayerMenuSelect(InventoryClickEvent e){
		if(e.getInventory().getHolder() == null) return;
		if(!(e.getInventory().getHolder() instanceof ASEHolder)) return;
		if(e.getInventory().getHolder() == menuHolder){
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			if(item!= null && item.hasItemMeta()){
				Player player = (Player) e.getWhoClicked();
				String command = item.getItemMeta().getPersistentDataContainer().get(plugin.getIconKey(), PersistentDataType.STRING);
				if(command != null){
					player.performCommand(command);
					return;
				}
			}
		}
		if(e.getInventory().getHolder() == equipmentHolder){
			ItemStack item = e.getCurrentItem();
			if(item == null) return;
			if(item.getItemMeta() == null ) return;
			if(item.getItemMeta().getPersistentDataContainer().has(plugin.getIconKey(), PersistentDataType.STRING)){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled=true)
	void onPlayerMenuClose(InventoryCloseEvent e){
		if(e.getInventory().getHolder() == null) return;
		if(!(e.getInventory().getHolder() instanceof ASEHolder)) return;
		if(e.getInventory().getHolder() == equipmentHolder){
			PlayerEditor pe = players.get(e.getPlayer().getUniqueId());
			pe.equipMenu.equipArmorstand();
		}
	}

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

	private void removePlayerEditor(UUID uuid){
		players.remove(uuid);
	}

	public ASEHolder getMenuHolder() {
		return menuHolder;
	}

	public ASEHolder getEquipmentHolder() {
		return equipmentHolder;
	}

	long getTime(){
		return counter.ticks;
	}

	class TickCounter implements Runnable{
		long ticks = 0; //I am optimistic
		@Override
		public void run() {ticks++;}
		public long getTime() {return ticks;}
	}
}