package io.github.rypofalem.armorstandeditor;

import io.github.rypofalem.armorstandeditor.language.Language;
import io.github.rypofalem.armorstandeditor.protection.ClaimsProtection;
import io.github.rypofalem.armorstandeditor.protection.GPProtection;
import io.github.rypofalem.armorstandeditor.protection.PlotSqProtection;
import io.github.rypofalem.armorstandeditor.protection.Protection;
import io.github.rypofalem.armorstandeditor.protection.TownyProtection;
import io.github.rypofalem.armorstandeditor.protection.WGProtection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.towny.Towny;
import com.plotsquared.bukkit.BukkitMain;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.winthier.claims.bukkit.BukkitClaimsPlugin;

public class ArmorStandEditorPlugin extends JavaPlugin{
	CommandEx execute;
	Language lang;
	public PlayerEditorManager editorManager;
	public Material editTool;
	boolean debug = false; //weather or not to broadcast messages via print(String message)
	double coarseRot;
	double fineRot;
	private ArrayList<Protection> protections;

	public void onEnable(){
		saveDefaultConfig();
		updateConfig("", "config.yml");
		updateConfig("lang", "en_US.yml");
		updateConfig("lang", "test_NA.yml");
		updateConfig("lang", "nl_NL.yml");
		lang = new Language(getConfig().getString("lang"), this);

		coarseRot = getConfig().getDouble("coarse");
		fineRot = getConfig().getDouble("fine");
		String tool = getConfig().getString("tool");
		editTool = Material.getMaterial(tool);

		editorManager = new PlayerEditorManager(this);
		execute = new CommandEx(this);
		getCommand("ase").setExecutor(execute);
		getServer().getPluginManager().registerEvents(editorManager, this);

		protections = new ArrayList<Protection>();
		if(isPluginEnabled("WorldGuard")){
			WorldGuardPlugin wgPlugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
			addProtection(new WGProtection(wgPlugin));
		}

		if(isPluginEnabled("GriefPrevention")){
			Plugin gpPlugin =  getServer().getPluginManager().getPlugin("GriefPrevention");
			if(gpPlugin instanceof GriefPrevention) addProtection(new GPProtection( (GriefPrevention)gpPlugin));
		}

		if(isPluginEnabled("PlotSquared")){
			Plugin plotSqPlugin = getServer().getPluginManager().getPlugin("PlotSquared");
			if(plotSqPlugin instanceof BukkitMain) addProtection(new PlotSqProtection((BukkitMain) plotSqPlugin));
		}

		if(isPluginEnabled("Claims")){
			Plugin bcp = getServer().getPluginManager().getPlugin("Claims");
			if(bcp instanceof BukkitClaimsPlugin) addProtection(new ClaimsProtection((BukkitClaimsPlugin) bcp));
		}
		
		if(isPluginEnabled("Towny")){
			Plugin towny = getServer().getPluginManager().getPlugin("Towny");
			if(towny instanceof Towny) addProtection(new TownyProtection((Towny)towny));
		}
	}

	//add missing configuration values
	private void updateConfig(String folder, String config) {
		YamlConfiguration defaultConfig;
		YamlConfiguration currentConfig;
		try {
			InputStream input;
			if(folder == "" || folder == null){
				input = getResource(config);
			} else{
				input = getResource(folder + "/" + config); //getResource doesn't accept File.seperator on windows, need to hardcode unix seperator "/" instead
				config = folder + File.separator + config;
			}
			Reader defaultConfigStream = new InputStreamReader(input, "UTF8");
			defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);
			currentConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), config));
			if(currentConfig == null || defaultConfig == null){
				log("Either the current or default configuration could not me loaded.");
				return;
			}
			for(String key : defaultConfig.getKeys(true)){
				if(!currentConfig.contains(key)){
					currentConfig.set(key, defaultConfig.get(key));
					print(String.format("key not found %s. Setting value", key));
				}
			}
			currentConfig.save(new File(getDataFolder(), config));
		} catch (Exception e) {
			e.printStackTrace();
			log("Failed to update configuration: " + config);
			return;
		}
	}

	public void onDisable(){
	}

	void logError(Throwable throwable){
		for(World world : getServer().getWorlds()){
			for(Player player : world.getPlayers()){
				player.closeInventory();
			}
		}
		Writer writer = null;
		try {
			File logFileDir = new File("plugins/ArmorStandEditor/");
			logFileDir.mkdirs();
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("plugins/ArmorStandEditor/log.txt"), true)));
			writer.append("********"+ new Date(System.currentTimeMillis()).toString() + "********\n\n");
			writer.append(Bukkit.getServer().getVersion() + "\n");
			writer.append("Plugins: " + listPlugins() + "\n\n\n");
			writer.append(throwable.getClass().getName() + "\n");
			for(StackTraceElement ste: throwable.getStackTrace()){
				writer.append(ste.toString());
				writer.append("\n");
			}
			writer.append("\n\n");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {writer.close();} catch (Exception ex) {}
			getLogger().info(
					"\n***********************\n***********************\n***********************\n"
							+ "ArmorStandEditor Encountered an error! Check plugins/ArmorStandEditor/log.txt"
							+"\nYou should send this file to the developer."
							+ "\n***********************\n***********************\n***********************");
		}
	}

	public boolean isPluginEnabled(String plugin){
		return getServer().getPluginManager().isPluginEnabled(plugin);
	}

	public void addProtection (Protection protection){
		protections.add(protection);
	}

	public ArrayList<Protection> getProtections(){
		return protections;
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

	public Language getLang(){
		return lang;
	}
}
//todo: 
//Localisation
//Access to "DisabledSlots" data (probably simplified just a toggle enable/disable)
//Access to the "Marker" switch (so you can make the hitbox super small)
//API so that developers can add their own means of having editing respect protected regions (the plugin already supports worldguard, greifprevention and plotsquared)
//Target a specific armorstand to edit, so solve the issue of editing armorstands close to each other and to make the Marker switch a viable option.
//Ability for users to set a per-user amount for coarse/fine adjustment
//Force an update packet when an armorstand moves a small amount so it feels more responsive.
//Menu description