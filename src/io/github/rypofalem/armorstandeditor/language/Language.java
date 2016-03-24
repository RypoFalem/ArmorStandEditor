package io.github.rypofalem.armorstandeditor.language;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.rypofalem.armorstandeditor.ArmorStandEditorPlugin;

public class Language {
	final String DEFAULTLANG = "en_US.yml";
	private YamlConfiguration langConfig = null;
	private YamlConfiguration defConfig = null;
	private File langFile = null;
	private File defLangFile = null;
	ArmorStandEditorPlugin plugin;

	public Language(String langFileName, ArmorStandEditorPlugin plugin){
		if(langFileName == null) langFileName = DEFAULTLANG;
		this.plugin = plugin;
		reloadLang(langFileName);
	}

	public void reloadLang(String langFileName){
		if(langFileName == null) langFileName = DEFAULTLANG;
		File langFolder = new File(plugin.getDataFolder().getPath() + File.separator + "lang");
		defLangFile = new File(langFolder, DEFAULTLANG);
		langFile = new File(langFolder, langFileName);
		
		
		//load and save default language
		try {
			InputStream input = plugin.getResource("lang" + "/" + DEFAULTLANG); //getResource doesn't accept File.seperator on windows, need to hardcode unix seperator "/" instead
			Reader defaultLangStream = new InputStreamReader(input, "UTF8");
			if(defaultLangStream != null){
				defConfig = YamlConfiguration.loadConfiguration(defaultLangStream);
				defConfig.save(defLangFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		langConfig = YamlConfiguration.loadConfiguration(langFile);
	}

	public String getMessage(String path, String format, String option){
		if(langConfig == null) reloadLang(langFile.getName());
		if(path == null) return "";
		if(format == null) format = "info";
		if(option == null) option = "";

		format = getString(format);
		for(int i = 0; i<format.length(); i+=2){ //place formatting symbol before each character
			format = format.substring(0, i) + ChatColor.COLOR_CHAR + format.substring(i);
		}
		
		if( getString(path + "." + option) != null) option = getString(path + "." + option);
		String message = format + getString(path + ".msg");
		message = message.replace("<x>", option);
		return message;
	}

	public String getMessage(String path, String format){
		return getMessage(path, format, null);
	}

	public String getMessage(String path){
		return getMessage(path, "info");
	}

	private String getString(String path){
		String message = null;
		if(langConfig.contains(path)){
			message = langConfig.getString(path);
		}else if(defConfig.contains(path)){
			message = defConfig.getString(path);
		}
		return message;
	}
}