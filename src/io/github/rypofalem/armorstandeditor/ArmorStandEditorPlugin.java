package io.github.rypofalem.armorstandeditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class ArmorStandEditorPlugin extends JavaPlugin{
	CommandEx execute;
	public PlayerEditorManager editorManager;
	public Material editTool;
	boolean debug = false;
	double coarseRot;
	double fineRot;
	WorldGuardPlugin wgPlugin;
	GriefPrevention gpPlugin;

	public void onEnable(){
		saveDefaultConfig();
		coarseRot = getConfig().getDouble("coarse");
		fineRot = getConfig().getDouble("fine");
		String tool = getConfig().getString("tool");
		editTool = Material.getMaterial(tool);
		editorManager = new PlayerEditorManager(this);
		execute = new CommandEx(this);
		getCommand("ase").setExecutor(execute);
		getServer().getPluginManager().registerEvents(editorManager, this);
		if(isPluginEnabled("WorldGuard")){
			wgPlugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
			log("WoldGuard detected");
		}
		if(isPluginEnabled("GriefPrevention")){
			gpPlugin = (GriefPrevention) getServer().getPluginManager().getPlugin("GriePrevention");
			log("GriefPrevention detected");
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
			getLogger().info("\n***********************\n***********************\n***********************\n"
					+ "ArmorStandEditor Encountered an error! Check plugins/ArmorStandEditor/log.txt"
					+"\nYou should send this file to the developer."
					+ "\n***********************\n***********************\n***********************");
		}
	}
	
	public boolean isPluginEnabled(String plugin){
		return getServer().getPluginManager().isPluginEnabled(plugin);
	}
	
	public WorldGuardPlugin getWGPlugin(){
		if(wgPlugin != null && wgPlugin.isEnabled()){
			return wgPlugin;
		}
		return null;
	}
	
	public GriefPrevention getGPPlugin(){
		if(gpPlugin != null && gpPlugin.isEnabled()){
			return gpPlugin;
		}
		return null;
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
				list = list +" :" + p.getName() + ": ";
			}
		}
		return list;
	}
}