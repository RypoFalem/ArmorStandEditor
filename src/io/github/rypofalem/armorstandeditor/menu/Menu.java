package io.github.rypofalem.armorstandeditor.menu;

import io.github.rypofalem.armorstandeditor.PlayerEditor;
import io.github.rypofalem.armorstandeditor.Util;

import java.util.ArrayList;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class Menu {
	Inventory menuInv;
	private PlayerEditor pe;

	public Menu(PlayerEditor pe){
		this.pe = pe;
		menuInv = Bukkit.createInventory(pe.getManager().getPluginHolder(), 54, "Select an option!");
		fillInventory();
	}

	private void fillInventory() {
		menuInv.clear();
		ArrayList<String> lore = new ArrayList<String>();
		
		ItemStack xAxis= null, yAxis= null, zAxis= null, coarseAdj= null, fineAdj= null, headPos= null, place = null,
		rightArmPos= null, bodyPos= null, leftArmPos= null, showArms= null, visibility= null, size= null,
		rightLegPos= null, leftLegPos= null, gravity= null, plate= null, copy= null, paste= null,
		slot1= null, slot2= null, slot3= null, slot4= null, slot5= null, slot6= null, slot7= null, slot8= null, slot9 = null;
		
		ItemMeta iMeta;

		if(pe.getPlayer().hasPermission("asedit.head") ||
				pe.getPlayer().hasPermission("asedit.body") ||
				pe.getPlayer().hasPermission("asedit.leftarm") ||
				pe.getPlayer().hasPermission("asedit.rightarm") ||
				pe.getPlayer().hasPermission("asedit.leftleg") ||
				pe.getPlayer().hasPermission("asedit.rightleg")){
			
			xAxis = new ItemStack(Material.WOOL, 1, (short) 14);
			iMeta = xAxis.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "X Axis");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase axis x")));
			iMeta.setLore(lore);
			xAxis.setItemMeta(iMeta);

			yAxis = new ItemStack(Material.WOOL, 1, (short) 13);
			iMeta = yAxis.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Y Axis");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase axis y")));
			iMeta.setLore(lore);
			yAxis.setItemMeta(iMeta);

			zAxis = new ItemStack(Material.WOOL, 1, (short) 11);
			iMeta = zAxis.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Z Axis");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase axis z")));
			iMeta.setLore(lore);
			zAxis.setItemMeta(iMeta);

			coarseAdj = new ItemStack(Material.DIRT, 1, (short)1);
			iMeta = coarseAdj.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Coarse Adjustment");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase adj coarse")));
			iMeta.setLore(lore);
			coarseAdj.setItemMeta(iMeta);

			fineAdj = new ItemStack(Material.SANDSTONE);
			iMeta = fineAdj.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Fine Adjustment");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase adj fine")));
			iMeta.setLore(lore);
			fineAdj.setItemMeta(iMeta);
		}

		if(pe.getPlayer().hasPermission("asedit.head")){
			headPos = new ItemStack(Material.LEATHER_HELMET);
			iMeta = headPos.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Head Position");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode head")));
			iMeta.setLore(lore);
			headPos.setItemMeta(iMeta);
		}

		if(pe.getPlayer().hasPermission("asedit.body")){
			bodyPos = new ItemStack(Material.LEATHER_CHESTPLATE);
			iMeta = bodyPos.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Body Position");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode body")));
			iMeta.setLore(lore);
			bodyPos.setItemMeta(iMeta);
		}

		if(pe.getPlayer().hasPermission("asedit.leftleg")){
			leftLegPos = new ItemStack(Material.LEATHER_LEGGINGS);
			iMeta = leftLegPos.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Left Leg Position");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode leftleg")));
			iMeta.setLore(lore);
			leftLegPos.setItemMeta(iMeta);
		}

		if(pe.getPlayer().hasPermission("asedit.rightleg")){
			rightLegPos = new ItemStack(Material.LEATHER_LEGGINGS);
			iMeta = rightLegPos.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Right Leg Position");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode rightleg")));
			iMeta.setLore(lore);
			rightLegPos.setItemMeta(iMeta);
		}

		if(pe.getPlayer().hasPermission("asedit.leftarm")){
			leftArmPos = new ItemStack(Material.STICK);
			iMeta = leftArmPos.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Left Arm Position");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode leftarm")));
			iMeta.setLore(lore);
			leftArmPos.setItemMeta(iMeta);
		}

		if(pe.getPlayer().hasPermission("asedit.rightarm")){
			rightArmPos = new ItemStack(Material.STICK);
			iMeta = rightArmPos.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Right Arm Position");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode rightarm")));
			iMeta.setLore(lore);
			rightArmPos.setItemMeta(iMeta);
		}

		if(pe.getPlayer().hasPermission("asedit.showarms")){
			showArms = new ItemStack(Material.STICK);
			iMeta = showArms.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Show Arms");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode showarms")));
			iMeta.setLore(lore);
			showArms.setItemMeta(iMeta);
		}

		if(pe.getPlayer().hasPermission("asedit.invisible")){
			visibility = new Potion(PotionType.INVISIBILITY).toItemStack(1);
			iMeta = visibility.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Visibility");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode invisible")));
			iMeta.setLore(lore);
			visibility.setItemMeta(iMeta);
		}

		if(pe.getPlayer().hasPermission("asedit.size")){
			size = new ItemStack(Material.RAW_FISH, 1, (short)3);
			iMeta = size.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Size");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode size")));
			iMeta.setLore(lore);
			size.setItemMeta(iMeta);
		}

		if(pe.getPlayer().hasPermission("asedit.gravity")){
			gravity = new ItemStack(Material.SAND);
			iMeta = gravity.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Gravity");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode gravity")));
			iMeta.setLore(lore);
			gravity.setItemMeta(iMeta);
		}

		if(pe.getPlayer().hasPermission("asedit.baseplate")){
			plate = new ItemStack(Material.STEP, 1, (short) 0);
			iMeta = plate.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "BasePlate");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode baseplate")));
			iMeta.setLore(lore);
			plate.setItemMeta(iMeta);
		}
		
		if(pe.getPlayer().hasPermission("asedit.placement")){
			place = new ItemStack(Material.MINECART, 1, (short) 0);
			iMeta = place.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Placement");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode placement")));
			iMeta.setLore(lore);
			place.setItemMeta(iMeta);
		}

		if(pe.getPlayer().hasPermission("asedit.copy")){
			copy = new ItemStack(Material.BOOK_AND_QUILL);
			iMeta = copy.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Copy");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode copy")));
			iMeta.setLore(lore);
			copy.setItemMeta(iMeta);

			paste = new ItemStack(Material.ENCHANTED_BOOK);
			iMeta = paste.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Paste");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase mode paste")));
			iMeta.setLore(lore);
			paste.setItemMeta(iMeta);

			//Fuck flower's Material type.

			// IGN "Dandelion"
			slot1 = new ItemStack(Material.YELLOW_FLOWER);
			iMeta = slot1.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Copy Slot 1");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase slot 1")));
			iMeta.setLore(lore);
			slot1.setItemMeta(iMeta);

			// Red_Rose 3 is the first thing to come to mind when I think "Azure Bluet"
			slot2 = new ItemStack(Material.RED_ROSE, 2, (short) 3);
			iMeta = slot2.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Copy Slot 2");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase slot 2")));
			iMeta.setLore(lore);
			slot2.setItemMeta(iMeta);

			//"Poppy" are you even trying, Materials?
			slot3 = new ItemStack(Material.RED_ROSE, 3, (short) 1);
			iMeta = slot3.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Copy Slot 3");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase slot 3")));
			iMeta.setLore(lore);
			slot3.setItemMeta(iMeta);

			//"Peony" Oh, a third material type with some arbitray value attached
			slot4 = new ItemStack(Material.DOUBLE_PLANT, 4, (short) 5);
			iMeta = slot4.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Copy Slot 4");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase slot 4")));
			iMeta.setLore(lore);
			slot4.setItemMeta(iMeta);

			//"Red Tulip" I am honestly surprised they didn't stick this one under "yellow flower"
			// I can only assume they thought that would be consistently inconsistent and decided to opt
			// for inconsistently inconsistent instead.
			slot5 = new ItemStack(Material.RED_ROSE, 5, (short) 4);
			iMeta = slot5.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Copy Slot 5");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase slot 5")));
			iMeta.setLore(lore);
			slot5.setItemMeta(iMeta);

			//"Allium" this purple flower is also a red rose.
			slot6 = new ItemStack(Material.RED_ROSE, 6, (short) 2);
			iMeta = slot6.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Copy Slot 6");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase slot 6")));
			iMeta.setLore(lore);
			slot6.setItemMeta(iMeta);

			//"Blue Orchid" but seriously, what where these guys thinking?
			slot7 = new ItemStack(Material.RED_ROSE, 7, (short) 1);
			iMeta = slot7.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Copy Slot 7");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase slot 7")));
			iMeta.setLore(lore);
			slot7.setItemMeta(iMeta);

			//"Oxeye Daisy" Almost getting tired of mocking this
			slot8 = new ItemStack(Material.RED_ROSE, 8, (short) 8);
			iMeta = slot8.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Copy Slot 8");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase slot 8")));
			iMeta.setLore(lore);
			slot8.setItemMeta(iMeta);

			//"Rose bush", goes by the pseudonym "DOUBLE_PLANT:1"
			slot9 = new ItemStack(Material.DOUBLE_PLANT, 9, (short) 1);
			iMeta = slot9.getItemMeta();
			iMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Copy Slot 9");
			lore = new ArrayList<String>();
			lore.add((Util.encodeHiddenLore("ase slot 9")));
			iMeta.setLore(lore);
			slot9.setItemMeta(iMeta);
		}

		ItemStack[] items = 
			{xAxis, yAxis, zAxis, null, coarseAdj, fineAdj, null, null, place,
				null, headPos, null, null, null, null, null, null, null,
				rightArmPos, bodyPos, leftArmPos, null, null, null, showArms, visibility, size,
				rightLegPos, null, leftLegPos, null, null, null, null, gravity, plate,
				null, null, null, null, copy, paste, null, null, null,
				slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8, slot9
			};
		menuInv.setContents(items);
	}

	public void openMenu(){
		pe.getPlayer().openInventory(menuInv);
	}
}
