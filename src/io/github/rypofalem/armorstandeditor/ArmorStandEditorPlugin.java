package io.github.rypofalem.armorstandeditor;

import io.github.rypofalem.armorstandeditor.language.Language;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ArmorStandEditorPlugin extends JavaPlugin{
	private static ArmorStandEditorPlugin instance;
	private CommandEx execute;
	private Language lang;
	public PlayerEditorManager editorManager;
	public Material editTool = Material.FLINT;
	boolean requireToolData = false;
	int editToolData = Integer.MIN_VALUE;
	boolean requireToolLore = false;
	String editToolLore = null;
	boolean debug = false; //weather or not to broadcast messages via print(String message)
	double coarseRot;
	double fineRot;

	public void onEnable(){
		instance = this;
		//saveResource doesn't accept File.seperator on windows, need to hardcode unix seperator "/" instead
		updateConfig("", "config.yml");
		updateConfig("lang/", "en_US.yml");
		updateConfig("lang/", "test_NA.yml");
		updateConfig("lang/", "nl_NL.yml");
		updateConfig("lang/", "uk_UA.yml");
		updateConfig("lang/", "zh.yml");
		updateConfig("lang/", "fr_FR.yml");
		lang = new Language(getConfig().getString("lang"), this);

		coarseRot = getConfig().getDouble("coarse");
		fineRot = getConfig().getDouble("fine");
		String toolType = getConfig().getString("tool");
		editTool = Material.getMaterial(toolType);
		requireToolData = getConfig().getBoolean("requireToolData");
		if(requireToolData) editToolData = getConfig().getInt("toolData");
		requireToolLore = getConfig().getBoolean("requireToolLore");
		if(requireToolLore) editToolLore= getConfig().getString("toolLore");
		debug = getConfig().getBoolean("debug");

		editorManager = new PlayerEditorManager(this);
		execute = new CommandEx(this);
		getCommand("ase").setExecutor(execute);
		getServer().getPluginManager().registerEvents(editorManager, this);
	}


	private void updateConfig(String folder, String config) {
		if(!new File(folder + config).exists()){
			saveResource(folder  + config, false);
		}

	}

	public void onDisable(){
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(player.getOpenInventory() == null) continue;
			if(player.getOpenInventory().getTopInventory().getHolder() == editorManager.getPluginHolder()) player.closeInventory();
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
}
//todo: 
//Access to "DisabledSlots" data (probably simplified just a toggle enable/disable)
//Access to the "Marker" switch (so you can make the hitbox super small)
//Target a specific armorstand to edit, so solve the issue of editing armorstands close to each other and to make the Marker switch a viable option.
//Ability for users to set a per-user amount for coarse/fine adjustment