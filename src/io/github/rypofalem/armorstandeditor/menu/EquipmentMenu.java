package io.github.rypofalem.armorstandeditor.menu;

import java.util.ArrayList;

import org.bukkit.Bukkit;
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
		name = pe.plugin.getLang().getMessage("equiptitle", "menutitle");
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
		
		ItemStack disabledIcon = new ItemStack(Material.BARRIER);
		ItemMeta meta = disabledIcon.getItemMeta();
		meta.setDisplayName(pe.plugin.getLang().getMessage("disabled", "warn")); //equipslot.msg <option>
		ArrayList<String> loreList = new ArrayList<String>();
		loreList.add(Util.encodeHiddenLore("ase icon"));
		meta.setLore(loreList);
		disabledIcon.setItemMeta(meta);
		
		ItemStack helmetIcon = createIcon(Material.LEATHER_HELMET, "helm");
		ItemStack chestIcon = createIcon(Material.LEATHER_CHESTPLATE, "chest");
		ItemStack pantsIcon = createIcon(Material.LEATHER_LEGGINGS, "pants");
		ItemStack feetsiesIcon = createIcon(Material.LEATHER_BOOTS, "boots");
		ItemStack rightHandIcon = createIcon(Material.WOOD_SWORD, "rhand");
		ItemStack leftHandIcon = createIcon(Material.SHIELD, "lhand");
		ItemStack[] items = 
			{ helmetIcon, chestIcon, pantsIcon, feetsiesIcon, rightHandIcon, leftHandIcon, disabledIcon, disabledIcon, disabledIcon,
					helmet, chest, pants, feetsies, rightHand, leftHand, disabledIcon, disabledIcon, disabledIcon
			};
		menuInv.setContents(items);
	}
	
	private ItemStack createIcon(Material mat, String slot){
		ItemStack icon = new ItemStack(mat);
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName(pe.plugin.getLang().getMessage("equipslot", "iconname", slot)); //equipslot.msg <option>
		ArrayList<String> loreList = new ArrayList<String>();
		loreList.add(pe.plugin.getLang().getMessage("equipslot.description", "icondescription", slot)); //equioslot.description.msg <option>
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
