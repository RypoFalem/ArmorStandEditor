package io.github.rypofalem.armorstandeditor.menu;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rypofalem.armorstandeditor.PlayerEditor;
import io.github.rypofalem.armorstandeditor.Util;

public class EquipmentMenu {
	Inventory menuInv;
	private PlayerEditor pe;
	private ArmorStand armorstand;
	static String name = "ArmorStand Equipment";
	ItemStack helmet, chest, pants, feetsies, rightHand, leftHand;
	
	public EquipmentMenu(PlayerEditor pe, ArmorStand as){
		this.pe = pe;
		this.armorstand = as;
		menuInv = Bukkit.createInventory(pe.getManager().getPluginHolder(), 18, name);
	}
	
	private void fillInventory(){
		menuInv.clear();
		EntityEquipment equipment = armorstand.getEquipment();
		ItemStack helmet = equipment.getHelmet();
		ItemStack chest = equipment.getChestplate();
		ItemStack pants = equipment.getLeggings();
		ItemStack feetsies = equipment.getBoots();
		ItemStack rightHand = equipment.getItemInMainHand();
		ItemStack leftHand = equipment.getItemInOffHand();
		equipment.clear();
		ItemStack disabledIcon = createIcon(Material.BARRIER, "Disabled", "This does nothing!");
		ItemStack helmetIcon = createIcon(Material.LEATHER_HELMET, "Helmet Slot", "Drag your helmet into the slot below");
		ItemStack chestIcon = createIcon(Material.LEATHER_CHESTPLATE, "Chestplate Slot", "Drag your chestplate into the slot below");
		ItemStack pantsIcon = createIcon(Material.LEATHER_LEGGINGS, "Helmet Slot", "Drag your pants into the slot below");
		ItemStack feetsiesIcon = createIcon(Material.LEATHER_BOOTS, "Feetsies Slot", "Drag your boots into the slot below");
		ItemStack rightHandIcon = createIcon(Material.WOOD_SWORD, "Right Hand Slot", "Drag your right hand item into the slot below");
		ItemStack leftHandIcon = createIcon(Material.SHIELD, "Left Hand Slot", "Drag your left hand item into the slot below");
		ItemStack[] items = 
			{ helmetIcon, chestIcon, pantsIcon, feetsiesIcon, rightHandIcon, leftHandIcon, disabledIcon, disabledIcon, disabledIcon,
					helmet, chest, pants, feetsies, rightHand, leftHand, disabledIcon, disabledIcon, disabledIcon
			};
		menuInv.setContents(items);
	}
	
	private ItemStack createIcon(Material mat, String name, String lore){
		ItemStack icon = new ItemStack(mat);
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + name);
		ArrayList<String> loreList = new ArrayList<String>();
		loreList.add(ChatColor.GREEN + lore);
		loreList.add(Util.encodeHiddenLore("ase icon"));
		meta.setLore(loreList);
		icon.setItemMeta(meta);
		return icon;
	}
	
	public void open(){
		fillInventory();
		pe.getPlayer().openInventory(menuInv);
	}
	
	public void equipArmorstand(){
		helmet = menuInv.getItem(9);
		chest = menuInv.getItem(10);
		pants = menuInv.getItem(11);
		feetsies = menuInv.getItem(12);
		rightHand = menuInv.getItem(13);
		leftHand = menuInv.getItem(14);
		armorstand.setHelmet(helmet);
		armorstand.setChestplate(chest);
		armorstand.setLeggings(pants);
		armorstand.setBoots(feetsies);
		armorstand.getEquipment().setItemInMainHand(rightHand);
		armorstand.getEquipment().setItemInOffHand(leftHand);
	}
	
	public static String getName(){
		return name;
	}
}
