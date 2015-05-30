package io.github.rypofalem.armorstandeditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ArmorStandEditorPlugin extends JavaPlugin{
	CommandEx execute;
	public PlayerEditorManager editor;
	public Material editTool;
	boolean debug = false;
	double coarseRot;
	double fineRot;

	public void onEnable(){
		saveDefaultConfig();
		coarseRot = getConfig().getDouble("coarse");
		fineRot = getConfig().getDouble("fine");
		String tool = getConfig().getString("tool");
		editTool = Material.getMaterial(tool);
		editor = new PlayerEditorManager(this);
		execute = new CommandEx(this);
		getCommand("ase").setExecutor(execute);
		getServer().getPluginManager().registerEvents(editor, this);
	}

	public void onDisable(){
		for(World world : getServer().getWorlds()){
			for(Player player : world.getPlayers()){
				player.closeInventory();
			}
		}
		getServer().getScheduler().cancelTasks(this);
	}

	void logError(Throwable throwable){
		Writer writer = null;
		print("Disabling ASE");
		try {
			File logFileDir = new File("plugins/ArmorStandEditor/");
			logFileDir.mkdirs();
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("plugins/ArmorStandEditor/log.txt"), true)));
			writer.append("********"+ new Date(System.currentTimeMillis()).toString() + "********\n\n");
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
					+ "Disabling ArmorStandEditor! Check plugins/ArmorStandEditor/log.txt"
					+ "\n***********************\n***********************\n***********************");
			this.getPluginLoader().disablePlugin(this);
		}
	}


	public void print(String message){
		if(debug){
			this.getServer().broadcastMessage(message);
		}
	}
}
