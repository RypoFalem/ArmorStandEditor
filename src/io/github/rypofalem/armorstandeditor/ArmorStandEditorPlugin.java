package io.github.rypofalem.armorstandeditor;

import io.github.rypofalem.armorstandmanipulation.editor.PlayerEditorManager;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class ArmorStandEditorPlugin extends JavaPlugin{
	CommandEx execute;
	public PlayerEditorManager editor;
	public Material editTool;
	boolean debug = true;
	
	public void onEnable(){
		editTool = Material.FLINT;
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
