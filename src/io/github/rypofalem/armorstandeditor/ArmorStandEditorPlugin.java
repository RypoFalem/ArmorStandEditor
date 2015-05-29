package io.github.rypofalem.armorstandeditor;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class ArmorStandEditorPlugin extends JavaPlugin{
	CommandEx execute;
	public PlayerEditorManager editor;
	public Material editTool;
	boolean debug = true;
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
	
	public void print(String message){
		if(debug){
			this.getServer().broadcastMessage(message);
		}
	}
}
