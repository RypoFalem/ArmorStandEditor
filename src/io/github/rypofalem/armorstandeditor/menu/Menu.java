package io.github.rypofalem.armorstandeditor.menu;

import io.github.rypofalem.armorstandeditor.PlayerEditor;
import io.github.rypofalem.armorstandeditor.Util;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Menu {
	Inventory menuInv;
	private PlayerEditor pe;
	static String name = "Armor Stand Editor Menu";

	public Menu(PlayerEditor pe){
		this.pe = pe;
		name = pe.plugin.getLang().getMessage("mainmenutitle", "menutitle");
		menuInv = Bukkit.createInventory(pe.getManager().getPluginHolder(), 54, name);
		fillInventory();
	}

	private void fillInventory() {
		menuInv.clear();
		ItemStack xAxis= null, yAxis= null, zAxis= null, coarseAdj= null, fineAdj= null, rotate = null, place = null,
				headPos= null, target = null,
				rightArmPos= null, bodyPos= null, leftArmPos= null, showArms= null, visibility= null, size= null,
				rightLegPos= null, equipment = null, leftLegPos= null, disableSlots = null, gravity= null, plate= null, copy= null, paste= null,
				slot1= null, slot2= null, slot3= null, slot4= null, slot5= null, slot6= null, slot7= null, slot8= null, slot9 = null;

		if(pe.getPlayer().hasPermission("asedit.head") ||
				pe.getPlayer().hasPermission("asedit.body") ||
				pe.getPlayer().hasPermission("asedit.leftarm") ||
				pe.getPlayer().hasPermission("asedit.rightarm") ||
				pe.getPlayer().hasPermission("asedit.leftleg") ||
				pe.getPlayer().hasPermission("asedit.rightleg") ||
				pe.getPlayer().hasPermission("asedit.placement")){
			xAxis = createIcon(new ItemStack(Material.WOOL, 1, (short) 14),
					"xaxis", "axis x");

			yAxis = createIcon(new ItemStack(Material.WOOL, 1, (short) 13),
					"yaxis", "axis y");

			zAxis = createIcon(new ItemStack(Material.WOOL, 1, (short) 11),
					"zaxis", "axis z");

			coarseAdj = createIcon(new ItemStack(Material.DIRT, 1, (short)1),
					"coarseadj", "adj coarse");

			fineAdj = createIcon( new ItemStack(Material.SANDSTONE),
					"fineadj", "adj fine");
		}

		if(pe.getPlayer().hasPermission("asedit.head")){
			headPos = createIcon( new ItemStack(Material.LEATHER_HELMET),
					"head", "mode head");
		}

		if(pe.getPlayer().hasPermission("asedit.body")){
			bodyPos = createIcon( new ItemStack(Material.LEATHER_CHESTPLATE),
					"body", "mode body");
		}

		if(pe.getPlayer().hasPermission("asedit.leftleg")){
			leftLegPos = createIcon( new ItemStack(Material.LEATHER_LEGGINGS),
					"leftleg", "mode leftleg");
		}

		if(pe.getPlayer().hasPermission("asedit.rightleg")){
			rightLegPos = createIcon( new ItemStack(Material.LEATHER_LEGGINGS),
					"rightleg", "mode rightleg");
		}

		if(pe.getPlayer().hasPermission("asedit.leftarm")){
			leftArmPos = createIcon( new ItemStack(Material.STICK),
					"leftarm", "mode leftarm");
		}

		if(pe.getPlayer().hasPermission("asedit.rightarm")){
			rightArmPos = createIcon( new ItemStack(Material.STICK),
					"rightarm", "mode rightarm");
		}

		if(pe.getPlayer().hasPermission("asedit.showarms")){
			showArms = createIcon( new ItemStack(Material.STICK),
					"showarms", "mode showarms");
		}

		if(pe.getPlayer().hasPermission("asedit.invisible")){
			visibility = new ItemStack(Material.POTION, 1);
			PotionMeta potionMeta = (PotionMeta) visibility.getItemMeta();
			PotionEffect eff1 = new PotionEffect(PotionEffectType.INVISIBILITY, 1, 0);
			potionMeta.addCustomEffect(eff1, true);
			visibility.setItemMeta(potionMeta);
			visibility = createIcon( visibility,
					"invisible", "mode invisible");
		}

		if(pe.getPlayer().hasPermission("asedit.size")){
			size = createIcon( new ItemStack(Material.RAW_FISH, 1, (short)3),
					"size", "mode size");
		}

		if(pe.getPlayer().hasPermission("asedit.disableslots")){
			disableSlots = createIcon(new ItemStack(Material.BARRIER),
					"disableslots", "mode disableslots");
		}

		if(pe.getPlayer().hasPermission("asedit.gravity")){
			gravity = createIcon( new ItemStack(Material.SAND),
					"gravity", "mode gravity");
		}

		if(pe.getPlayer().hasPermission("asedit.baseplate")){
			plate = createIcon( new ItemStack(Material.STEP, 1),
					"baseplate", "mode baseplate");
		}

		if(pe.getPlayer().hasPermission("asedit.placement")){
			place = createIcon( new ItemStack(Material.MINECART, 1),
					"placement", "mode placement");
		}

		if(pe.getPlayer().hasPermission("asedit.rotate")){
			rotate = createIcon( new ItemStack(Material.COMPASS, 1),
					"rotate", "mode rotate");
		}

		target = createIcon(new ItemStack(Material.END_CRYSTAL, 1),
				"target", "mode target");

		if(pe.getPlayer().hasPermission("asedit.equipment")){
			equipment = createIcon(new ItemStack(Material.CHEST, 1),
					"equipment", "mode equipment");
		}

		if(pe.getPlayer().hasPermission("asedit.copy")){
			copy = createIcon(new ItemStack(Material.BOOK_AND_QUILL),
					"copy","mode copy");

			paste = createIcon(new ItemStack(Material.ENCHANTED_BOOK),
					"paste","mode paste");

			slot1 = createIcon(new ItemStack(Material.YELLOW_FLOWER),
					"copyslot","slot 1", "1");

			slot2 = createIcon(new ItemStack(Material.RED_ROSE, 2, (short) 3),
					"copyslot", "slot 2", "2");

			slot3 = createIcon(new ItemStack(Material.RED_ROSE, 3, (short) 1),
					"copyslot","slot 3", "3");

			slot4 = createIcon( new ItemStack(Material.DOUBLE_PLANT, 4, (short) 5),
					"copyslot","slot 4", "4");

			slot5 = createIcon( new ItemStack(Material.RED_ROSE, 5, (short) 4),
					"copyslot","slot 5", "5");

			slot6 = createIcon( new ItemStack(Material.RED_ROSE, 6, (short) 2),
					"copyslot","slot 6", "6");

			slot7 = createIcon(new ItemStack(Material.RED_ROSE, 7, (short) 1),
					"copyslot","slot 7", "7");

			slot8 = createIcon( new ItemStack(Material.RED_ROSE, 8, (short) 8),
					"copyslot","slot 8", "8");

			slot9 = createIcon( new ItemStack(Material.DOUBLE_PLANT, 9, (short) 1),
					"copyslot","slot 9", "9");
		}
		ItemStack[] items = 
			{xAxis, yAxis, zAxis, null, coarseAdj, fineAdj, null, rotate, place,
					null, headPos, null, null, null, null, null, null, target,
					rightArmPos, bodyPos, leftArmPos, null, null, null, showArms, visibility, size,
					rightLegPos, equipment, leftLegPos, null, null, null, null, gravity, plate,
					null, null, null, null, copy, paste, null, null, null,
					slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8, slot9
			};
		menuInv.setContents(items);
	}

	private ItemStack createIcon(ItemStack icon, String path, String command){
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName(getIconName(path));
		ArrayList<String> loreList = new ArrayList<String>();
		loreList.add(Util.encodeHiddenLore("ase " + command));
		loreList.add(getIconDescription(path));
		meta.setLore(loreList);
		icon.setItemMeta(meta);
		return icon;
	}

	private ItemStack createIcon(ItemStack icon, String path, String command, String option){
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName(getIconName(path, option));
		ArrayList<String> loreList = new ArrayList<String>();
		loreList.add(Util.encodeHiddenLore("ase " + command));
		loreList.add(getIconDescription(path, option));
		meta.setLore(loreList);
		icon.setItemMeta(meta);
		return icon;
	}

	private String getIconName(String path){
		return getIconName(path, null);
	}

	private String getIconName(String path, String option){
		return pe.plugin.getLang().getMessage(path, "iconname", option);
	}

	private String getIconDescription(String path){
		return getIconDescription(path, null);
	}

	private String getIconDescription(String path, String option){
		return pe.plugin.getLang().getMessage(path + ".description", "icondescription", option);
	}

	public void openMenu(){
		if(pe.getPlayer().hasPermission("asedit.basic")){
			fillInventory();
			pe.getPlayer().openInventory(menuInv);
		}
	}

	public static String getName(){
		return name;
	}
}