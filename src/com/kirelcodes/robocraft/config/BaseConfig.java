package com.kirelcodes.robocraft.config;

import java.io.File;
import java.io.InputStreamReader;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kirelcodes.robocraft.RoboCraft;

public class BaseConfig {

	private FileConfiguration baseConfig = null;
	private File baseConfigFile = null;
	private String configName = "";
	///////////////////////////////////
	
	public void reloadConfig(){
		if(baseConfigFile == null){ // Checks if the file is null
			baseConfigFile = new File(RoboCraft.getInstance().getDataFolder(), configName + ".yml"); // Creates config file
		}
		baseConfig = YamlConfiguration.loadConfiguration(baseConfigFile); // Loads the config from the file
	}
	public FileConfiguration getConfig(){
		if(baseConfig == null)
			reloadConfig();
		return baseConfig;
	}
	public void saveConfig(){
		if(baseConfig == null | baseConfigFile == null)
			return;
		try{
			getConfig().save(baseConfigFile);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void saveDeafultConfig(){
		if(baseConfigFile == null)
			baseConfigFile = new File(RoboCraft.getInstance().getDataFolder(), configName + ".yml");
		if(!baseConfigFile.exists())
			RoboCraft.getInstance().saveResource(configName + ".yml", false);
	}
	
	public FileConfiguration getDefaultConfig(){
		InputStreamReader reader = new InputStreamReader(RoboCraft.getInstance().getResource(configName + ".yml"));
		return YamlConfiguration.loadConfiguration(reader);
	}
	
	///////////////////////////////////////
	
	public String getName(){
		return configName;
	};
	public BaseConfig(String name){
		this.configName = name;
		saveDeafultConfig();
		reloadConfig();
	}
	protected String getVariableColorCode(String variable) {
		return ChatColor.translateAlternateColorCodes('&', getVariable(variable));
	}
	protected String getVariable(String variable) {
		if (getConfig().contains(variable) && getConfig().isString(variable))
			return getConfig().getString(variable);
		if(!getDefaultConfig().contains(variable)){
			return "MISSING CONFIG";
		}
		String newVariable = getDefaultConfig().getString(variable);
		getConfig().set(variable, newVariable);
		saveConfig();
		return newVariable;
	}
}
