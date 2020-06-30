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

import io.github.rypofalem.armorstandeditor.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ArmorStandEditorPlugin extends JavaPlugin{
	private NamespacedKey iconKey;
	private static ArmorStandEditorPlugin instance;
	private CommandEx execute;
	private Language lang;
	public PlayerEditorManager editorManager;
	public Material editTool = Material.FLINT;
	boolean requireToolData = false;
	boolean sendToActionBar = true;
	int editToolData = Integer.MIN_VALUE;
	boolean requireToolLore = false;
	String editToolLore = null;
	boolean debug = false; //weather or not to broadcast messages via print(String message)
	double coarseRot;
	double fineRot;

	public ArmorStandEditorPlugin(){
		instance = this;
	}

	@Override
	public void onEnable(){
		//saveResource doesn't accept File.seperator on windows, need to hardcode unix seperator "/" instead
		updateConfig("", "config.yml");
		updateConfig("lang/", "test_NA.yml");
		updateConfig("lang/", "nl_NL.yml");
		updateConfig("lang/", "uk_UA.yml");
		updateConfig("lang/", "zh.yml");
		updateConfig("lang/", "fr_FR.yml");
		updateConfig("lang/", "ro_RO.yml");
		updateConfig("lang/", "ja_JP.yml");
		//English is the default language and needs to be unaltered to so that there is always a backup message string
		saveResource("lang/en_US.yml", true);
		lang = new Language(getConfig().getString("lang"), this);

		coarseRot = getConfig().getDouble("coarse");
		fineRot = getConfig().getDouble("fine");
		String toolType = getConfig().getString("tool", "FLINT");
		editTool = Material.getMaterial(toolType);
		requireToolData = getConfig().getBoolean("requireToolData", false);
		if(requireToolData) editToolData = getConfig().getInt("toolData", Integer.MIN_VALUE);
		requireToolLore = getConfig().getBoolean("requireToolLore", false);
		if(requireToolLore) editToolLore= getConfig().getString("toolLore", null);
		debug = getConfig().getBoolean("debug", true);
		sendToActionBar = getConfig().getBoolean("sendMessagesToActionBar", true);

		editorManager = new PlayerEditorManager(this);
		execute = new CommandEx(this);
		getCommand("ase").setExecutor(execute);
		getServer().getPluginManager().registerEvents(editorManager, this);
	}

	private void updateConfig(String folder, String config) {
		if(!new File(getDataFolder() + File.separator + folder + config).exists()){
			saveResource(folder  + config, false);
		}
	}

	@Override
	public void onDisable(){
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(player.getOpenInventory() == null) continue;
			if(player.getOpenInventory().getTopInventory().getHolder() == editorManager.getMenuHolder()) player.closeInventory();
		}
	}

	public void log(String message){
		this.getServer().getLogger().info("ArmorStandEditor: " + message);
	}

	public void print(String message){
		if(debug){
			this.getServer().broadcastMessage(message);
		}
	}

	public String listPlugins(){
		Plugin[] plugins = getServer().getPluginManager().getPlugins();
		String list = "";
		for(Plugin p : plugins){
			if(p!=null){
				list = list +" :" + p.getName() + " " + p.getDescription().getVersion() + ": ";
			}
		}
		return list;
	}

	public static ArmorStandEditorPlugin instance(){
		return instance;
	}

	public Language getLang(){
		return lang;
	}
	
	public boolean isEditTool(ItemStack item){
		if(item == null) return false;
		if(editTool != item.getType()) return false;
		if(requireToolData && item.getDurability() != (short)editToolData) return false;
		if(requireToolLore && editToolLore != null){
			if(!item.hasItemMeta()) return false;
			if(!item.getItemMeta().hasLore()) return false;
			print("has Lore");
			if(item.getItemMeta().getLore().isEmpty()) return false;
			print("lore not empty");
			if(!item.getItemMeta().getLore().get(0).equals(editToolLore)) return false;
		}
		return true;
	}

	public NamespacedKey getIconKey() {
		if(iconKey == null) iconKey = new NamespacedKey(this, "command_icon");
		return iconKey;
	}
}
//todo: 
//Access to "DisabledSlots" data (probably simplified just a toggle enable/disable)
//Access to the "Marker" switch (so you can make the hitbox super small)